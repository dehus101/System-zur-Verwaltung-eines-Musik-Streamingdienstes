package de.hhu.cs.dbs.propra.domain.services;

import de.hhu.cs.dbs.propra.domain.model.Kuenstler;
import de.hhu.cs.dbs.propra.domain.model.Song;

import java.util.Optional;

public interface KuenstlerService {
    Optional<Song> insertSong(
            String titel,
            int dauer,
            String speicherortHq,
            String speicherortLq,
            Integer genreid,
            Kuenstler kuenstler);

    Optional<Song> deleteSong(Integer songid, Kuenstler kuenstler);
}
