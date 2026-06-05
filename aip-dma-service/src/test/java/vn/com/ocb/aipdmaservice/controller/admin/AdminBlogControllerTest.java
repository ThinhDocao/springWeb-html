package vn.com.ocb.aipdmaservice.controller.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import vn.com.ocb.aipdmaservice.entity.BlogCategoryEntity;
import vn.com.ocb.aipdmaservice.entity.BlogPostEntity;
import vn.com.ocb.aipdmaservice.repository.BlogCategoryRepository;
import vn.com.ocb.aipdmaservice.repository.BlogPostRepository;
import vn.com.ocb.aipdmaservice.service.UploadService;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminBlogControllerTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @Mock
    private BlogCategoryRepository blogCategoryRepository;

    @Mock
    private UploadService uploadService;

    @InjectMocks
    private AdminBlogController adminBlogController;

    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        redirectAttributes = new RedirectAttributesModelMap();
    }

    @Test
    void deleteCategory_WhenCategoryHasPosts_ShouldReturnErrorMessage() {
        Long categoryId = 1L;
        BlogCategoryEntity category = BlogCategoryEntity.builder().id(categoryId).name("Tech").build();
        BlogPostEntity post = BlogPostEntity.builder().id(10L).title("Intro to Java").category(category).build();

        when(blogPostRepository.findAll()).thenReturn(Collections.singletonList(post));

        String view = adminBlogController.deleteCategory(categoryId, redirectAttributes);

        assertThat(view).isEqualTo("redirect:/admin/blog/categories");
        assertThat(redirectAttributes.getFlashAttributes().get("errorMessage"))
                .isEqualTo("Không thể xóa chuyên mục này vì đang có bài viết thuộc về nó. Vui lòng chuyển hoặc xóa các bài viết trước.");
        verify(blogCategoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteCategory_WhenSuccess_ShouldDeleteAndReturnSuccessMessage() {
        Long categoryId = 1L;
        when(blogPostRepository.findAll()).thenReturn(Collections.emptyList());

        String view = adminBlogController.deleteCategory(categoryId, redirectAttributes);

        assertThat(view).isEqualTo("redirect:/admin/blog/categories");
        assertThat(redirectAttributes.getFlashAttributes().get("successMessage"))
                .isEqualTo("Đã xóa chuyên mục.");
        verify(blogCategoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void deletePost_WhenSuccess_ShouldDeleteAndReturnSuccessMessage() {
        Long postId = 10L;

        String view = adminCatalogController_deletePost(postId);

        assertThat(view).isEqualTo("redirect:/admin/blog");
        assertThat(redirectAttributes.getFlashAttributes().get("successMessage"))
                .isEqualTo("Đã xóa bài viết.");
        verify(blogPostRepository, times(1)).deleteById(postId);
    }

    private String adminCatalogController_deletePost(Long postId) {
        return adminBlogController.deletePost(postId, redirectAttributes);
    }
}
