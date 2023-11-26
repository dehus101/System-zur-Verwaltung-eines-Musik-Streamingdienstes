package de.hhu.cs.dbs.propra.persistence.inmemory;

import de.hhu.cs.dbs.propra.domain.model.User;
import de.hhu.cs.dbs.propra.domain.repos.UserRepository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
@ConditionalOnMissingBean(name = "userRepository")
public class InMemoryUserRepository implements UserRepository {

    private final Set<User> users;

    public InMemoryUserRepository(Set<User> users) {
        this.users = users;
    }

    @Override
    public Optional<User> findUser(String uniqueString) {
        return users.stream()
                .filter(user -> uniqueString.equals(user.getUniqueString()))
                .findFirst();
    }
}
