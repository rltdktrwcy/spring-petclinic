package org.springframework.samples.petclinic.owner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VisitController.class)
class VisitControllerTests {

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OwnerRepository owners;

	private Owner owner;

	@BeforeEach
	void setup() {
		owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		Pet pet = new Pet();
		pet.setId(TEST_PET_ID);
		pet.setName("Max");
		pet.setBirthDate(LocalDate.now());
		PetType petType = new PetType();
		petType.setName("dog");
		pet.setType(petType);
		owner.addPet(pet);
		when(owners.findById(TEST_OWNER_ID)).thenReturn(Optional.of(owner));
	}

	@Test
	void testInitNewVisitForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"))
			.andExpect(model().attributeExists("visit"))
			.andExpect(model().attributeExists("pet"))
			.andExpect(model().attributeExists("owner"));
	}

	@Test
	void testProcessNewVisitFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
			.param("description", "Regular checkup"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
		verify(owners).save(any(Owner.class));
	}

	@Test
	void testProcessNewVisitFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
			.param("description", ""))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("visit"))
			.andExpect(model().attributeHasFieldErrors("visit", "description"))
			.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

}
