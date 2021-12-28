package com.revature.RevRelay.controller;

import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.service.PageService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@NoArgsConstructor
@RestController
@RequestMapping(value = "/pages")
public class PageController {
    PageService pageService;

    @Autowired
    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    //CREATE
    @PostMapping
    public ResponseEntity<Page> createPage(@RequestBody Page page) {
        return ResponseEntity.ok(pageService.createPage(page));
    }

    //READ
    @GetMapping("/all")
    public List<Page> getAll() {
        return pageService.getAll();
    }

    @GetMapping("/users/{userOwnerID}")
    public Page getPageByUserOwnerID(@PathVariable Integer userOwnerID) {
        return pageService.getPageByUserOwnerID(userOwnerID);
    }

    @GetMapping("/groups/{groupOwnerID}")
    public Page getPageByGroupOwnerID(@PathVariable Integer groupOwnerID) {
        return pageService.getPageByGroupOwnerID(groupOwnerID);
    }

    @GetMapping("/{pageID}")
    public Page getPageByPageID(@PathVariable Integer pageID) {
        return pageService.getPageByPageID(pageID);
    }

    //UPDATE
    @PutMapping
    public Page updatePage(@RequestBody Page page) {
        return pageService.updatePage(page);
    }

    //DELETE
    @DeleteMapping("/{pageID}")
    public void deletePageByID(@PathVariable Integer pageID) {
        pageService.deletePageByID(pageID);
    }
}
