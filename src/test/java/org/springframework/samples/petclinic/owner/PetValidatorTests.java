package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for {@link PetValidator}
 *
 */
@ExtendWith(MockitoExtension.class)
@DisabledInNativeImage
public class PetValidatorTests {

	private PetValidator petValidator;

	private Pet pet;

	private PetType petType;

	private Errors errors;

	private static final String petName = "Buddy";

	private static final String petTypeName = "Dog";

	private static final LocalDate petBirthDate = LocalDate.of(1990, 1, 1);

	@BeforeEach
	void setUp() {
		petValidator = new PetValidator();
		pet = new Pet();
		petType = new PetType();
		errors = new MapBindingResult(new HashMap<>(), "pet");
	}

	@Test
	void testValidate() {
		petType.setName(petTypeName);
		pet.setName(petName);
		pet.setType(petType);
		pet.setBirthDate(petBirthDate);

		petValidator.validate(pet, errors);

		assertFalse(errors.hasErrors());
	}

	@Nested
	class ValidateHasErrors {

		@Test
		void testValidateWithInvalidPetName() {
			petType.setName(petTypeName);
			pet.setName("");
			pet.setType(petType);
			pet.setBirthDate(petBirthDate);

			petValidator.validate(pet, errors);

			assertTrue(errors.hasFieldErrors("name"));
		}

		@Test
		void testValidateWithInvalidPetType() {
			pet.setName(petName);
			pet.setType(null);
			pet.setBirthDate(petBirthDate);

			petValidator.validate(pet, errors);

			assertTrue(errors.hasFieldErrors("type"));
		}

		@Test
		void testValidateWithInvalidBirthDate() {
			petType.setName(petTypeName);
			pet.setName(petName);
			pet.setType(petType);
			pet.setBirthDate(null);

			petValidator.validate(pet, errors);

			assertTrue(errors.hasFieldErrors("birthDate"));
		}
	}

	@Test
	void testSupports() {
		assertTrue(petValidator.supports(Pet.class));
		assertFalse(petValidator.supports(Object.class));
	}
}
