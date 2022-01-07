package com.revature.RevRelay.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.RevRelay.controllers.GroupsController;
import com.revature.RevRelay.controllers.UserController;
import com.revature.RevRelay.models.Chatroom;
import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.repositories.ChatroomRepository;
import com.revature.RevRelay.repositories.GroupRepository;
import com.revature.RevRelay.repositories.UserRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private final String testUsername = "fakeUser";
    private final String testPassword = "testPassword";
    private final String testEmail = "fakeEmail";
    private final String testDisplayName = "fakeDisplayName";
    private User user;

    public GroupControllerTest() {
    }

    @BeforeEach
    public void setup() {
        this.user = new User();
        user.setUsername(testUsername);
        user.setPassword(testPassword);
        user.setEmail(testEmail);
        user.setDisplayName(testDisplayName);
        userRepository.deleteAll();
        chatroomRepository.deleteAll();
    }

        @Test
//    @Ignore  // TODO fix test
    void createAGroupWithValidCredentials() throws Exception {
        Group group = new Group();
        User user1 = userRepository.save(user);

        group.setGroupName("hello");
        group.setUserOwner(user1);
        group.setPrivate(false);
        mockMvc = MockMvcBuilders.standaloneSetup(groupsController).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", " ")
                .content(mapper.writeValueAsString(group)))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    void getAllGroupTest() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(groupsController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/all")).andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").exists())
                .andDo(print());
    }

    // READ
    @Test
    void getAllGroupsByOwnerIdTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupsController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/all/{userOwnerID}", 1)).andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").exists())
                .andDo(print());
    }

    @Test
    void getAllGroupsByGroupIdTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupsController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/{groupsID}", 10)).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateGroupsTestWithValidData() throws Exception {
        Group group = new Group();
        User user1 = userRepository.save(user);
        group.setGroupName("hello");
        group.setUserOwner(user1);
        group.setPrivate(false);
        groupRepository.save(group);
        mockMvc = MockMvcBuilders.standaloneSetup(groupsController).build();
        mockMvc.perform(MockMvcRequestBuilders.put("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(group)))
                .andExpect((status().isOk())).andDo(print());
    }

    @Test
//    @Ignore // TODO fix test
    void deleteGroupIDTestWithValidData () throws Exception {
        Group group = new Group();
        User user1 = userRepository.save(user);

        group.setGroupName("hello");
        group.setUserOwner(user1);
        group.setPrivate(false);
        groupRepository.save(group);
        mockMvc = MockMvcBuilders.standaloneSetup(groupsController).build();
        mockMvc.perform(MockMvcRequestBuilders.delete("/groups/{groupID}", group.getGroupID()))
                .andExpect((status().isOk())).andDo(print());
    }

}
