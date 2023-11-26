package de.hhu.cs.dbs.propra.persistence.inmemory;

import de.hhu.cs.dbs.propra.domain.errors.APIException;
import de.hhu.cs.dbs.propra.domain.model.Kuenstler;
import de.hhu.cs.dbs.propra.domain.model.Nutzer;
import de.hhu.cs.dbs.propra.domain.model.PremiumNutzer;
import de.hhu.cs.dbs.propra.domain.model.Role;
import de.hhu.cs.dbs.propra.domain.model.SimpleRole;
import de.hhu.cs.dbs.propra.domain.model.SimpleUser;
import de.hhu.cs.dbs.propra.domain.model.User;
import de.hhu.cs.dbs.propra.domain.repos.KuenstlerRepo;
import de.hhu.cs.dbs.propra.domain.repos.NutzerRepo;
import de.hhu.cs.dbs.propra.domain.repos.PremiumnutzerRepo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Configuration
public class InMemoryConfiguration {

    private final NutzerRepo nutzerRepo;
    private final PremiumnutzerRepo premiumnutzerRepo;
    private final KuenstlerRepo kuenstlerRepo;

    private final SQLiteDataSource dataSource;

    public InMemoryConfiguration(
            NutzerRepo nutzerRepo,
            PremiumnutzerRepo premiumnutzerRepo,
            KuenstlerRepo kuenstlerRepo,
            SQLiteDataSource dataSource) {
        this.nutzerRepo = nutzerRepo;
        this.premiumnutzerRepo = premiumnutzerRepo;
        this.kuenstlerRepo = kuenstlerRepo;
        this.dataSource = dataSource;
    }

    @ConditionalOnBean(InMemoryRoleRepository.class)
    @Bean
    Set<Role> roles() {
        Role nutzer = new SimpleRole("NUTZER", Collections.emptySet());
        Role premiumnutzer = new SimpleRole("PREMIUMNUTZER", Set.of(nutzer));
        Role kuenstler = new SimpleRole("KUENSTLER", Set.of(premiumnutzer));
        return Set.of(nutzer, kuenstler, premiumnutzer);
    }

    private Optional<User> findUserByUsername(List<User> users, String username) {
        return users.stream().filter(u -> u.getUniqueString().equals(username)).findFirst();
    }

    @ConditionalOnBean(InMemoryUserRepository.class)
    @Bean
    Set<User> users() {
        try (Connection conn = dataSource.getConnection()) {
            try {
                Role nutzerRolle = new SimpleRole("NUTZER", Collections.emptySet());
                Role premiumnutzerRolle = new SimpleRole("PREMIUMNUTZER", Collections.emptySet());
                Role kuenstlerRolle = new SimpleRole("KÜNSTLER", Collections.emptySet());
                ArrayList<Nutzer> nutzer = nutzerRepo.getAllNutzer(conn);
                ArrayList<PremiumNutzer> premiumnutzer =
                        premiumnutzerRepo.getAllPremiumnutzer(conn);
                ArrayList<Kuenstler> kuenstler = kuenstlerRepo.getAllKuenstler(conn);
                ArrayList<User> users = new ArrayList<>();

                for (Kuenstler k : kuenstler) {
                    users.add(
                            new SimpleUser(
                                    k.getPremiumNutzer().getNutzer().getBenutzername(),
                                    "{noop}" + k.getPremiumNutzer().getNutzer().getPassword(),
                                    new HashSet<>(Set.of(kuenstlerRolle))));
                }

                for (PremiumNutzer p : premiumnutzer) {
                    Optional<User> existingUser =
                            findUserByUsername(users, p.getNutzer().getBenutzername());
                    if (existingUser.isPresent()) {
                        HashSet<Role> roles = new HashSet<>(existingUser.get().getRoles());
                        roles.add(premiumnutzerRolle);
                        existingUser.get().setRoles(roles);
                    } else {
                        users.add(
                                new SimpleUser(
                                        p.getNutzer().getBenutzername(),
                                        "{noop}" + p.getNutzer().getPassword(),
                                        new HashSet<>(Set.of(premiumnutzerRolle))));
                    }
                }

                for (Nutzer n : nutzer) {
                    Optional<User> existingUser = findUserByUsername(users, n.getBenutzername());
                    if (existingUser.isPresent()) {
                        HashSet<Role> roles = new HashSet<>(existingUser.get().getRoles());
                        roles.add(nutzerRolle);
                        existingUser.get().setRoles(roles);
                    } else {
                        users.add(
                                new SimpleUser(
                                        n.getBenutzername(),
                                        "{noop}" + n.getPassword(),
                                        new HashSet<>(Set.of(nutzerRolle))));
                    }
                }

                return Set.copyOf(users);

            } catch (Exception e) {
                throw new APIException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
