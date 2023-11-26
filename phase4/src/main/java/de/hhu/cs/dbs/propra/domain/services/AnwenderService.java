package de.hhu.cs.dbs.propra.domain.services;

import de.hhu.cs.dbs.propra.domain.errors.APIException;
import de.hhu.cs.dbs.propra.domain.model.*;

import java.util.List;
import java.util.Optional;

public interface AnwenderService {

    List<Nutzer> getAllNutzerWithFilter(String email, String iban) throws APIException;

    Optional<Nutzer> insertNutzer(String email, String passwort, String benutzername, String iban)
            throws APIException;

    List<PremiumNutzer> getAllPremiumnutzerWithFilter(Integer gueltigkeitstage) throws APIException;

    Optional<PremiumNutzer> insertPremiumnutzer(
            String email,
            String passwort,
            String benutzername,
            String iban,
            String gueltigkeitstage)
            throws APIException;

    List<Kuenstler> getAllKuenstlerWithFilter(String email) throws APIException;

    Optional<Kuenstler> insertKuenstler(
            String email,
            String passwort,
            String benutzername,
            String iban,
            String gueltigkeitstage,
            String aktivSeit,
            String pseudonym)
            throws APIException;

    List<Song> getSongs() throws APIException;

    List<Album> getAlben() throws APIException;

    List<Band> getBands(String name);

    List<Playlist> getPlaylists(String name);

    List<Aufnahme> getAufnahmenByPlaylistsByID(String title, int playlistId);

    List<Kritik> getKritikenByAlbumID(int albumId);

    Optional<Kuenstler> getKuenstlerByUsername(String username);

    Optional<PremiumNutzer> getPremiumNutzerByUsername(String username);
}
