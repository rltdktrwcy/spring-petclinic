package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class PetValidatorTests {

	private PetValidator validator;
	private Pet pet;
	private Errors errors;

	@BeforeEach
	void setUp() {
		validator = new PetValidator();
		pet = new Pet();
		errors = new BeanPropertyBindingResult(pet, "pet");
	}

	@Test
	void shouldValidateWhenAllFieldsCorrect() {
		pet.setName("Max");
		pet.setType(new PetType());
		pet.setBirthDate(LocalDate.now());

		validator.validate(pet, errors);

		assertThat(errors.hasErrors()).isFalse();
	}

	@Test
	void shouldNotValidateWhenNameEmpty() {
		pet.setName("");
		pet.setType(new PetType());
		pet.setBirthDate(LocalDate.now());

		validator.validate(pet, errors);

		assertThat(errors.getFieldError("name").getCode()).isEqualTo("required");
	}

	@Test
	void shouldNotValidateWhenNameNull() {
		pet.setName(null);
		pet.setType(new PetType());
		pet.setBirthDate(LocalDate.now());

		validator.validate(pet, errors);

		assertThat(errors.getFieldError("name").getCode()).isEqualTo("required");
	}

	@Test
	void shouldNotValidateWhenTypeNullForNewPet() {
		pet.setName("Max");
		pet.setType(null);
		pet.setBirthDate(LocalDate.now());

		validator.validate(pet, errors);

		assertThat(errors.getFieldError("type").getCode()).isEqualTo("required");
	}

	@Test
	void shouldValidateWhenTypeNullForExistingPet() {
		pet.setId(1);
		pet.setName("Max");
		pet.setType(null);
		pet.setBirthDate(LocalDate.now());

		validator.validate(pet, errors);

		assertThat(errors.hasFieldErrors("type")).isFalse();
	}

	@Test
	void shouldNotValidateWhenBirthDateNull() {
		pet.setName("Max");
		pet.setType(new PetType());
		pet.setBirthDate(null);

		validator.validate(pet, errors);

		assertThat(errors.getFieldError("birthDate").getCode()).isEqualTo("required");
	}

	@Test
	void supportsClassPet() {
		assertThat(validator.supports(Pet.class)).isTrue();
	}

	@Test
	void doesNotSupportOtherClasses() {
		assertThat(validator.supports(Object.class)).isFalse();
	}

}
