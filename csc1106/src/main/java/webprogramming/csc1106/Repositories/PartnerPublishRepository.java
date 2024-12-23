package webprogramming.csc1106.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import webprogramming.csc1106.Entities.Partner;
import webprogramming.csc1106.Entities.PartnerPublish;
import webprogramming.csc1106.Entities.UploadCourse;

public interface PartnerPublishRepository extends JpaRepository<PartnerPublish, Integer> {
     List<PartnerPublish> findByPartnerPartnerId(Integer partnerId);
     PartnerPublish findByCourse(UploadCourse course);
}
