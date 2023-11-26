package de.hhu.cs.dbs.propra.domain.model;

import java.util.Set;

public class SimpleUser implements User {

    private String uniqueString;
    private String password;
    private Set<Role> roles;

    public SimpleUser(String uniqueString, String password, Set<Role> roles) {
        this.uniqueString = uniqueString;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public String getUniqueString() {
        return uniqueString;
    }

    @Override
    public void setUniqueString(String uniqueString) {
        this.uniqueString = uniqueString;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }

    @Override
    public void setRoles(Set<Role> roles) {
        this.roles = Set.copyOf(roles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleUser that = (SimpleUser) o;

        return getUniqueString().equals(that.getUniqueString());
    }

    @Override
    public int hashCode() {
        return getUniqueString().hashCode();
    }
}
