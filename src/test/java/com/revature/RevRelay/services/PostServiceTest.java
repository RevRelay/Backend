package com.revature.RevRelay.services;


import com.revature.RevRelay.enums.PostType;
import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.models.Post;
import com.revature.RevRelay.repositories.PageRepository;
import com.revature.RevRelay.repositories.PostRepository;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PostServiceTest {
	@Autowired
	PostRepository postRepo;
	@Autowired
	PageRepository pageRepo;

	PostService service;

	@BeforeEach
	public void setup(){
		postRepo.deleteAll();
		pageRepo.deleteAll();
		service = new PostService(postRepo);
	}
	@Test
	public void noArgsConstructorTest(){
		PostService post = new PostService();
		assertNotNull(post);
	}
	@Test
	public void  createPostTest(){
		Post post = new Post(0, PostType.ORIGINAL,"TEST","CONTENT",-1, DateUtil.now(),0,null,null,null);
		service.createPost(post);
		assertEquals("TEST", postRepo.getById(post.getPostID()).getPostTitle());
	}
	@Test
	public void  findPostByIDTest() throws Exception {
		Post post = new Post(0, PostType.ORIGINAL,"TEST","CONTENT",-1, DateUtil.now(),0,null,null,null);
		postRepo.save(post);
		assertEquals("TEST",service.findPostByPostID(post.getPostID()).getPostTitle());
	}
	@Test
	public void findPostByIDTestException(){
		boolean exceptionTest = false;
		try{
			service.findPostByPostID(1);
		}catch(Exception e){
			exceptionTest = true;
		}
		assertTrue(exceptionTest);
	}
	@Test
	public void findPostByPostPageIDTest(){
		Page page = new Page(1,"","",false,false,null,null,null);
		pageRepo.save(page);
		Post post = new Post(1, PostType.ORIGINAL,"TEST","CONTENT",-1, DateUtil.now(),0,page,null,null);
		postRepo.save(post);
		assertEquals("TEST",service.findPostByPostPageID(1).getContent().get(0).getPostTitle());
	}
	@Test
	public void findPostByPostPageIDTestPageable(){
		Page page = new Page(1,"","",false,false,null,null,null);
		Page p2 = pageRepo.save(page);
		Post post = new Post(1, PostType.ORIGINAL,"TEST","CONTENT",-1, DateUtil.now(),0,p2,null,null);
		postRepo.save(post);
		assertEquals("TEST",service.findPostByPostPageID(p2.getPageID(), Pageable.unpaged()).getContent().get(0).getPostTitle());
	}
	@Test
	public void findPostByPostOwnerIDTest(){
		Post post = new Post(1, PostType.ORIGINAL,"TEST","CONTENT",-1, DateUtil.now(),0,null,null,null);
		postRepo.save(post);
		assertEquals("TEST",service.findPostByPostOwnerID(0).getContent().get(0).getPostTitle());
	}
	@Test
	public void findPostByPostOwnerIDTestPageable(){
		Post post = new Post(1, PostType.ORIGINAL,"TEST","CONTENT",-1, DateUtil.now(),0,null,null,null);
		postRepo.save(post);
		assertEquals("TEST",service.findPostByPostOwnerID(0,Pageable.unpaged()).getContent().get(0).getPostTitle());
	}
	@Test
	public void updatePostTest() throws Exception {
		Post post = new Post(1, PostType.ORIGINAL,"TEST","CONTENT",-1, DateUtil.now(),0,null,null,null);
		Post post2 = service.createPost(post);

		post2.setPostContent("TEST2");
		Post post3 = service.updatePost(post2);
		assertNotNull(post3);
		assertEquals("TEST2",service.findPostByPostID(post2.getPostID()).getPostContent());
	}
	@Test
	public void deletePostByPostIDTest() throws Exception {
		Post post = new Post(1, PostType.ORIGINAL,"TEST","CONTENT",-1, DateUtil.now(),0,null,null,null);
		Post post2 = service.createPost(post);
		service.deletePostByPostID(post2.getPostID());

		try{
			post2 = service.findPostByPostID(post2.getPostID());
		}catch(Exception e){
			post2 = null;
		}
		assertNull(post2);
	}

}
