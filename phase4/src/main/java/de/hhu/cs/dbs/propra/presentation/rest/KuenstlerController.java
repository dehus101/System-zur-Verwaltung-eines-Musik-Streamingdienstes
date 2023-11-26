package de.hhu.cs.dbs.propra.presentation.rest;

import static de.hhu.cs.dbs.propra.presentation.rest.ControllerHelper.errorResponse;

import de.hhu.cs.dbs.propra.domain.errors.APIException;
import de.hhu.cs.dbs.propra.domain.model.Kuenstler;
import de.hhu.cs.dbs.propra.domain.model.Song;
import de.hhu.cs.dbs.propra.domain.model.User;
import de.hhu.cs.dbs.propra.domain.services.AnwenderService;
import de.hhu.cs.dbs.propra.domain.services.KuenstlerService;
import de.hhu.cs.dbs.propra.security.CurrentUser;

import jakarta.annotation.security.RolesAllowed;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/")
@RestController
@RolesAllowed("KÜNSTLER")
public class KuenstlerController {

    private final KuenstlerService kuenstlerService;

    private final AnwenderService anwenderService;

    public KuenstlerController(KuenstlerService kuenstlerService, AnwenderService anwenderService) {
        this.kuenstlerService = kuenstlerService;
        this.anwenderService = anwenderService;
    }

    @PostMapping("songs")
    public ResponseEntity<?> insertSong(
            @CurrentUser User user,
            @RequestParam(value = "titel") String titel,
            @RequestParam(value = "dauer") int dauer,
            @RequestParam(value = "speicherort_hq") String speicherort_hq,
            @RequestParam(value = "speicherort_lq") String speicherort_lq,
            @RequestParam(value = "pseudonym", required = false) String pseudonym,
            @RequestParam(value = "genreid", required = false) Integer genreid) {

        try {
            Kuenstler kuenstler =
                    anwenderService.getKuenstlerByUsername(user.getUniqueString()).orElseThrow();
            Optional<Song> song =
                    kuenstlerService.insertSong(
                            titel, dauer, speicherort_hq, speicherort_lq, genreid, kuenstler);
            return song.map(
                            value ->
                                    ResponseEntity.status(HttpStatus.CREATED)
                                            .header("Location", "/songs/" + value.getSongid() + "/")
                                            .body(
                                                    "Song mit ID "
                                                            + value.getSongid()
                                                            + " wurde erstellt."))
                    .orElseGet(ControllerHelper::somethingWentWrongResponse);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @DeleteMapping("songs/{songid}")
    public ResponseEntity<?> deleteSong(
            @CurrentUser User user, @PathVariable(value = "songid") Integer songid) {
        try {
            Kuenstler kuenstler =
                    anwenderService.getKuenstlerByUsername(user.getUniqueString()).orElseThrow();
            Optional<Song> song = kuenstlerService.deleteSong(songid, kuenstler);
            return song.map(
                            value ->
                                    ResponseEntity.status(HttpStatus.OK)
                                            .body(
                                                    "Song mit ID "
                                                            + value.getSongid()
                                                            + " wurde gelöscht."))
                    .orElseGet(ControllerHelper::somethingWentWrongResponse);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }
}
