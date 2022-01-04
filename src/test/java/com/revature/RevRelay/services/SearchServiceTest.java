package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.models.dtos.SearchResultItem;
import com.revature.RevRelay.repositories.GroupRepository;
import com.revature.RevRelay.repositories.PageRepository;
import com.revature.RevRelay.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SearchServiceTest {

    UserRepository mockUserRepository;
    GroupRepository mockGroupRepository;
    PageRepository mockPageRepository;
    SearchService searchService;
    Pageable mockPageable;
    String mockSearchTerm;
    User mockUser;
    Group mockGroup;
    List<User> mockUserList;
    List<Group> mockGroupList;
    Page<User> mockUserPage;
    Page<Group> mockGroupPage;
    com.revature.RevRelay.models.Page mockPage;

    @BeforeEach
    public void setup() {
        mockUserRepository = Mockito.mock(UserRepository.class);
        mockGroupRepository = Mockito.mock(GroupRepository.class);
        mockPageRepository = Mockito.mock(PageRepository.class);
        searchService = new SearchService(mockUserRepository, mockGroupRepository, mockPageRepository);
        mockSearchTerm = "";
        mockPageable = PageRequest.of(1, 20);
        mockUser = Mockito.mock(User.class);
        mockGroup = Mockito.mock(Group.class);
        mockPage = Mockito.mock(com.revature.RevRelay.models.Page.class);
    }

    @Test
    void constructorTest() {
        SearchService searchServiceTestConstructor = new SearchService(mockUserRepository, mockGroupRepository, mockPageRepository);
        try {
            Class<?> searchServiceClazz = Class.forName("com.revature.RevRelay.services.SearchService");
            Field fieldUserRepository = searchServiceClazz.getDeclaredField("userRepository");
            fieldUserRepository.setAccessible(true);
            Field fieldGroupRepository = searchServiceClazz.getDeclaredField("groupRepository");
            fieldGroupRepository.setAccessible(true);
            Field fieldPageRepository = searchServiceClazz.getDeclaredField("pageRepository");
            fieldPageRepository.setAccessible(true);
            assertTrue(fieldUserRepository.get(searchServiceTestConstructor).equals(mockUserRepository)
                    && fieldGroupRepository.get(searchServiceTestConstructor).equals(mockGroupRepository)
                    && fieldPageRepository.get(searchServiceTestConstructor).equals(mockPageRepository));
        } catch (ClassNotFoundException e) {
            fail("SearchService Class Not Found");
        } catch (NoSuchFieldException e) {
            fail("SearchService Field Not Found");
        } catch (IllegalAccessException e) {
            fail("Illegal Access Exception Thrown");
        }
    }

    @Test
    void searchWithNoResultsInUsersOrGroup() {
        when(mockUserRepository.findByDisplayNameContainingIgnoreCase(any())).thenReturn(Optional.empty());
        when(mockGroupRepository.findByGroupNameContainingIgnoreCase(any())).thenReturn(Optional.empty());
        assertEquals(0, searchService.SearchAllByName(mockSearchTerm).size());
    }

    @Test
    void searchWithNoResultsInUsersOrGroupPageable() {
        when(mockUserRepository.findByDisplayNameContainingIgnoreCase(any(), any())).thenReturn(Page.empty());
        when(mockGroupRepository.findByGroupNameContainingIgnoreCase(any(), any())).thenReturn(Page.empty());
        assertEquals(0, searchService.SearchAllByName(mockSearchTerm, mockPageable).size());
    }

    @Test
    void searchWithMockResultsInUsersOrGroup() {
        // Mock users
        User mockUser1 = Mockito.mock(User.class);
        when(mockUser1.getUserID()).thenReturn(1);
        when(mockUser1.getDisplayName()).thenReturn("user1");
        User mockUser2 = Mockito.mock(User.class);
        when(mockUser2.getDisplayName()).thenReturn("user2");
        when(mockUser2.getUserID()).thenReturn(2);
        User mockUser3 = Mockito.mock(User.class);
        when(mockUser3.getUserID()).thenReturn(3);
        when(mockUser3.getDisplayName()).thenReturn("user3");
        mockUserList = Arrays.asList(mockUser1, mockUser2, mockUser3);
        // Mock groups
        Group mockGroup1 = Mockito.mock(Group.class);
        when(mockGroup1.getGroupID()).thenReturn(1);
        when(mockGroup1.getGroupName()).thenReturn("group1");
        Group mockGroup2 = Mockito.mock(Group.class);
        when(mockGroup2.getGroupID()).thenReturn(2);
        when(mockGroup2.getGroupName()).thenReturn("group2");
        Group mockGroup3 = Mockito.mock(Group.class);
        when(mockGroup3.getGroupID()).thenReturn(3);
        when(mockGroup3.getGroupName()).thenReturn("group3");
        mockGroupList = Arrays.asList(mockGroup1, mockGroup2, mockGroup3);
        // Mock user and group repository call returns
        when(mockUserRepository.findByDisplayNameContainingIgnoreCase(any())).thenReturn(Optional.of(mockUserList));
        when(mockGroupRepository.findByGroupNameContainingIgnoreCase(any())).thenReturn(Optional.of(mockGroupList));
        // Mock page repository PageId returns
        when(mockPageRepository.getPageByUserOwnerUserID(any())).thenReturn(mockPage);
        when(mockPageRepository.getPageByGroupOwnerGroupID(any())).thenReturn(mockPage);
        when(mockPage.getPageID()).thenReturn(0);
        // Test assertions.
        List<SearchResultItem> results = searchService.SearchAllByName(mockSearchTerm);
        // This assertion would be more robust with more terms.
        assertEquals(6, results.size());
    }
}
