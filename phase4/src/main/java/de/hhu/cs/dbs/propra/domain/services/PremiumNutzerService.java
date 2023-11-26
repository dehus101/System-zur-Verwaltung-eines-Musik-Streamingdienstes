package de.hhu.cs.dbs.propra.domain.services;

import de.hhu.cs.dbs.propra.domain.model.Aufnahme;
import de.hhu.cs.dbs.propra.domain.model.Playlist;
import de.hhu.cs.dbs.propra.domain.model.PremiumNutzer;

import java.util.Optional;

public interface PremiumNutzerService {
    Optional<Playlist> insertPlaylist(String name, Boolean privat, PremiumNutzer premiumNutzer);

    Optional<Playlist> deletePlaylist(int playlistid, PremiumNutzer premiumNutzer);

    Optional<Aufnahme> insertAufnahmeInPlaylist(
            int playlistid, int aufnahmeid, PremiumNutzer premiumNutzer);

    Optional<Aufnahme> deleteAufnahmeFromPlaylist(
            int playlistid, int aufnahmeid, PremiumNutzer premiumNutzer);
}
