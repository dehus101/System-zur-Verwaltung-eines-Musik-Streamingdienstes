package de.hhu.cs.dbs.propra.domain.model;

import java.util.Set;

/** Spiegelt einen Nutzertyp in der Datenbank wider. */
public interface Role {

    String getValue();

    Set<Role> getIncludedRoles();
}
