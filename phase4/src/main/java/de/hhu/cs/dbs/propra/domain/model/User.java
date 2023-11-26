package de.hhu.cs.dbs.propra.domain.model;

import java.util.Set;

/** Spiegelt einen Nutzer der Anwendung wider. */
public interface User {

    String getUniqueString();

    void setUniqueString(String uniqueString);

    String getPassword();

    void setPassword(String password);

    Set<Role> getRoles();

    void setRoles(Set<Role> roles);
}
