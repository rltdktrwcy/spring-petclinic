package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

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
public class PetTypeFormatterTests {

	@Mock
	private OwnerRepository owners;

	private PetTypeFormatter petTypeFormatter;

	@BeforeEach
	public void setup() {
		petTypeFormatter = new PetTypeFormatter(owners);
	}

	@Test
	public void shouldPrintPetType() {
		PetType petType = new PetType();
		petType.setName("Hamster");
		String petTypeName = petTypeFormatter.print(petType, Locale.ENGLISH);
		assertEquals("Hamster", petTypeName);
	}

	@Test
	public void shouldParsePetType() throws ParseException {
		Collection<PetType> petTypes = makePetTypes();
		given(owners.findPetTypes()).willReturn((List<PetType>) petTypes);
		PetType petType = petTypeFormatter.parse("Bird", Locale.ENGLISH);
		assertEquals("Bird", petType.getName());
	}

	@Test
	public void shouldThrowParseException() {
		Collection<PetType> petTypes = makePetTypes();
		given(owners.findPetTypes()).willReturn((List<PetType>) petTypes);
		assertThrows(ParseException.class, () -> petTypeFormatter.parse("Fish", Locale.ENGLISH));
	}

	private Collection<PetType> makePetTypes() {
		Collection<PetType> petTypes = new ArrayList<>();
		PetType dog = new PetType();
		dog.setName("Dog");
		PetType cat = new PetType();
		cat.setName("Cat");
		PetType bird = new PetType();
		bird.setName("Bird");
		petTypes.add(dog);
		petTypes.add(cat);
		petTypes.add(bird);
		return petTypes;
	}

}
