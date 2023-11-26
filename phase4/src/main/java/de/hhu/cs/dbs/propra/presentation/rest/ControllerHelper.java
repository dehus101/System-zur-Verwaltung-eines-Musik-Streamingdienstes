package de.hhu.cs.dbs.propra.presentation.rest;

import de.hhu.cs.dbs.propra.domain.errors.APIException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class ControllerHelper {

    public static ResponseEntity<?> errorResponse(APIException a) {
        return ResponseEntity.status(a.getStatus()).body(createJSONErrorMessage(a.getMessage()));
    }

    public static HashMap<String, String> createJSONErrorMessage(String message) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("message", message);
        return hashMap;
    }

    public static ResponseEntity somethingWentWrongResponse() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createJSONErrorMessage("Something went wrong"));
    }
}
