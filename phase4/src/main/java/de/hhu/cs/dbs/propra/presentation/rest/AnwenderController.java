package de.hhu.cs.dbs.propra.presentation.rest;

import static de.hhu.cs.dbs.propra.presentation.rest.ControllerHelper.errorResponse;

import com.fasterxml.jackson.annotation.JsonView;

import de.hhu.cs.dbs.propra.domain.errors.APIException;
import de.hhu.cs.dbs.propra.domain.model.*;
import de.hhu.cs.dbs.propra.domain.services.AnwenderService;
import de.hhu.cs.dbs.propra.domain.views.Views;

import jakarta.annotation.security.PermitAll;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/")
@PermitAll
@RestController
@EnableWebSecurity()
public class AnwenderController {

    private final AnwenderService anwenderService;

    public AnwenderController(AnwenderService anwenderService) {
        this.anwenderService = anwenderService;
    }

    @GetMapping("nutzer")
    public ResponseEntity<?> getNutzer(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "iban", required = false) String iban) {
        try {
            // return this list as a json Object
            List<Nutzer> nutzer = anwenderService.getAllNutzerWithFilter(email, iban);
            return ResponseEntity.status(HttpStatus.OK).body(nutzer);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @PostMapping("nutzer")
    public ResponseEntity<?> insertNutzer(
            @RequestParam("email") String email,
            @RequestParam("benutzername") String benutzername,
            @RequestParam("passwort") String passwort,
            @RequestParam("iban") String iban) {
        try {
            Optional<Nutzer> nutzer =
                    anwenderService.insertNutzer(email, passwort, benutzername, iban);
            return nutzer.map(
                            value ->
                                    ResponseEntity.status(HttpStatus.CREATED)
                                            .header(
                                                    "Location",
                                                    "/nutzer/" + value.getNutzerid() + "/")
                                            .body(
                                                    "Nutzer mit ID "
                                                            + value.getNutzerid()
                                                            + " wurde erstellt."))
                    .orElseGet(ControllerHelper::somethingWentWrongResponse);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @GetMapping("premiumnutzer")
    public ResponseEntity<?> getPremiumnutzer(
            @RequestParam(value = "gueltigkeitstage", required = false) Integer gueltigkeitstage) {
        try {
            // return this list as a json object
            List<PremiumNutzer> premiumNutzer =
                    anwenderService.getAllPremiumnutzerWithFilter(gueltigkeitstage);
            return ResponseEntity.status(HttpStatus.OK).body(premiumNutzer);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @PostMapping("premiumnutzer")
    public ResponseEntity<?> insertPremiumnutzer(
            @RequestParam("email") String email,
            @RequestParam("benutzername") String benutzername,
            @RequestParam("passwort") String passwort,
            @RequestParam("iban") String iban,
            @RequestParam("gueltigkeit") String gueltigkeitstage) {
        try {
            Optional<PremiumNutzer> premiumNutzer =
                    anwenderService.insertPremiumnutzer(
                            email, passwort, benutzername, iban, gueltigkeitstage);
            return premiumNutzer
                    .map(
                            value ->
                                    ResponseEntity.status(HttpStatus.CREATED)
                                            .header(
                                                    "Location",
                                                    "/premiumnutzer/"
                                                            + value.getPremiumnutzerid()
                                                            + "/")
                                            .body(
                                                    "Premiumnutzer mit ID "
                                                            + value.getPremiumnutzerid()
                                                            + " wurde erstellt."))
                    .orElseGet(ControllerHelper::somethingWentWrongResponse);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @GetMapping("kuenstler")
    public ResponseEntity<?> getKuenstler(
            @RequestParam(value = "email", required = false) String email) {
        try {
            // return this list as a json object
            List<Kuenstler> nutzer = anwenderService.getAllKuenstlerWithFilter(email);
            return ResponseEntity.status(HttpStatus.OK).body(nutzer);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @PostMapping("kuenstler")
    public ResponseEntity<?> insertKuenstler(
            @RequestParam("email") String email,
            @RequestParam("benutzername") String benutzername,
            @RequestParam("passwort") String passwort,
            @RequestParam("iban") String iban,
            @RequestParam("gueltigkeit") String gueltigkeitstage,
            @RequestParam("aktiv_seit") String aktiv_seit,
            @RequestParam("pseudonym") String pseudonym) {
        try {
            Optional<Kuenstler> kuenstler =
                    anwenderService.insertKuenstler(
                            email,
                            passwort,
                            benutzername,
                            iban,
                            gueltigkeitstage,
                            aktiv_seit,
                            pseudonym);
            return kuenstler
                    .map(
                            value ->
                                    ResponseEntity.status(HttpStatus.CREATED)
                                            .header(
                                                    "Location",
                                                    "/kuenstler/" + value.getKuenstlerid() + "/")
                                            .body(
                                                    "Kuenstler mit ID "
                                                            + value.getKuenstlerid()
                                                            + " wurde erstellt."))
                    .orElseGet(ControllerHelper::somethingWentWrongResponse);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @GetMapping("songs")
    public ResponseEntity<?> getSongs() {
        try {
            // return this list as a json object
            List<Song> songs = anwenderService.getSongs();
            return ResponseEntity.status(HttpStatus.OK).body(songs);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @GetMapping("alben")
    public ResponseEntity<?> getAlben() {
        try {
            // return this list as a json object
            List<Album> alben = anwenderService.getAlben();
            return ResponseEntity.status(HttpStatus.OK).body(alben);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @GetMapping("bands")
    public ResponseEntity<?> getBands(@RequestParam(value = "name", required = false) String name) {
        try {
            // return this list as a json object
            List<Band> bands = anwenderService.getBands(name);
            return ResponseEntity.status(HttpStatus.OK).body(bands);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @GetMapping("playlists")
    public ResponseEntity<?> getPlaylists(
            @RequestParam(value = "name", required = false) String name) {
        try {
            // return this list as a json object
            List<Playlist> playlists = anwenderService.getPlaylists(name);
            return ResponseEntity.status(HttpStatus.OK).body(playlists);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @GetMapping("playlists/{playlistid}/aufnahmen")
    public ResponseEntity<?> getPlaylistAufnahmen(
            @PathVariable("playlistid") int playlistId,
            @RequestParam(value = "titel", required = false) String title) {
        try {
            // return this list as a json object
            List<Aufnahme> aufnahmen =
                    anwenderService.getAufnahmenByPlaylistsByID(title, playlistId);
            return ResponseEntity.status(HttpStatus.OK).body(aufnahmen);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @GetMapping("alben/{albumid}/kritiken")
    @JsonView(Views.IdOnly.class)
    public ResponseEntity<?> getAlbumKritiken(@PathVariable("albumid") int albumId) {
        try {
            // return this list as a json object
            List<Kritik> kritiken = anwenderService.getKritikenByAlbumID(albumId);
            return ResponseEntity.status(HttpStatus.OK).body(kritiken);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }
}
