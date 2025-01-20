package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        Owner owner = owners.getContent().get(0);
        assertThat(owner.getLastName()).isEqualTo("Davis");
        assertThat(owner.getFirstName()).isEqualTo("Betty");
    }

    @Test
    void shouldFindSingleOwnerWithPet() {
        Optional<Owner> owner = this.owners.findById(1);
        assertThat(owner.isPresent()).isTrue();
        assertThat(owner.get().getPets()).isNotEmpty();
        assertThat(owner.get().getPets().get(0).getType()).isNotNull();
    }

    @Test
    void shouldFindAllOwners() {
        Page<Owner> owners = this.owners.findAll(PageRequest.of(0, 10));
        assertThat(owners.getTotalElements()).isGreaterThan(0);
    }

    @Test
    void shouldThrowExceptionForNullId() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> this.owners.findById(null));
    }

    @Test
    void shouldReturnEmptyOptionalForNonExistentId() {
        Optional<Owner> owner = this.owners.findById(999);
        assertThat(owner.isPresent()).isFalse();
    }

    @Test
    void shouldReturnEmptyResultForNonExistentLastName() {
        Page<Owner> owners = this.owners.findByLastNameStartingWith("XYZ", PageRequest.of(0, 10));
        assertThat(owners.isEmpty()).isTrue();
    }

    @Test
    void shouldPaginateOwnerResults() {
        Page<Owner> firstPage = this.owners.findAll(PageRequest.of(0, 2));
        Page<Owner> secondPage = this.owners.findAll(PageRequest.of(1, 2));

        assertThat(firstPage.getContent()).hasSize(2);
        assertThat(secondPage.getContent()).isNotEmpty();
        assertThat(firstPage.getContent()).isNotEqualTo(secondPage.getContent());
    }

    @Test
    void shouldOrderPetTypes() {
        List<PetType> petTypes = this.owners.findPetTypes();
        for (int i = 0; i < petTypes.size() - 1; i++) {
            assertThat(petTypes.get(i).getName().compareTo(petTypes.get(i + 1).getName())).isLessThanOrEqualTo(0);
        }
    }
}
