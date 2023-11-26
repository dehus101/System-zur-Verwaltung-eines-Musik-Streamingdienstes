package de.hhu.cs.dbs.propra.persistence.inmemory;

import de.hhu.cs.dbs.propra.domain.model.Role;
import de.hhu.cs.dbs.propra.domain.repos.RoleRepository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@ConditionalOnMissingBean(name = "roleRepository")
public class InMemoryRoleRepository implements RoleRepository {

    private final Set<Role> roles;

    public InMemoryRoleRepository(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Set<Role> findAllRoles() {
        return Set.copyOf(roles);
    }
}
