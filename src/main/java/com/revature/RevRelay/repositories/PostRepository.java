package com.revature.RevRelay.repositories;

import com.revature.RevRelay.enums.PostType;
import com.revature.RevRelay.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

    Page<Post> findAllByPostTypeAndPostPagePageID(PostType postType, Integer pageID, Pageable pageable);

    Page<Post> findByPostOwnerID(Integer postOwnerID,Pageable pageable);
}
