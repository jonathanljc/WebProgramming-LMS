package webprogramming.csc1106.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import webprogramming.csc1106.Entities.CourseSubscriptionEntity;
import webprogramming.csc1106.Entities.User;

public interface CourseSubscriptionRepo extends JpaRepository<CourseSubscriptionEntity, String>{
    
    // GET by user
    List<CourseSubscriptionEntity> findByUserId(int userId);

    // GET all
    @Query("SELECT c FROM CourseSubscriptionEntity c")
    List<CourseSubscriptionEntity> findAll();
}
