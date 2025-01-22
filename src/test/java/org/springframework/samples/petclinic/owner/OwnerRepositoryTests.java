package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
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

    private Owner george;

    @BeforeEach
    void setup() {
        george = new Owner();
        george.setFirstName("George");
        george.setLastName("Franklin");
        george.setAddress("110 W. Liberty St.");
        george.setCity("Madison");
        george.setTelephone("6085551023");
        owners.save(george);
    }

    @Test
    void testFindByLastNameStartingWith() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Owner> results = owners.findByLastNameStartingWith("F", pageable);
        assertThat(results).isNotEmpty();
        assertThat(results.getContent().get(0).getLastName()).isEqualTo("Franklin");
    }

    @Test
    void testFindByLastNameStartingWithNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Owner> results = owners.findByLastNameStartingWith("Z", pageable);
        assertThat(results).isEmpty();
    }

    @Test
    void testFindById() {
        Optional<Owner> owner = owners.findById(george.getId());
        assertThat(owner).isPresent();
        assertThat(owner.get().getFirstName()).isEqualTo("George");
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Owner> owner = owners.findById(999);
        assertThat(owner).isEmpty();
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
        assertThat(results.getTotalElements()).isGreaterThan(0);
    }

}
