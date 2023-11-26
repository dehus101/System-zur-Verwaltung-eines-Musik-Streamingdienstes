package de.hhu.cs.dbs.propra.presentation.rest;

import static de.hhu.cs.dbs.propra.presentation.rest.ControllerHelper.errorResponse;

import de.hhu.cs.dbs.propra.domain.errors.APIException;
import de.hhu.cs.dbs.propra.domain.model.Kritik;
import de.hhu.cs.dbs.propra.domain.model.Nutzer;
import de.hhu.cs.dbs.propra.domain.model.User;
import de.hhu.cs.dbs.propra.domain.services.NutzerService;
import de.hhu.cs.dbs.propra.security.CurrentUser;

import jakarta.annotation.security.RolesAllowed;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RequestMapping("/")
@RestController
@RolesAllowed("NUTZER")
public class NutzerController {

    private final NutzerService nutzerService;

    public NutzerController(NutzerService nutzerService) {
        this.nutzerService = nutzerService;
    }

    @PostMapping("alben/{albumid}/kritiken")
    public ResponseEntity<?> insertKritik(
            @CurrentUser User user,
            @PathVariable(value = "albumid") Integer albumid,
            @RequestParam(value = "text") String text) {
        try {
            Nutzer nutzer =
                    nutzerService.getNutzerByBenutzername(user.getUniqueString()).orElseThrow();
            Optional<Kritik> kritik = nutzerService.insertKritik(albumid, text, nutzer);
            return kritik.map(
                            value ->
                                    ResponseEntity.status(HttpStatus.OK)
                                            .body(
                                                    "Kritik mit ID "
                                                            + value.getKritikid()
                                                            + " wurde erstellt."))
                    .orElseGet(ControllerHelper::somethingWentWrongResponse);
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @DeleteMapping("kritiken/{kritikid}")
    public ResponseEntity<?> deleteKritik(
            @CurrentUser User user, @PathVariable(value = "kritikid") int kritikid) {
        try {
            Nutzer nutzer =
                    nutzerService.getNutzerByBenutzername(user.getUniqueString()).orElseThrow();
            ArrayList<Kritik> kritiken = nutzerService.getKritikenByNutzer(nutzer);
            if (kritiken.stream().noneMatch(kritik -> kritik.getKritikid() == kritikid)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Kritik mit ID " + kritikid + " gehört nicht dem Nutzer.");
            }
            nutzerService.deleteKritik(kritikid, nutzer);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Kritik mit ID " + kritikid + " wurde gelöscht.");
        } catch (APIException a) {
            return errorResponse(a);
        }
    }

    @PatchMapping("alben/{albumid}/kritiken/{kritikid}")
    public ResponseEntity<?> updateKritik(
            @CurrentUser User user,
            @PathVariable(value = "kritikid") Integer kritikid,
            @PathVariable(value = "albumid") Integer albumid,
            @RequestParam(value = "text") String text) {
        try {
            Nutzer nutzer =
                    nutzerService.getNutzerByBenutzername(user.getUniqueString()).orElseThrow();
            ArrayList<Kritik> kritiken = nutzerService.getKritikenByNutzer(nutzer);
            if (kritiken.stream().noneMatch(kritik -> kritik.getKritikid() == kritikid)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Kritik mit ID " + kritikid + " gehört nicht dem Nutzer.");
            }
            nutzerService.updateKritik(kritikid, albumid, text, nutzer);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Kritik mit ID " + kritikid + " wurde geändert.");
        } catch (APIException a) {
            return errorResponse(a);
        }
    }
}
