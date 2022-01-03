package com.revature.RevRelay.services;


import com.revature.RevRelay.enums.PostType;
import com.revature.RevRelay.models.Post;
import com.revature.RevRelay.repositories.PostRepository;
import javafx.geometry.Pos;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PostServiceTest {
	@Autowired
	PostRepository repo;

	PostService service;

	@BeforeEach
	public void setup(){
		service = new PostService(repo);
		repo.deleteAll();
	}
	@Test
	public void  createPostTest(){
		Post post = new Post(0, PostType.ORIGINAL,"TEST","CONTENT",-1, DateUtil.now(),0,null,null,null);
		service.createPost(post);
		assertEquals("TEST",repo.getById(post.getPostID()).getPostTitle());
	}
	@Test
	public void  findPostByIDTest() throws Exception {
		Post post = new Post(0, PostType.ORIGINAL,"TEST","CONTENT",-1, DateUtil.now(),0,null,null,null);
		repo.save(post);
		assertEquals("TEST",service.findPostByPostID(post.getPostID()).getPostTitle());
	}

}
