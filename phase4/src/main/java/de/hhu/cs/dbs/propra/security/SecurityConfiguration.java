package de.hhu.cs.dbs.propra.security;

import de.hhu.cs.dbs.propra.domain.model.Role;
import de.hhu.cs.dbs.propra.domain.repos.RoleRepository;

import jakarta.servlet.DispatcherType;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(jsr250Enabled = true, prePostEnabled = false)
public class SecurityConfiguration {
    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    static RoleHierarchy roleHierarchy(RoleRepository roleRepository) {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        Set<Role> roles = roleRepository.findAllRoles();
        String hierarchy = getHierarchy(roles);
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    private static String getHierarchy(Set<Role> roles) {
        return roles.stream()
                .map(
                        role -> {
                            Set<Role> includedRoles = new HashSet<>(role.getIncludedRoles());
                            includedRoles.remove(role);
                            return role.getValue() + getHierarchy(includedRoles);
                        })
                .collect(Collectors.joining("\n", " > ", ""));
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(
            RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        (requests) ->
                                requests.dispatcherTypeMatchers(
                                                DispatcherType.FORWARD, DispatcherType.ERROR)
                                        .permitAll()
                                        .anyRequest()
                                        .permitAll())
                .httpBasic(
                        (httpBasic) ->
                                httpBasic.realmName("DBS ProPra – Phase 4 – Implementation"));
        return http.build();
    }
}
