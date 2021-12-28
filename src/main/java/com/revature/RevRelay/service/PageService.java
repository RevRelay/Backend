package com.revature.RevRelay.service;

import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {
    PageRepository pageRepository;

    @Autowired
    public PageService(PageRepository pageRepository){
        this.pageRepository = pageRepository;
    }

    //CREATE
    public Page createPage(Page page) {
        return pageRepository.save(page);
    }
    //READ
    public List<Page> getAll() {
        return pageRepository.findAll();
    }
    public Page getPageByUserOwnerID(Integer userOwnerID) {
        return pageRepository.getPageByUserOwnerID(userOwnerID);
    }

    public Page getPageByGroupOwnerID(Integer groupOwnerID) {
        return pageRepository.getPageByGroupOwnerID(groupOwnerID);
    }

    public Page getPageByPageID(Integer pageID) {
        return pageRepository.getPageByPageID(pageID);
    }
    //UPDATE
    public Page updatePage(Page page) {
        return pageRepository.save(page);
    }
    //DELETE
    public void deletePageByID(Integer pageID) {
        pageRepository.deleteById(pageID);
    }
}
