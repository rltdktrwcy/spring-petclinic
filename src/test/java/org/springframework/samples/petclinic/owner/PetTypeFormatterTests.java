package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PetTypeFormatterTests {

	@Mock
	private OwnerRepository owners;

	private PetTypeFormatter petTypeFormatter;

	@BeforeEach
	void setup() {
		petTypeFormatter = new PetTypeFormatter(owners);
	}

	@Test
	void shouldPrintPetType() {
		PetType petType = new PetType();
		petType.setName("Hamster");
		String petTypeName = petTypeFormatter.print(petType, Locale.ENGLISH);
		assertThat(petTypeName).isEqualTo("Hamster");
	}

	@Test
	void shouldParsePetType() throws ParseException {
		Collection<PetType> petTypes = new ArrayList<>();
		PetType petType = new PetType();
		petType.setName("Hamster");
		petTypes.add(petType);
		given(this.owners.findPetTypes()).willReturn(new ArrayList<>(petTypes));

		PetType parsedType = petTypeFormatter.parse("Hamster", Locale.ENGLISH);
		assertThat(parsedType.getName()).isEqualTo("Hamster");
	}

	@Test
	void shouldThrowParseException() {
		given(this.owners.findPetTypes()).willReturn(List.of());
		assertThrows(ParseException.class, () -> petTypeFormatter.parse("Fish", Locale.ENGLISH));
	}

}
