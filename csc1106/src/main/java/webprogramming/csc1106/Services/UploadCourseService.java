package webprogramming.csc1106.Services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;

import webprogramming.csc1106.Entities.CategoryGroup;
import webprogramming.csc1106.Entities.CourseCategory;
import webprogramming.csc1106.Entities.FileResource;
import webprogramming.csc1106.Entities.Lesson;
import webprogramming.csc1106.Entities.PartnerCertificate;
import webprogramming.csc1106.Entities.PartnerPublish;
import webprogramming.csc1106.Entities.Rating;
import webprogramming.csc1106.Entities.Section;
import webprogramming.csc1106.Entities.UploadCourse;
import webprogramming.csc1106.Entities.User;
import webprogramming.csc1106.Repositories.CategoryGroupRepository;
import webprogramming.csc1106.Repositories.CourseCategoryRepository;
import webprogramming.csc1106.Repositories.FileResourceRepository;
import webprogramming.csc1106.Repositories.LessonRepository;
import webprogramming.csc1106.Repositories.PartnerCertificateRepository;
import webprogramming.csc1106.Repositories.PartnerPublishRepository;
import webprogramming.csc1106.Repositories.RatingRepository;
import webprogramming.csc1106.Repositories.SectionRepository;
import webprogramming.csc1106.Repositories.UploadCourseRepository;
import webprogramming.csc1106.Repositories.UserRepository;

@Service
public class UploadCourseService {

    private static final Logger logger = LoggerFactory.getLogger(UploadCourseService.class);

    @Autowired
    private UploadCourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private FileResourceRepository fileResourceRepository;

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    @Autowired
    private PartnerCertificateRepository partnerCertificateRepository;

    @Autowired
    private PartnerPublishRepository partnerPublishRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private AzureBlobService azureBlobService;

    @Autowired
    private CourseSubscriptionService courseSubscriptionService;

    @Autowired
    private UserRepository userRepository; 

    // Course Management Methods
    // ===================================================================================================

    // Get all courses with ratings calculated
    public List<UploadCourse> getAllCourses() {
        return calculateRatings(courseRepository.findAll());
    }
    // Get all approved courses with ratings calculated
    public List<UploadCourse> getAllApprovedCourses() {
        return calculateRatings(courseRepository.findByIsApprovedTrue());
    }
    // Get course by ID with rating calculated
    public Optional<UploadCourse> getCourseById(Long id) {
        Optional<UploadCourse> course = courseRepository.findById(id);
        course.ifPresent(this::calculateRating);
        return course;
    }
    // Get course with details
    public UploadCourse getCourseWithDetails(Long courseId) {
        return courseRepository.findByIdWithDetails(courseId);
    }
    // Get total number of courses
    public long getTotalCourses() {
        return courseRepository.count();
    }
    // Add new course
    public UploadCourse addCourse(UploadCourse course) {
        initializeCourseFields(course);
        return courseRepository.save(course);
    }
    // Update existing course
    public UploadCourse updateCourse(UploadCourse course) {
        initializeCourseFields(course);
        return courseRepository.save(course);
    }
    // Delete course and associated resources
    public void deleteCourse(Long courseId) {
        UploadCourse course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        deleteCourseResources(course);
        courseRepository.deleteById(courseId);
    }
    // Process course upload with optional cover image and selected category
    public void processCourseUpload(UploadCourse course, MultipartFile coverImage, Long selectedCategory) throws IOException {
        if (coverImage != null && !coverImage.isEmpty()) {
            validateCoverImage(coverImage);
            String coverImageUrl = uploadToAzureBlob(coverImage.getInputStream(), coverImage.getOriginalFilename());
            coverImageUrl = generateSasUrl(coverImageUrl);
            course.setCoverImageUrl(coverImageUrl);
        }

        addCourse(course);

        CategoryGroup categoryGroup = getCategoryById(selectedCategory)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        CourseCategory courseCategory = new CourseCategory(course, categoryGroup);
        addCourseCategory(courseCategory);
        course.getCourseCategories().add(courseCategory);

        for (Section section : course.getSections()) {
            section.setCourse(course);
            addSection(section);

            for (Lesson lesson : section.getLessons()) {
                lesson.setSection(section);
                addLesson(lesson);

                MultipartFile lessonFile = lesson.getFile();
                if (lessonFile != null && !lessonFile.isEmpty()) {
                    String fileUrl = uploadToAzureBlob(lessonFile.getInputStream(), lessonFile.getOriginalFilename());
                    fileUrl = generateSasUrl(fileUrl);
                    FileResource fileResource = new FileResource(lessonFile.getOriginalFilename(), fileUrl);
                    fileResource.setLesson(lesson);
                    addFileResource(fileResource);
                    lesson.getFiles().add(fileResource);
                }
            }
        }
    }
    // Process course update with optional cover image and selected category
    public void processCourseUpdate(UploadCourse course, MultipartFile coverImage, Long selectedCategory) throws IOException {
        Optional<UploadCourse> existingCourseOpt = getCourseById(course.getId());
        if (!existingCourseOpt.isPresent()) {
            throw new RuntimeException("Course not found.");
        }

        UploadCourse existingCourse = existingCourseOpt.get();

        if (coverImage != null && !coverImage.isEmpty()) {
            validateCoverImage(coverImage);
            if (existingCourse.getCoverImageUrl() != null) {
                deleteBlob(existingCourse.getCoverImageUrl());
            }
            String coverImageUrl = uploadToAzureBlob(coverImage.getInputStream(), coverImage.getOriginalFilename());
            coverImageUrl = generateSasUrl(coverImageUrl);
            existingCourse.setCoverImageUrl(coverImageUrl);
        }

        // Update basic fields
        updateCourseFields(existingCourse, course);

        clearCourseCategories(existingCourse);
        CategoryGroup categoryGroup = getCategoryById(selectedCategory)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        CourseCategory courseCategory = new CourseCategory(existingCourse, categoryGroup);
        addCourseCategory(courseCategory);
        existingCourse.getCourseCategories().add(courseCategory);

        // Clear and update sections and lessons
        existingCourse.getSections().clear();
        for (Section section : course.getSections()) {
            section.setCourse(existingCourse);
            Section savedSection = addSection(section);

            for (Lesson lesson : section.getLessons()) {
                lesson.setSection(savedSection);
                Lesson savedLesson = addLesson(lesson);

                MultipartFile lessonFile = lesson.getFile();
                if (lessonFile != null && !lessonFile.isEmpty()) {
                    for (FileResource existingFile : lesson.getFiles()) {
                        deleteBlob(existingFile.getFileUrl());
                    }
                    lesson.getFiles().clear();
                    String fileUrl = uploadToAzureBlob(lessonFile.getInputStream(), lessonFile.getOriginalFilename());
                    fileUrl = generateSasUrl(fileUrl);
                    FileResource fileResource = new FileResource(lessonFile.getOriginalFilename(), fileUrl);
                    fileResource.setLesson(savedLesson);
                    addFileResource(fileResource);
                    savedLesson.getFiles().add(fileResource);
                }
            }
            existingCourse.getSections().add(savedSection);
        }

        updateCourse(existingCourse);
    }
    // End of Course Management Methods
    // ===================================================================================================


    // Section and Lesson Management Methods
    // ===================================================================================================

    // Get section by ID
    public Section getSectionById(Long sectionId) {
        return sectionRepository.findById(sectionId).orElse(null);
    }
    // Get lessons by section ID
    public List<Lesson> getLessonsBySectionId(Long sectionId) {
        return lessonRepository.findBySectionId(sectionId);
    }
    // Add new section
    public Section addSection(Section section) {
        return sectionRepository.save(section);
    }
    // Add new lesson
    public Lesson addLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }
    // Remove section by ID and associated lessons and files
    public void removeSection(Long sectionId, Long courseId) {
        Section section = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new RuntimeException("Section not found"));
    
        // Remove the section's lessons and their associated files
        for (Lesson lesson : section.getLessons()) {
            for (FileResource file : lesson.getFiles()) {
                azureBlobService.deleteBlob(file.getFileUrl());
            }
            lessonRepository.delete(lesson);
        }

        // Remove the section itself
        sectionRepository.delete(section);
    
        // Update the course by removing the section reference
        Optional<UploadCourse> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            UploadCourse course = courseOpt.get();
            course.getSections().removeIf(s -> s.getId().equals(sectionId));
            courseRepository.save(course);
        }
    }
    // Remove lesson by ID
    public void removeLesson(Long lessonId, Long courseId) {
        lessonRepository.deleteById(lessonId);
        Optional<UploadCourse> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            UploadCourse course = courseOpt.get();
            for (Section section : course.getSections()) {
                section.getLessons().removeIf(lesson -> lesson.getId().equals(lessonId));
            }
            courseRepository.save(course);
        }
    }
     // End of Section and Lesson Management Methods
    // ================================================================================================


    // File Resource Management Methods
    // ================================================================================================

    // Upload file to Azure Blob Storage
    public String uploadToAzureBlob(InputStream fileInputStream, String fileName) throws IOException {
        return azureBlobService.uploadToAzureBlob(fileInputStream, fileName);
    }
    // Generate SAS URL for Azure Blob Storage
    public String generateSasUrl(String blobUrl) {
        return azureBlobService.generateSasUrl(blobUrl);
    }
    // Download file with SAS URL
    public InputStream downloadFileWithSas(String blobUrl) {
        String sasUrl = azureBlobService.generateSasUrl(blobUrl);
        BlobClient blobClient = new BlobClientBuilder()
                .endpoint(sasUrl)
                .buildClient();
        return blobClient.openInputStream();
    }
    // Get file resource by ID
    public Optional<FileResource> getFileResourceById(Long id) {
        return fileResourceRepository.findById(id);
    }
    // Add new file resource
    public FileResource addFileResource(FileResource fileResource) {
        return fileResourceRepository.save(fileResource);
    }

    // Add new course category
    public CourseCategory addCourseCategory(CourseCategory courseCategory) {
        return courseCategoryRepository.save(courseCategory);
    }

    // Get all category groups
    public List<CategoryGroup> getAllCategories() {
        return categoryGroupRepository.findAll();
    }

    // Get category group by ID
    public Optional<CategoryGroup> getCategoryById(Long id) {
        logger.info("Fetching category by id: {}", id);
        return categoryGroupRepository.findById(id);
    }

    // Clear course categories
    public void clearCourseCategories(UploadCourse course) {
        List<CourseCategory> courseCategories = courseCategoryRepository.findByCourse(course);
        courseCategoryRepository.deleteAll(courseCategories);
        course.getCourseCategories().clear();
    }
     // End of File Resource Management Methods
    // ===================================================================================================


     // Rating Management Methods
    // ===================================================================================================
    
      // Add or update review for a course
      public void addReview(Long courseId, Integer userId, double rating, String comment) {
          Optional<UploadCourse> courseOpt = courseRepository.findById(courseId);
          Optional<User> userOpt = userRepository.findById(userId);
          if (courseOpt.isPresent() && userOpt.isPresent()) {
              UploadCourse course = courseOpt.get();
              User user = userOpt.get();
              Rating review = ratingRepository.findByCourseAndUser(course, user)
                  .orElse(new Rating(course, user, rating, comment, LocalDateTime.now()));
              review.setScore(rating);
              review.setComment(comment);
              review.setTimestamp(LocalDateTime.now());
              ratingRepository.save(review);
              calculateRating(course);
              courseRepository.save(course);
          } else {
              throw new RuntimeException("Course or User not found");
          }
      }
      
    // Calculate ratings for a list of courses
    private List<UploadCourse> calculateRatings(List<UploadCourse> courses) {
        courses.forEach(this::calculateRating);
        return courses;
    }
    // Calculate rating for a course
    public void calculateRating(UploadCourse course) {
        List<Rating> ratings = ratingRepository.findByCourse(course);
        if (ratings != null && !ratings.isEmpty()) {
            double averageRating = ratings.stream().mapToDouble(Rating::getScore).average().orElse(0.0);
            int reviewCount = ratings.size();
            course.setAverageRating(averageRating);
            course.setReviewCount(reviewCount);
        } else {
            course.setAverageRating(0.0);
            course.setReviewCount(0);
        }
    }
    // End of Rating Management Methods
    // ==========================================================================================================

    // Course Filtering and Sorting Methods
    // ====================================================================================================

     // Get courses by category ID with ratings calculated
     public List<UploadCourse> getCoursesByCategoryId(Long categoryId) {
        List<CourseCategory> courseCategories = courseCategoryRepository.findByCategoryGroupId(categoryId);
        List<UploadCourse> courses = courseCategories.stream()
            .map(CourseCategory::getCourse)
            .collect(Collectors.toList());
        return calculateRatings(courses);
    }
    // Get filtered and sorted courses by category ID
    public List<UploadCourse> getFilteredAndSortedCourses(Long categoryId, String sortBy) {
        List<UploadCourse> courses = courseRepository.findByCourseCategories_CategoryGroup_Id(categoryId);
        calculateRatings(courses);

        if (sortBy != null) {
            switch (sortBy) {
                case "price-low-high":
                    courses.sort(Comparator.comparingDouble(UploadCourse::getPrice));
                    break;
                case "price-high-low":
                    courses.sort(Comparator.comparingDouble(UploadCourse::getPrice).reversed());
                    break;
                case "rating-low-high":
                    courses.sort(Comparator.comparingDouble(UploadCourse::getAverageRating));
                    break;
                case "rating-high-low":
                    courses.sort(Comparator.comparingDouble(UploadCourse::getAverageRating).reversed());
                    break;
                case "reviews-low-high":
                    courses.sort(Comparator.comparingInt(UploadCourse::getReviewCount));
                    break;
                case "reviews-high-low":
                    courses.sort(Comparator.comparingInt(UploadCourse::getReviewCount).reversed());
                    break;
                case "newest":
                    courses.sort(Comparator.comparing(UploadCourse::getCreatedDate).reversed());
                    break;
                default:
                    courses.sort(Comparator.comparing(UploadCourse::getCreatedDate).reversed());
                    break;
            }
        }
        
        return courses;
    }
    // End of Course Filtering and Sorting Methods
    // =================================================================================================


    // Utility Methods
    // =================================================================================================

    // Delete resources associated with a course
    private void deleteCourseResources(UploadCourse course) {
        for (Section section : course.getSections()) {
            for (Lesson lesson : section.getLessons()) {
                for (FileResource file : lesson.getFiles()) {
                    azureBlobService.deleteBlob(file.getFileUrl());
                }
            }
        }
        if (course.getCoverImageUrl() != null) {
            azureBlobService.deleteBlob(course.getCoverImageUrl());
        }
    }
    // Initialize course fields
    private void initializeCourseFields(UploadCourse course) {
        if (course.getSections() == null) {
            course.setSections(new ArrayList<>());
        }
        if (course.getCourseCategories() == null) {
            course.setCourseCategories(new ArrayList<>());
        }
        course.setCreatedDate(LocalDateTime.now());
    }
    // Update basic fields of a course
    private void updateCourseFields(UploadCourse existingCourse, UploadCourse newCourseData) {
        existingCourse.setTitle(newCourseData.getTitle());
        existingCourse.setDescription(newCourseData.getDescription());
        existingCourse.setLecturer(newCourseData.getLecturer());
        existingCourse.setPrice(newCourseData.getPrice());
    }

    // Validate cover image file type
    private void validateCoverImage(MultipartFile coverImage) {
        String contentType = coverImage.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Invalid file type for cover image. Only images are allowed.");
        }
    }
     // Delete blob from Azure Blob Storage
     public void deleteBlob(String blobUrl) {
        azureBlobService.deleteBlob(blobUrl);
    }
    //End of Utility Methods
    // =================================================================================================


     // Partner Management Methods
    // =================================================================================================

    // Add new partner certificate
    public PartnerCertificate addPartnerCertificate(PartnerCertificate certificate) {
        return partnerCertificateRepository.save(certificate);
    }
    // Add new partner publish
    public PartnerPublish addPartnerPublish(PartnerPublish publish) {
        return partnerPublishRepository.save(publish);
    }
    // Get courses by partner ID
    public List<UploadCourse> getCoursesByPartnerId(Integer partnerId) {
        List<PartnerPublish> publishes = partnerPublishRepository.findByPartnerPartnerId(partnerId);
        return publishes.stream()
                .map(PartnerPublish::getCourse)
                .collect(Collectors.toList());
    }
    // Get partner publishes by partner ID
    public List<PartnerPublish> getPartnerPublishByPartnerId(Integer partnerId) {
        return partnerPublishRepository.findByPartnerPartnerId(partnerId);
    }
    // Add course with file
    public UploadCourse addCourseWithFile(UploadCourse course, InputStream fileInputStream, String fileName) throws IOException {
        String blobUrl = uploadToAzureBlob(fileInputStream, fileName);
        blobUrl = generateSasUrl(blobUrl);
        FileResource fileResource = new FileResource(fileName, blobUrl);
        fileResource.setLesson(null);  // Ensure this does not break your business logic
        fileResourceRepository.save(fileResource);
        return addCourse(course);
    }
    // Process partner course upload with optional cover image and selected category
    public UploadCourse partnerprocessCourseUpload(UploadCourse course, MultipartFile coverImage, Long selectedCategory) throws IOException {
        if (coverImage != null && !coverImage.isEmpty()) {
            validateCoverImage(coverImage);
            String coverImageUrl = uploadToAzureBlob(coverImage.getInputStream(), coverImage.getOriginalFilename());
            coverImageUrl = generateSasUrl(coverImageUrl);
            course.setCoverImageUrl(coverImageUrl);
        }

        UploadCourse savedCourse = addCourse(course);

        CategoryGroup categoryGroup = getCategoryById(selectedCategory)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        CourseCategory courseCategory = new CourseCategory(course, categoryGroup);
        addCourseCategory(courseCategory);
        course.getCourseCategories().add(courseCategory);

        for (Section section : course.getSections()) {
            section.setCourse(course);
            addSection(section);

            for (Lesson lesson : section.getLessons()) {
                lesson.setSection(section);
                addLesson(lesson);

                MultipartFile lessonFile = lesson.getFile();
                if (lessonFile != null && !lessonFile.isEmpty()) {
                    String fileUrl = uploadToAzureBlob(lessonFile.getInputStream(), lessonFile.getOriginalFilename());
                    fileUrl = generateSasUrl(fileUrl);
                    FileResource fileResource = new FileResource(lessonFile.getOriginalFilename(), fileUrl);
                    fileResource.setLesson(lesson);
                    addFileResource(fileResource);
                    lesson.getFiles().add(fileResource);
                }
            }
        }
        return savedCourse;
    }

    public String getFileContent(Long fileId) throws IOException {
        FileResource fileResource = getFileResourceById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        if (fileResource.getFileName().toLowerCase().endsWith(".txt")) {
            InputStream inputStream = downloadFileWithSas(fileResource.getFileUrl());
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        return null;
    }
    // Update course with optional cover image, category, and certificate details
    public void updateCourse(UploadCourse course, MultipartFile coverImage, Long categoryId, String certificateBlobUrl, String certificateTitle) throws IOException {
        Optional<UploadCourse> existingCourseOpt = getCourseById(course.getId());
        if (!existingCourseOpt.isPresent()) {
            throw new RuntimeException("Course not found.");
        }

        UploadCourse existingCourse = existingCourseOpt.get();

        // Update cover image if a new one is provided
        if (coverImage != null && !coverImage.isEmpty()) {
            validateCoverImage(coverImage);
            if (existingCourse.getCoverImageUrl() != null) {
                deleteBlob(existingCourse.getCoverImageUrl());
            }
            String coverImageUrl = uploadToAzureBlob(coverImage.getInputStream(), coverImage.getOriginalFilename());
            coverImageUrl = generateSasUrl(coverImageUrl);
            existingCourse.setCoverImageUrl(coverImageUrl);
        }
        // Update basic fields
        updateCourseFields(existingCourse, course);

        clearCourseCategories(existingCourse);
        CategoryGroup categoryGroup = getCategoryById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        CourseCategory courseCategory = new CourseCategory(existingCourse, categoryGroup);
        addCourseCategory(courseCategory);
        existingCourse.getCourseCategories().add(courseCategory);

        // Update the certificate details
        if (certificateBlobUrl != null && certificateTitle != null) {
            PartnerPublish partnerpublish = partnerPublishRepository.findByCourse(existingCourse);
            PartnerCertificate certificate = partnerCertificateRepository.findByPartnerPublish(partnerpublish);
            certificate.setCertificateName(certificateTitle);
            certificate.setBlobUrl(certificateBlobUrl); // Set Blob URL
            certificate.setIssueDate(LocalDateTime.now());

            // Save the updated certificate details
            addPartnerCertificate(certificate);

            // Update the PartnerPublish entity with the new certificate details
            PartnerPublish publish = partnerPublishRepository.findByCourse(existingCourse);
            publish.setCertificate(certificate);

            // Save the updated PartnerPublish entity
            addPartnerPublish(publish);
        }
        updateCourse(existingCourse);
    }
    // Get list of pending courses
    public List<UploadCourse> getPendingCourses() {
        return courseRepository.findByIsApprovedFalse();
    }
    // Get list of approved courses
    public List<UploadCourse> getApprovedCourses() {
        return courseRepository.findByIsApprovedTrue();
    }
    // Get approved courses by category ID
    public List<UploadCourse> getApprovedCoursesByCategoryId(Long categoryId) {
        return courseRepository.findByCourseCategories_CategoryGroup_IdAndIsApprovedTrue(categoryId);
    }
    // Get approved courses by user ID
    public List<UploadCourse> getApprovedCoursesByUserId(Integer userId) {
        return courseRepository.findByUser_UserIdAndIsApprovedTrue(userId);
    }
    // Get course categories by course IDs
    public List<CourseCategory> getCourseCategoriesByCourseIds(List<Long> courseIds) {
        return courseCategoryRepository.findByCourseIdIn(courseIds);
    }
    // Check if user is subscribed to a course
    public boolean isUserSubscribed(Long courseId, Integer userId) {
        return courseSubscriptionService.getSubscriptionsByUserId(userId)
            .stream()
            .anyMatch(subscription -> Long.valueOf(subscription.getCourseId()).equals(courseId));
    }
     // End of Partner Management Methods
    // ====================================================================================================
}
