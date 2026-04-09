package edu.ban7.chatbotmsnmsii2527.unit;

import edu.ban7.chatbotmsnmsii2527.model.AppUser;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class AppUserUnitTest {

    static Validator validator;

    @BeforeAll
    public static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void validUserWithoutEmail_shouldNotBeValid() {
        AppUser user = new AppUser();
        user.setId(1);
        user.setEmail("");
        user.setPassword("root");
        user.setPseudo("toto");

        Set<ConstraintViolation<AppUser>> constraintViolations = validator.validate(user);

        boolean violationExist = constraintViolations.stream()
                .anyMatch(c -> c.getPropertyPath().toString().equals("email")
                        && c.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("NotBlank"));

        Assertions.assertTrue(violationExist);
    }


    @Test
    public void validUserWithMalformedEmail_shouldNotBeValid() {
        AppUser user = new AppUser();
        user.setId(1);
        user.setEmail("a");
        user.setPassword("root");
        user.setPseudo("toto");

        Set<ConstraintViolation<AppUser>> constraintViolations = validator.validate(user);

        boolean violationExist = constraintViolations.stream()
                .anyMatch(c -> c.getPropertyPath().toString().equals("email")
                        && c.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().equals("Email"));

        Assertions.assertTrue(violationExist);
    }


}
