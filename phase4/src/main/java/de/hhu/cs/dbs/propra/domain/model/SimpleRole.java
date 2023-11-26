package de.hhu.cs.dbs.propra.domain.model;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleRole implements Role {

    private final String value;
    private final Set<Role> includedRoles;

    public SimpleRole(String value, Set<Role> includedRoles) {
        this.value = value.toUpperCase(Locale.ENGLISH);
        Set<Role> roles = new HashSet<>();
        roles.add(this);
        roles.addAll(
                includedRoles.stream()
                        .flatMap(role -> role.getIncludedRoles().stream())
                        .collect(Collectors.toSet()));
        this.includedRoles = Set.copyOf(roles);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Set<Role> getIncludedRoles() {
        return includedRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleRole that = (SimpleRole) o;

        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }
}
