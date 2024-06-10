package webprogramming.csc1106.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import webprogramming.csc1106.Entities.CategoryGroup;
import webprogramming.csc1106.Entities.UploadCourse;
import webprogramming.csc1106.Services.CategoryGroupService;
import webprogramming.csc1106.Services.UploadCourseService;

import java.util.logging.Logger;

@Controller
public class MarketplaceCategoryGroupController {

    @Autowired
    private CategoryGroupService categoryGroupService;

    @Autowired
    private UploadCourseService uploadCourseService;

    private static final Logger logger = Logger.getLogger(MarketplaceCategoryGroupController.class.getName());

    @GetMapping("/add-category")
    public String showAddCategoryPage(Model model) {
        model.addAttribute("categoryGroup", new CategoryGroup());
        return "Marketplace/add-category";
    }

    @PostMapping("/add-category")
    public String addCategory(CategoryGroup categoryGroup, Model model, RedirectAttributes redirectAttributes) {
        try {
            
            categoryGroupService.addCategoryGroup(categoryGroup);
            redirectAttributes.addFlashAttribute("successMessage", "Category group added successfully.");
            return "redirect:/market";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to add category group. Please try again.");
            logger.severe("Error adding category group: " + e.getMessage());
            return "Marketplace/add-category";
        }
    }

    @PostMapping("/delete-category/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryGroupService.deleteCategoryGroup(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category group deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete category group. Please try again.");
            logger.severe("Error deleting category group: " + e.getMessage());
        }
        return "redirect:/market";
    }
    
}

