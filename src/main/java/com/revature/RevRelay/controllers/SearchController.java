package com.revature.RevRelay.controllers;

import com.revature.RevRelay.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Provides endpoints for queries to the SearchService.
 *
 * @author Nathan Luther
 */
@RestController
@RequestMapping(value = "/search")
public class SearchController {

    private final SearchService searchService;

    /**
     * Constructor for the SearchController.
     * @param searchService The service layer for Search.
     */
    @Autowired
    public SearchController(SearchService searchService) {
        super();
        this.searchService = searchService;
    }

    /**
     * Uses a PathVariable to submit a search to SearchService.
     * Note that SearchAllByName will always return a List, despite using pageables
     * behind the scenes when given the appropriate request parameters.
     *
     * @param searchTerm String to be compared to DisplayNames and GroupNames.
     * @param pageStart Starting page for when getting paginated results. Optional.
     * @param pageSize Page size for when getting paginated results. Optional.
     * @return An array containing the full set of matching SearchResultItems.
     */
    @GetMapping(value = "/name/{searchTerm}")
    public ResponseEntity<?> searchUsersAndGroupsByName(@PathVariable String searchTerm, @RequestParam Optional<Integer> pageStart, @RequestParam Optional<Integer> pageSize) {
        if (pageStart.isPresent() && pageSize.isPresent()) {
            return ResponseEntity.ok(searchService.SearchAllByName(searchTerm, PageRequest.of(pageStart.get(),pageSize.get())));
        } else {
            return ResponseEntity.ok(searchService.SearchAllByName(searchTerm));
        }
    }
}