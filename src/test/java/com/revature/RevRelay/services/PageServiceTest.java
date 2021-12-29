package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.repositories.PageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.revature.RevRelay.models.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PageServiceTest {
	@Autowired
	PageRepository pageRepository;
	@Autowired
	PageService pageService;

	@Test
	public void createPageTest(){
		System.out.println(pageRepository);
		Page page = new Page();
		page.setDescription("TEST");
		Page Page1 = pageService.createPage(page);
		assertEquals(page,Page1);
		assertEquals(Page1.getPageID(), pageService.getPageByPageID(Page1.getPageID()).getPageID());
	}
	@Test
	public void NoArgsTest(){
		PageService gr = new PageService();
		assertNotNull(gr);
	}

	@Test
	public void getAllTest(){
		pageRepository.deleteAll();
		List<Page> Pages = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Page g = new Page();
			g.setDescription(i+"");
			g.setUserOwnerID(i);
			g.setPrivate(false);
			Pages.add(g);
			pageService.createPage(g);
		}
		List<Page> p1 = pageService.getAll();
		for (int i = 0; i < 100; i++) {
			assertEquals(Pages.get(i).getPageID(),p1.get(i).getPageID());
		}
	}
	@Test
	public void updatePagesTestTest(){
		pageRepository.deleteAll();
		System.out.println(pageRepository);
		Page Page = new Page();
		Page.setDescription("TEST");
		Page Page1 = pageService.createPage(Page);
		Page1.setDescription("Test2");
		assertEquals(Page1.getDescription(), pageService.updatePage(Page1).getDescription());

	}
	@Test
	public void deletePagesByIDTest(){
		pageRepository.deleteAll();
		Page Page = new Page();
		Page.setPageID(10000);
		Page.setDescription("TEST");
		Page Page1 = pageService.createPage(Page);
		pageService.deletePageByID(Page1.getPageID());
		Page g = pageService.getPageByPageID(10000);
		assertNull(g);
	}
	@Test
	public void getPageByUserOwnerIDTest() {
		pageRepository.deleteAll();
		Page page = new Page();
		page.setUserOwnerID(1);
		Page Page1 = pageService.createPage(page);
		assertEquals(page,Page1);
		assertEquals(Page1.getUserOwnerID(), pageService.getPageByUserOwnerID(Page1.getUserOwnerID()).getUserOwnerID());
	}
	@Test
	public void getPageByGroupIDTest() {
		pageRepository.deleteAll();
		Page page = new Page();
		page.setGroupID(1);
		Page Page1 = pageService.createPage(page);
		assertEquals(page,Page1);
		assertEquals(Page1.getGroupID(), pageService.getPageByGroupID(Page1.getGroupID()).getGroupID());
	}

}
