package webprogramming.csc1106.Repositories;

import org.springframework.data.repository.CrudRepository;

import webprogramming.csc1106.Entities.CourseForum;

public interface CourseForumRepo extends CrudRepository<CourseForum, Integer>{ 
    CourseForum findByForumID(int forumID);
} 