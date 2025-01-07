/*
 * Copyright 2012-2019
 * the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file
 * except in compliance with the License.
 * You may obtain a copy of the
 * License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to
 * in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.samples.petclinic.owner.VisitController;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.anyInt;
// import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitController.class)
@DisabledInNativeImage
@DisabledInAotMode
@Import(VisitControllerTests.ExceptionHandlingControllerAdvice.class)
class VisitControllerTests {

    private static final int TEST_OWNER_ID = 1;
    private static final int TEST_PET_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerRepository owners;

    @ControllerAdvice
    static class ExceptionHandlingControllerAdvice {

        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public void handleNotFound() {
            // do nothing
        }
    }

    private Owner george() {
        Owner george = new Owner();
        george.setId(TEST_OWNER_ID);
        george.setFirstName("George");
        george.setLastName("Franklin");
        george.setAddress("110 W. Liberty St.");
        george.setCity("Madison");
        george.setTelephone("6085551023");

        Pet max = new Pet();
        max.setName("Max");
        PetType dog = new PetType();
        dog.setName("dog");
        max.setType(dog);
        max.setBirthDate(LocalDate.of(2015, 2, 20));

        george.addPet(max);
        max.setId(TEST_PET_ID); // Set ID after adding to ensure pet is added
        return george;
    }

    @BeforeEach
    void setup() {
        Owner george = george();
        given(this.owners.findById(eq(TEST_OWNER_ID))).willReturn(Optional.of(george));
    }

    @Test
    void testInitNewVisitForm() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("visit"))
            .andExpect(model().attributeExists("pet"))
            .andExpect(model().attributeExists("owner"))
            .andExpect(view().name("pets/createOrUpdateVisitForm"));
    }

    @Test
    void testProcessNewVisitFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
                .param("description", "Regular check-up"))
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attributeExists("message"))
            .andExpect(redirectedUrl("/owners/" + TEST_OWNER_ID));
    }

    @Test
    void testProcessNewVisitFormHasErrors() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("visit"))
            .andExpect(view().name("pets/createOrUpdateVisitForm"));
    }

    @Test
    void testInitNewVisitFormOwnerNotFound() throws Exception {
        given(this.owners.findById(TEST_OWNER_ID)).willReturn(Optional.empty());

        mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID))
            .andExpect(status().isNotFound());
    }

    @Test
    void testProcessNewVisitFormOwnerNotFound() throws Exception {
        given(this.owners.findById(TEST_OWNER_ID)).willReturn(Optional.empty());

        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
                .param("description", "Regular check-up"))
            .andExpect(status().isNotFound());
    }
}
