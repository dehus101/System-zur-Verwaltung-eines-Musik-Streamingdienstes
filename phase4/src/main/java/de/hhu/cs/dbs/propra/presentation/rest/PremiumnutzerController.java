package de.hhu.cs.dbs.propra.presentation.rest;

import static de.hhu.cs.dbs.propra.presentation.rest.ControllerHelper.errorResponse;

import de.hhu.cs.dbs.propra.domain.errors.APIException;
import de.hhu.cs.dbs.propra.domain.model.*;
import de.hhu.cs.dbs.propra.domain.services.AnwenderService;
import de.hhu.cs.dbs.propra.domain.services.PremiumNutzerService;
import de.hhu.cs.dbs.propra.security.CurrentUser;

import jakarta.annotation.security.RolesAllowed;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/")
@RestController
@RolesAllowed("PREMIUMNUTZER")
public class PremiumnutzerController {

    private final AnwenderService anwenderService;

    private final PremiumNutzerService premiumNutzerService;

    public PremiumnutzerController(
            AnwenderService anwenderService, PremiumNutzerService premiumNutzerService) {
        this.anwenderService = anwenderService;
        this.premiumNutzerService = premiumNutzerService;
    }

    @PostMapping("playlists")
    public ResponseEntity<?> insertPlaylist(
            @CurrentUser User user,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "privat") Boolean privat) {
        try {
            PremiumNutzer premiumNutzer =
                    anwenderService
                            .getPremiumNutzerByUsername(user.getUniqueString())
                            .orElseThrow();
            Optional<Playlist> playlist =
                    premiumNutzerService.insertPlaylist(name, privat, premiumNutzer);
            return playlist.map(
                            value ->
                                    ResponseEntity.status(HttpStatus.OK)
                                            .body(
                                                    "Playlist mit ID "
                                                            + value.getPlaylistid()
                                                            + " wurde hinzugefügt."))
                    .orElseGet(ControllerHelper::somethingWentWrongResponse);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @DeleteMapping("playlists/{playlistid}")
    public ResponseEntity<?> deletePlaylist(
            @CurrentUser User user, @PathVariable(value = "playlistid") int playlistid) {
        try {
            PremiumNutzer premiumNutzer =
                    anwenderService
                            .getPremiumNutzerByUsername(user.getUniqueString())
                            .orElseThrow();
            Optional<Playlist> playlist =
                    premiumNutzerService.deletePlaylist(playlistid, premiumNutzer);
            return playlist.map(
                            value ->
                                    ResponseEntity.status(HttpStatus.OK)
                                            .body(
                                                    "Playlist mit ID "
                                                            + value.getPlaylistid()
                                                            + " wurde gelöscht."))
                    .orElseGet(ControllerHelper::somethingWentWrongResponse);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @PostMapping("playlists/{playlistid}/aufnahmen")
    public ResponseEntity<?> insertAufnahme(
            @CurrentUser User user,
            @PathVariable(value = "playlistid") int playlistid,
            @RequestParam(value = "aufnahmeid") int aufnahmeid) {
        try {
            PremiumNutzer premiumNutzer =
                    anwenderService
                            .getPremiumNutzerByUsername(user.getUniqueString())
                            .orElseThrow();
            Optional<Aufnahme> aufnahme =
                    premiumNutzerService.insertAufnahmeInPlaylist(
                            playlistid, aufnahmeid, premiumNutzer);
            return aufnahme.map(
                            value ->
                                    ResponseEntity.status(HttpStatus.OK)
                                            .body(
                                                    "Aufnahme mit ID "
                                                            + value.getAufnahmeid()
                                                            + " wurde hinzugefügt."))
                    .orElseGet(ControllerHelper::somethingWentWrongResponse);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @DeleteMapping("playlists/{playlistid}/aufnahmen/{aufnahmeid}")
    public ResponseEntity<?> deleteAufnahme(
            @CurrentUser User user,
            @PathVariable(value = "playlistid") int playlistid,
            @PathVariable(value = "aufnahmeid") int aufnahmeid) {
        try {
            PremiumNutzer premiumNutzer =
                    anwenderService
                            .getPremiumNutzerByUsername(user.getUniqueString())
                            .orElseThrow();
            Optional<Aufnahme> aufnahme =
                    premiumNutzerService.deleteAufnahmeFromPlaylist(
                            playlistid, aufnahmeid, premiumNutzer);
            return aufnahme.map(
                            value ->
                                    ResponseEntity.status(HttpStatus.OK)
                                            .body(
                                                    "Aufnahme mit ID "
                                                            + value.getAufnahmeid()
                                                            + " wurde gelöscht."))
                    .orElseGet(ControllerHelper::somethingWentWrongResponse);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }
}
