package com.revature.RevRelay.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.RevRelay.controllers.PageController;
import com.revature.RevRelay.controllers.PostController;
import com.revature.RevRelay.enums.PostType;
import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.models.Post;
import com.revature.RevRelay.repositories.PageRepository;
import com.revature.RevRelay.repositories.PostRepository;
import com.revature.RevRelay.services.PageService;
import com.revature.RevRelay.services.PostService;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
public class PostControllerTest {

    @Autowired
    private PostController postController;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    PostService postService;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    PageService pageService;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void createPostTestWithValidData () throws Exception {
        Page page = new Page(0,"","",false,false,null,null,null);
        Post post = new Post();
        post.setPostType(PostType.ORIGINAL);
        post.setPostTitle("HELLO");
        post.setPostContent("CONTENTT");
        post.setPostTime(DateUtil.now());
        post.setPostOwnerID(1);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
                mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(post)))
                        .andExpect((status().isOk())).andDo(print());
    }

    @Test
    void findPostByPostIDTestWithValidData () throws Exception {
        Post post = new Post();
        post.setPostType(PostType.ORIGINAL);
        post.setPostTitle("HELLO");
        post.setPostContent("CONTENTT");
        post.setPostTime(DateUtil.now());
        post.setPostOwnerID(1);
        Post post1 = postRepository.save(post);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postID}", post1.getPostID())
                )
                        .andExpect((status().isOk())).andDo(print());

    }
    @Test
    void findPostByPostPageIDTestWithValidData () throws Exception {
        Post post = new Post();
        post.setPostType(PostType.ORIGINAL);
        post.setPostTitle("HELLOO");
        post.setPostContent("CONTENTTT");
        post.setPostTime(DateUtil.now());
        post.setPostOwnerID(1);
        Post post1 = postRepository.save(post);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/page/{postPageID}", 1)
                )
                .andExpect((status().isOk())).andDo(print());

    }

    @Test
    void findPostByPostOwnerIDTestWithValidData () throws Exception {
        Post post = new Post();
        post.setPostType(PostType.ORIGINAL);
        post.setPostTitle("HELLOO");
        post.setPostContent("CONTENTTT");
        post.setPostTime(DateUtil.now());
        post.setPostOwnerID(1);
        Post post1 = postRepository.save(post);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/user/{postOwnerID}", 1)
                )
                .andExpect((status().isOk())).andDo(print());
    }

    @Test
    void updatePostIDTestWithValidData () throws Exception {
        Post post = new Post();
        post.setPostType(PostType.ORIGINAL);
        post.setPostTitle("HELLOO");
        post.setPostContent("CONTENTTT");
        post.setPostTime(DateUtil.now());
        post.setPostOwnerID(1);
        Post post1 = postRepository.save(post);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
        mockMvc.perform(MockMvcRequestBuilders.put("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(post)))
                .andExpect((status().isOk())).andDo(print());
    }

    @Test
    void deletePostByPostIDTestWithValidData () throws Exception {
        Post post = new Post();
        post.setPostType(PostType.ORIGINAL);
        post.setPostTitle("HELLOO");
        post.setPostContent("CONTENTTT");
        post.setPostTime(DateUtil.now());
        post.setPostOwnerID(1);
        Post post1 = postRepository.save(post);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postID}", post.getPostID()))
                .andExpect((status().isOk())).andDo(print());
    }
}
