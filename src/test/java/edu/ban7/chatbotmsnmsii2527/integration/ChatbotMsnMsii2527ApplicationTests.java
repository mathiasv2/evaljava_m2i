package edu.ban7.chatbotmsnmsii2527.integration;

import edu.ban7.chatbotmsnmsii2527.dao.QuestionDao;
import edu.ban7.chatbotmsnmsii2527.model.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
class ChatbotMsnMsii2527ApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private AppUser user;
    private AppUser admin;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void callHelloWorld_shouldBeOk() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void callListRecipeAsUser_shouldBeForbidden() throws Exception {
        mvc.perform(get("/recipe/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("a@a") // nécessaire pour alimenter @AuthenticationPrincipal
    void callListRecipeAsAdmin_shouldBeForbidden() throws Exception {
        mvc.perform(get("/recipe/list"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getUserwithId1_shouldBeTheCorrectUser() throws Exception {
        mvc.perform(get("/user/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getAllQuestions_asUser_shouldBeForbidden() throws Exception {

        mvc.perform(get("/get-all-questions"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getMyQuestions_asAdmin_shouldBeForbidden() throws Exception {

        mvc.perform(get("/get-my-questions"))
                .andExpect(status().isForbidden());
    }
}
