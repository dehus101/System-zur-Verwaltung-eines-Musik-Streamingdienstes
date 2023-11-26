package de.hhu.cs.dbs.propra.domain.repos;

import de.hhu.cs.dbs.propra.domain.model.Role;

import java.util.Set;

public interface RoleRepository {

    /**
     * Gibt alle möglichen Nutzertypen als Menge von {@link Role}-Instanzen zurück, indem die
     * Datenbank angefragt wird.
     *
     * @return {@link Set<Role>}, die {@link Role}-Instanzen zurückgibt.
     */
    public Set<Role> findAllRoles();
}
