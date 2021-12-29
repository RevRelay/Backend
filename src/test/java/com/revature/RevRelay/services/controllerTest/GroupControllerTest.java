package com.revature.RevRelay.services.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.RevRelay.controllers.GroupsController;
import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.repositories.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
 class GroupControllerTest {
    @Autowired
    private GroupsController groupsController;
    @Autowired
    private GroupRepository groupRepository;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void createAGroupWithValidCredentials() throws Exception {
        Group group = new Group();
        group.setGroupName("hello");
        group.setPrivate(false);
        group.setUserOwnerID(1);
        mockMvc = MockMvcBuilders.standaloneSetup(groupsController).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(group)))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    void getAllGroupTest() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(groupsController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/all")
                ).andExpect(status().isOk()).andExpect(jsonPath("$.totalPages").exists())
                .andDo(print());
    }

    //READ
    @Test
    void getAllGroupsByOwnerIdTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupsController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/all/{userOwnerID}", 1)
                ).andExpect(status().isOk()).andExpect(jsonPath("$.totalPages").exists())
                .andDo(print());
    }

    @Test
    void getAllGroupsByGroupIdTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupsController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/{groupsID}", 10)
                ).andExpect(status().isOk()).andExpect(jsonPath("$.totalPages").exists())
                .andDo(print());
    }
}