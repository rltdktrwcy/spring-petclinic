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
        petType.setName("Dog");
        String petTypeName = petTypeFormatter.print(petType, Locale.ENGLISH);
        assertEquals("Dog", petTypeName);
    }

    @Test
    void shouldParsePetType() throws ParseException {
        Collection<PetType> petTypes = new ArrayList<>();
        PetType dog = new PetType();
        dog.setName("Dog");
        petTypes.add(dog);

        given(this.owners.findPetTypes()).willReturn((List<PetType>) petTypes);

        PetType petType = petTypeFormatter.parse("Dog", Locale.ENGLISH);
        assertEquals("Dog", petType.getName());
    }

    @Test
    void shouldThrowParseException() {
        Collection<PetType> petTypes = new ArrayList<>();
        given(this.owners.findPetTypes()).willReturn((List<PetType>) petTypes);

        assertThrows(ParseException.class, () -> petTypeFormatter.parse("Fish", Locale.ENGLISH));
    }

}
