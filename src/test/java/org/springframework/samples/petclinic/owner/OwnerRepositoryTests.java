package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void shouldFindPetTypes() {
        List<PetType> petTypes = this.owners.findPetTypes();
        assertThat(petTypes).isNotEmpty();
        PetType petType = petTypes.get(0);
        assertThat(petType.getName()).isNotEmpty();
    }

    @Test
    void shouldFindOwnersByLastName() {
        Page<Owner> owners = this.owners.findByLastNameStartingWith("Davis", PageRequest.of(0, 10));
        assertThat(owners).isNotEmpty();
        assertThat(owners.getContent().get(0).getLastName()).startsWith("Davis");
    }

    @Test
    void shouldFindSingleOwnerWithPet() {
        Owner owner = this.owners.findById(1).orElse(null);
        assertThat(owner).isNotNull();
        assertThat(owner.getLastName()).isEqualTo("Franklin");
        assertThat(owner.getPets()).isNotEmpty();
        assertThat(owner.getPets().get(0).getType()).isNotNull();
        assertThat(owner.getPets().get(0).getType().getName()).isNotEmpty();
    }

    @Test
    void shouldFindAllOwners() {
        Page<Owner> owners = this.owners.findAll(PageRequest.of(0, 10));
        assertThat(owners).isNotEmpty();
        assertThat(owners.getTotalElements()).isGreaterThan(0);
    }

    @Test
    void shouldThrowExceptionForNullId() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.owners.findById(null);
        });
    }

    @Test
    void shouldReturnEmptyOptionalForNonExistentId() {
        Optional<Owner> owner = this.owners.findById(999);
        assertThat(owner).isEmpty();
    }

    @Test
    void shouldFindNoOwnersWithUnknownLastName() {
        Page<Owner> owners = this.owners.findByLastNameStartingWith("Unknown", PageRequest.of(0, 10));
        assertThat(owners.getContent()).isEmpty();
    }

    @Test
    void shouldReturnEmptyPageWhenNoOwners() {
        Pageable pageable = PageRequest.of(100, 10); // Page beyond available data
        Page<Owner> owners = this.owners.findAll(pageable);
        assertThat(owners.getContent()).isEmpty();
    }
}
