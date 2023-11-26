package de.hhu.cs.dbs.propra.presentation.rest;

import de.hhu.cs.dbs.propra.domain.model.User;
import de.hhu.cs.dbs.propra.security.CurrentUser;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequestMapping("/")
@PermitAll
@RestController
public class ExampleController {

    private final JdbcTemplate jdbcTemplate;

    public ExampleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    // GET http://localhost:8080
    public String halloWelt() {
        return "\"Hallo Welt!\"";
    }

    @GetMapping("/exception")
    // GET http://localhost:8080
    public String halloException() {
        throw new RuntimeException("Hallo Exception!");
    }

    @GetMapping("foo")
    @RolesAllowed("USER")
    // GET http://localhost:8080/foo => OK. Siehe SQLiteUserRepository.
    public String halloFoo(@CurrentUser User user) {
        return "\"Hallo " + user.getUniqueString() + "\"!";
    }

    @GetMapping("foo2")
    @RolesAllowed("EMPLOYEE")
    // GET http://localhost:8080/foo2 => FORBIDDEN. Siehe SQLiteUserRepository.
    public String halloFoo2(@CurrentUser User user) {
        return "\"Hallo " + user.getUniqueString() + "\"!";
    }

    @GetMapping("foo3/{foo}")
    // GET http://localhost:8080/foo3/bar
    public String halloFoo3(@PathVariable("foo") String foo) {
        if (!foo.equals("bar")) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return "\"Hallo " + foo + "\"!";
    }

    @GetMapping("foo4")
    // GET http://localhost:8080/foo4?bar=xyz
    public String halloFoo4(@RequestParam("bar") String bar) {
        return "\"Hallo " + bar + "\"!";
    }

    @GetMapping("bar")
    // GET http://localhost:8080/bar => BAD REQUEST; http://localhost/bar?foo=xyz => OK
    public ResponseEntity<?> halloBar(@RequestParam(name = "foo", required = false) String foo) {
        if (foo == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "foo must not be foo.");
        return ResponseEntity.status(HttpStatus.OK).body("Hallo " + foo + "!");
    }

    @GetMapping("bar2")
    // GET http://localhost:8080/bar2
    public String halloBar2(
            @RequestParam(name = "name", defaultValue = "Max Mustermann") List<String> names) {
        int random = ThreadLocalRandom.current().nextInt(0, names.size());
        return jdbcTemplate.queryForObject("SELECT ?", String.class, names.get(random));
    }

    @PostMapping("foo")
    // POST http://localhost:8080/foo
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file, UriComponentsBuilder uriComponentsBuilder) {
        try {
            String length = String.valueOf(file.getBytes().length);
            return ResponseEntity.created(uriComponentsBuilder.path("/{id}").build(length)).build();
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, null, exception);
        }
    }
}
