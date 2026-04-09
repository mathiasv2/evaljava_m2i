package edu.ban7.chatbotmsnmsii2527.controller;

import edu.ban7.chatbotmsnmsii2527.dao.AppUserDao;
import edu.ban7.chatbotmsnmsii2527.model.AppUser;
import edu.ban7.chatbotmsnmsii2527.security.AppUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
@Tag(name = "Gestion des accès", description = "API de connexion et d'inscription utilisant des JWT")
public class AuthController {

    @Autowired
    protected AppUserDao appUserDao;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected AuthenticationProvider authenticationProvider;

    @PostMapping("/sign-in")
    @Operation(
            summary = "Inscription",
            description = "Envoie les information d'inscription en Json et évalue si l'email est déja utilisé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "L'utilisateur a été créé"),
            @ApiResponse(responseCode = "409", description = "L'email est déjà utilisé")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                    {
                                      "email": "john.doe@example.com",
                                      "password": "motDePasse123"
                                    }
                                    """)))
    public ResponseEntity<Void> inscription(@RequestBody AppUser user) {

        //on hash le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        appUserDao.save(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AppUser user) {

        try {
            AppUserDetails userDetails = (AppUserDetails) authenticationProvider
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    user.getPassword()))
                    .getPrincipal();

            String jwt = Jwts.builder()
                    .setSubject(user.getEmail())
                    .addClaims(Map.of("role", userDetails.getUser().isAdmin() ? "admin" : "user"))
                    .signWith(SignatureAlgorithm.HS256, "secret")
                    .compact();

            return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
