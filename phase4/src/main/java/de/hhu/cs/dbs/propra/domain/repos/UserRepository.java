package de.hhu.cs.dbs.propra.domain.repos;

import de.hhu.cs.dbs.propra.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    /**
     * Gibt einen {@link User} anhand von {@code nutzerId} wie bspw. Benutzername oder
     * E-Mail-Adresse zurück.
     *
     * @param uniqueString Eindeutige Zeichenkette, die für genau einen {@link User} steht.
     * @return {@link Optional<User>}, der einen {@link User} mit {@code username} zurückgibt.
     */
    public Optional<User> findUser(String uniqueString);
}
