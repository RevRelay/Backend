package com.revature.RevRelay.services.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.RevRelay.controllers.PageController;
import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.repositories.PageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
public class PageControllerTest {
    @Autowired
    private PageController pageController;
    @Autowired
    private PageRepository pageRepository;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    // @Test
    // void createPageValid() throws Exception {
    // Page page = new Page();
    // page.setPrivate(true);
    // page.setGroupPage(true);
    // page.setUserOwnerID(1);
    // page.set
    // mockMvc = MockMvcBuilders.standaloneSetup(pageController).build();
    // mockMvc.perform(MockMvcRequestBuilders.post("/pages")
    // ).andExpect(status().isOk()).andExpect(jsonPath("$.totalPages").exists())
    // .andDo(print());
    // }

    @Test
    void getPageByValidUserOwnerId() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(pageController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/pages/users/{userOwnerID}", 0)).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getPageByValidPageByGroupOwnerID() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(pageController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/pages/groups/{groupID}", 0)).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getPageByValidPageIDThatDoesNotExist() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(pageController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/pages/{groupID}", 0)).andExpect(status().isNotFound())
                .andDo(print());
    }
}