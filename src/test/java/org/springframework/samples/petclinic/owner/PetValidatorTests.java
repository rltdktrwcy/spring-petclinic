package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class PetValidatorTests {

    private PetValidator petValidator;
    private Pet pet;
    private Errors errors;

    @BeforeEach
    void setUp() {
        petValidator = new PetValidator();
        pet = new Pet();
        errors = new BeanPropertyBindingResult(pet, "pet");
    }

    @Test
    void shouldValidateWhenAllFieldsAreCorrect() {
        pet.setName("Max");
        pet.setType(new PetType());
        pet.setBirthDate(LocalDate.now());

        petValidator.validate(pet, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    void shouldNotValidateWhenNameEmpty() {
        pet.setName("");
        pet.setType(new PetType());
        pet.setBirthDate(LocalDate.now());

        petValidator.validate(pet, errors);

        assertTrue(errors.hasFieldErrors("name"));
        assertEquals("required", errors.getFieldError("name").getCode());
    }

    @Test
    void shouldNotValidateWhenNameIsNull() {
        pet.setName(null);
        pet.setType(new PetType());
        pet.setBirthDate(LocalDate.now());

        petValidator.validate(pet, errors);

        assertTrue(errors.hasFieldErrors("name"));
        assertEquals("required", errors.getFieldError("name").getCode());
    }

    @Test
    void shouldNotValidateWhenTypeIsNullForNewPet() {
        pet.setName("Max");
        pet.setType(null);
        pet.setBirthDate(LocalDate.now());

        petValidator.validate(pet, errors);

        assertTrue(errors.hasFieldErrors("type"));
        assertEquals("required", errors.getFieldError("type").getCode());
    }

    @Test
    void shouldValidateWhenTypeIsNullForExistingPet() {
        pet.setId(1);
        pet.setName("Max");
        pet.setType(null);
        pet.setBirthDate(LocalDate.now());

        petValidator.validate(pet, errors);

        assertFalse(errors.hasFieldErrors("type"));
    }

    @Test
    void shouldNotValidateWhenBirthDateIsNull() {
        pet.setName("Max");
        pet.setType(new PetType());
        pet.setBirthDate(null);

        petValidator.validate(pet, errors);

        assertTrue(errors.hasFieldErrors("birthDate"));
        assertEquals("required", errors.getFieldError("birthDate").getCode());
    }

    @Test
    void supportsClassShouldReturnTrueForPetClass() {
        assertTrue(petValidator.supports(Pet.class));
    }

    @Test
    void supportsClassShouldReturnFalseForOtherClass() {
        assertFalse(petValidator.supports(Object.class));
    }

}
