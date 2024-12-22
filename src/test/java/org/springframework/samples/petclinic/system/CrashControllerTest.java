package org.springframework.samples.petclinic.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CrashController.class)
class CrashControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testTriggerException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/oups"))
                .andExpect(status().isInternalServerError());
    }
}
