package org.blog.apis.repositories;

import org.blog.apis.entities.Category;
import org.blog.apis.entities.Post;
import org.blog.apis.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);

}
