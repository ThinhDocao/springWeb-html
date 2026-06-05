package vn.com.ocb.aipdmaservice.controller.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import vn.com.ocb.aipdmaservice.entity.CategoryEntity;
import vn.com.ocb.aipdmaservice.entity.ProductEntity;
import vn.com.ocb.aipdmaservice.repository.CategoryRepository;
import vn.com.ocb.aipdmaservice.repository.MaterialRepository;
import vn.com.ocb.aipdmaservice.repository.ProductRepository;
import vn.com.ocb.aipdmaservice.service.UploadService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminCatalogControllerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private UploadService uploadService;

    @InjectMocks
    private AdminCatalogController adminCatalogController;

    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        redirectAttributes = new RedirectAttributesModelMap();
    }

    @Test
    void deleteCategory_WhenCategoryNotFound_ShouldReturnErrorMessage() {
        Long categoryId = 99L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        String view = adminCatalogController.deleteCategory(categoryId, redirectAttributes);

        assertThat(view).isEqualTo("redirect:/admin/categories");
        assertThat(redirectAttributes.getFlashAttributes().get("errorMessage"))
                .isEqualTo("Không tìm thấy danh mục cần xóa.");
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteCategory_WhenCategoryHasSubcategories_ShouldReturnErrorMessage() {
        Long categoryId = 1L;
        CategoryEntity parentCat = CategoryEntity.builder().id(categoryId).name("Parent Cat").build();
        CategoryEntity childCat = CategoryEntity.builder().id(2L).name("Child Cat").parent(parentCat).build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(parentCat));
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(parentCat, childCat));

        String view = adminCatalogController.deleteCategory(categoryId, redirectAttributes);

        assertThat(view).isEqualTo("redirect:/admin/categories");
        assertThat(redirectAttributes.getFlashAttributes().get("errorMessage"))
                .isEqualTo("Không thể xóa danh mục này vì đang có danh mục con thuộc về nó. Vui lòng xóa các danh mục con trước.");
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteCategory_WhenCategoryHasProducts_ShouldReturnErrorMessage() {
        Long categoryId = 1L;
        CategoryEntity category = CategoryEntity.builder().id(categoryId).name("Tech").build();
        ProductEntity product = ProductEntity.builder().id(10L).name("Laptop").category(category).build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        String view = adminCatalogController.deleteCategory(categoryId, redirectAttributes);

        assertThat(view).isEqualTo("redirect:/admin/categories");
        assertThat(redirectAttributes.getFlashAttributes().get("errorMessage"))
                .isEqualTo("Không thể xóa danh mục này vì đang có sản phẩm thuộc danh mục này. Vui lòng di chuyển hoặc xóa các sản phẩm trước.");
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteCategory_WhenSuccess_ShouldDeleteAndReturnSuccessMessage() {
        Long categoryId = 1L;
        CategoryEntity category = CategoryEntity.builder().id(categoryId).name("Clean Cat").build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        String view = adminCatalogController.deleteCategory(categoryId, redirectAttributes);

        assertThat(view).isEqualTo("redirect:/admin/categories");
        assertThat(redirectAttributes.getFlashAttributes().get("successMessage"))
                .isEqualTo("Đã xóa danh mục.");
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    @SuppressWarnings("unchecked")
    void products_WhenFilteredByCategory_ShouldIncludeProductsOfSubcategories() {
        Long parentId = 1L;
        Long childId = 2L;

        CategoryEntity parentCat = CategoryEntity.builder().id(parentId).name("Parent").build();
        CategoryEntity childCat = CategoryEntity.builder().id(childId).name("Child").parent(parentCat).build();

        ProductEntity parentProd = ProductEntity.builder().id(10L).name("Parent Product").category(parentCat).build();
        ProductEntity childProd = ProductEntity.builder().id(11L).name("Child Product").category(childCat).build();
        ProductEntity otherProd = ProductEntity.builder().id(12L).name("Other Product").category(null).build();

        org.springframework.ui.Model model = new org.springframework.ui.ExtendedModelMap();

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(parentCat, childCat));
        when(productRepository.findAllByOrderByUpdatedAtDescCreatedAtDescIdDesc())
                .thenReturn(Arrays.asList(parentProd, childProd, otherProd));
        when(materialRepository.findAllByOrderByNameAsc()).thenReturn(Collections.emptyList());

        String view = adminCatalogController.products(null, parentId, null, null, null, model);

        assertThat(view).isEqualTo("admin/products/list");
        java.util.List<ProductEntity> resultProducts = (java.util.List<ProductEntity>) model.getAttribute("products");
        assertThat(resultProducts).containsExactlyInAnyOrder(parentProd, childProd);
    }
}
