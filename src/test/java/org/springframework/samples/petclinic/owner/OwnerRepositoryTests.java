package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class OwnerRepositoryTests {

	@Autowired
	private OwnerRepository owners;

	@BeforeEach
	void setup() {
		Owner owner = new Owner();
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("123 Main St");
		owner.setCity("New York");
		owner.setTelephone("1234567890");
		owners.save(owner);
	}

	@Test
	void testFindByLastNameStartingWith() {
		Page<Owner> results = owners.findByLastNameStartingWith("Do", PageRequest.of(0, 10));
		assertThat(results).isNotEmpty();
		assertThat(results.getContent().get(0).getLastName()).startsWith("Do");
	}

	@Test
	void testFindById() {
		Owner owner = new Owner();
		owner.setFirstName("Jane");
		owner.setLastName("Smith");
		owner.setAddress("456 Oak St");
		owner.setCity("Boston");
		owner.setTelephone("9876543210");
		owners.save(owner);

		Optional<Owner> result = owners.findById(owner.getId());
		assertThat(result).isPresent();
		assertThat(result.get().getLastName()).isEqualTo("Smith");
	}

	@Test
	void testFindByIdWithInvalidId() {
		Optional<Owner> result = owners.findById(999);
		assertThat(result).isEmpty();
	}

	@Test
	void testFindByIdWithNullId() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> owners.findById(null));
	}

	@Test
	void testFindPetTypes() {
		List<PetType> petTypes = owners.findPetTypes();
		assertThat(petTypes).isNotNull();
	}

	@Test
	@Transactional
	void testFindAll() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Owner> results = owners.findAll(pageable);
		assertThat(results).isNotNull();
		assertThat(results.getContent()).isNotEmpty();
	}

	@Test
	void testSaveOwner() {
		Owner owner = new Owner();
		owner.setFirstName("Test");
		owner.setLastName("User");
		owner.setAddress("789 Test St");
		owner.setCity("TestCity");
		owner.setTelephone("5555555555");

		Owner savedOwner = owners.save(owner);
		assertThat(savedOwner.getId()).isNotNull();
		assertThat(savedOwner.getLastName()).isEqualTo("User");
	}

}
