# Plan Thiết Kế Admin Panel V1

## Summary

Xây dựng Admin Panel riêng dưới `/admin` cho project Spring Boot + Thymeleaf hiện tại, tập trung vào 5 nhóm chính: Dashboard, Catalog, Blog, Leads, Settings. V1 bỏ qua thanh toán online và phân quyền nhiều vai trò, nhưng có login admin đơn giản để tránh public toàn bộ trang quản trị.

Admin dùng layout Sidebar + Topbar + Main Content, style clean/minimal theo brand hiện có: nền `#FAFAF5`, trắng, bronze `#8C6239`, font Inter. Rich text editor dùng CKEditor CDN. Ảnh upload vào thư mục runtime `/uploads`, DB chỉ lưu URL.

## Key Changes

- Thêm admin routes:
  `/admin/login`, `/admin`, `/admin/products`, `/admin/categories`, `/admin/materials`, `/admin/blog`, `/admin/leads`, `/admin/settings`.
- Bảo vệ `/admin/**` bằng form login session, một tài khoản admin cấu hình qua `application.yml`/env; không làm role/permission.
- Thêm upload service:
  lưu file vào `uploads/`, expose public qua `/uploads/**`, giới hạn `jpg/jpeg/png/webp`, max mặc định 5MB.
- Bổ sung data phục vụ admin:
  thêm repository còn thiếu cho `ContactInquiryEntity`, `SiteSettingEntity`;
  thêm SEO fields `metaTitle`, `metaDescription` cho product/category/blog post;
  thêm lead status cho contact inquiry: `NEW`, `CONSULTING`, `CLOSED`; order hiện có map `PENDING` thành `NEW`.
- Public site đọc settings cơ bản từ `site_setting`: logo, favicon, hotline, email, address, social links, footer text, script embeds.

## Implementation Tasks

- Admin foundation:
  tạo admin layout fragments, `admin.css`, `admin.js`, sidebar/topbar responsive, active menu, table/form components, empty/loading/error states.
- Dashboard:
  hiển thị quick stats: lead mới, tổng sản phẩm, tổng bài viết, tổng danh mục; danh sách 5-10 lead mới nhất. Google Analytics chỉ để setting script embed trong V1, chưa tích hợp API analytics.
- Catalog:
  CRUD danh mục, chất liệu, sản phẩm.
  Product form gồm thông tin chung, giá hoặc "Liên hệ" bằng giá `0`, category/material, gallery upload nhiều ảnh, chọn ảnh primary, reorder bằng drag-drop cập nhật `sortOrder`, mô tả ngắn, mô tả chi tiết, thông số, ý nghĩa phong thủy, flags `bestSeller/new/premium/active`, slug và SEO.
- Blog:
  CRUD blog category và blog post.
  Blog post dùng CKEditor cho `content`, upload thumbnail, trạng thái published, author, publish date, slug, meta title/description.
- Leads:
  tạo màn hình unified lead list từ `customer_order` và `contact_inquiry`.
  Detail lead hiển thị thông tin khách, nguồn lead, sản phẩm quan tâm nếu có, nội dung ghi chú/message, trạng thái xử lý `NEW/CONSULTING/CLOSED`.
  Kết nối form liên hệ hiện tại để lưu vào `contact_inquiry` thay vì chỉ `alert`.
- Settings:
  quản lý logo/favicon upload, tên website, hotline, email, địa chỉ, giờ làm việc, Facebook/Zalo/YouTube, footer text, Google Analytics/Facebook Pixel/Tawk.to/Zalo script.

## Test Plan

- Chạy `mvn test` và đảm bảo context Spring Boot load được sau khi thêm security/admin services.
- Test service chính: tạo/sửa sản phẩm kèm gallery, primary image, reorder ảnh; tạo/sửa blog post CKEditor content; cập nhật lead status; đọc/ghi site settings.
- Smoke test bằng browser:
  login admin, CRUD product/category/blog, upload ảnh, kiểm tra ảnh hiển thị ở public product/blog page, gửi form liên hệ, xử lý lead trong admin.
- Kiểm tra security:
  truy cập `/admin` khi chưa login phải redirect login; public routes `/`, `/san-pham`, `/tin-tuc`, `/api/order`, `/uploads/**` vẫn hoạt động.

## Assumptions

- V1 chưa làm CMS đầy đủ cho Homepage/About như hero banner, trust cards, brand story; phần này để phase sau.
- Không làm phân quyền nhiều vai trò, chỉ một admin login.
- Dùng CKEditor qua CDN; nếu CDN lỗi thì textarea vẫn còn nội dung form để submit.
- Dùng `spring.jpa.hibernate.ddl-auto=update` hiện có để cập nhật schema trong môi trường dev.
