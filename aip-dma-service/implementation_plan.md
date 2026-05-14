# Admin Panel - Bảng Điều Khiển Quản Trị

## Mục tiêu
Xây dựng trang Admin tích hợp vào dự án Spring Boot hiện tại, cho phép quản trị viên quản lý toàn bộ nội dung website (sản phẩm, bài viết, yêu cầu tư vấn, cài đặt hệ thống) mà không cần chỉnh sửa code.

**Phong cách**: Clean & Minimal, nền Cream `#FAFAF5`, CTA Bronze `#8C6239`, font Inter.
**Layout**: Sidebar (trái) + Topbar (trên) + Main Content (giữa).
**Routing**: Tất cả các trang admin sử dụng prefix `/admin/**`.

> [!IMPORTANT]
> **Không có phân quyền phức tạp**: Admin panel sẽ không yêu cầu đăng nhập (vì yêu cầu của user bỏ qua phân quyền). Có thể thêm basic auth sau nếu cần.

> [!IMPORTANT]
> **Không có Rich Text Editor phức tạp**: Giai đoạn đầu sử dụng `<textarea>` đơn giản cho các trường nội dung. Có thể tích hợp TinyMCE/CKEditor sau.

## User Review Required

> [!WARNING]
> Do khối lượng công việc rất lớn (~30+ file mới), tôi đề xuất **chia thành 4 Phase** thực thi tuần tự. Mỗi phase sẽ build & verify trước khi chuyển sang phase tiếp theo.

## Open Questions

1. **Upload ảnh**: Bạn muốn upload ảnh lên server local (`/uploads/`) hay chỉ nhập URL ảnh từ bên ngoài? *(Đề xuất: Giai đoạn 1 dùng URL, sau đó thêm upload file)*.
2. **Quản lý nội dung trang chủ (CMS)**: Bạn muốn triển khai ngay hay để lại cho phase sau? *(Đề xuất: Phase 3-4)*.

---

## Proposed Changes

### Phase 1: Foundation - Admin Layout, Dashboard & Quản lý Sản phẩm (CRUD)

Đây là phase quan trọng nhất, dựng nền tảng cho toàn bộ admin panel.

---

#### Backend - Admin Service Layer

##### [NEW] [AdminService.java](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/java/vn/com/ocb/aipdmaservice/service/AdminService.java)
Service chuyên biệt cho admin, tách biệt khỏi `AppService` (frontend):
- CRUD đầy đủ cho **Product** (list có phân trang, tạo, sửa, xóa).
- CRUD cho **Category** (list tree, tạo, sửa, xóa).
- CRUD cho **Material** (list, tạo, sửa, xóa).
- Dashboard stats: đếm tổng sản phẩm, bài viết, yêu cầu tư vấn mới.

##### [NEW] [AdminController.java](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/java/vn/com/ocb/aipdmaservice/controller/AdminController.java)
Controller xử lý tất cả route `/admin/**`:
- `GET /admin` → Dashboard
- `GET /admin/products` → Danh sách sản phẩm (bảng, filter, tìm kiếm)
- `GET /admin/products/new` → Form thêm sản phẩm
- `GET /admin/products/{id}/edit` → Form sửa sản phẩm
- `POST /admin/products/save` → Lưu sản phẩm (create/update)
- `POST /admin/products/{id}/delete` → Xóa sản phẩm
- `GET /admin/categories` → Danh sách danh mục
- `POST /admin/categories/save` → Lưu danh mục
- `POST /admin/categories/{id}/delete` → Xóa danh mục

##### [MODIFY] [ContactInquiryEntity.java](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/java/vn/com/ocb/aipdmaservice/entity/ContactInquiryEntity.java)
- Thêm trường `status` (String): `PENDING`, `IN_PROGRESS`, `CLOSED`.
- Thêm trường `sourceProductSlug` (String): lưu slug sản phẩm mà khách đang xem khi gửi yêu cầu.

##### [NEW] [ContactInquiryRepository.java](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/java/vn/com/ocb/aipdmaservice/repository/ContactInquiryRepository.java)
- `countByIsReadFalse()` — đếm yêu cầu chưa đọc.
- `findAllByOrderByCreatedAtDesc()` — list tất cả, mới nhất lên đầu.

##### [NEW] [SiteSettingRepository.java](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/java/vn/com/ocb/aipdmaservice/repository/SiteSettingRepository.java)
- `findBySettingKey(String key)` — lấy giá trị cài đặt.

---

#### Frontend - Admin Templates

##### [NEW] `templates/admin/fragments/admin-head.html`
Fragment `<head>` riêng cho admin, load `admin.css` thay vì `main.css`.

##### [NEW] `templates/admin/fragments/admin-sidebar.html`
Sidebar navigation với các mục:
- 📊 Tổng quan (`/admin`)
- 📦 Sản phẩm (`/admin/products`)
- 📁 Danh mục (`/admin/categories`)
- 📰 Bài viết (`/admin/posts`) *(Phase 2)*
- 📞 Yêu cầu tư vấn (`/admin/inquiries`) *(Phase 2)*
- ⚙️ Cài đặt (`/admin/settings`) *(Phase 3)*

Sidebar highlight menu item đang active dựa trên URL hiện tại.

##### [NEW] `templates/admin/fragments/admin-topbar.html`
Topbar hiển thị: tên trang hiện tại, badge thông báo yêu cầu mới.

##### [NEW] `templates/admin/dashboard.html`
- Quick stats cards: Tổng sản phẩm, Tổng bài viết, Yêu cầu tư vấn mới, Tổng đơn hàng.
- Bảng "Hoạt động gần đây": 10 yêu cầu tư vấn mới nhất.

##### [NEW] `templates/admin/product-list.html`
- Bảng danh sách: Ảnh thu nhỏ, Tên, Danh mục, Giá, Trạng thái (Active/Inactive), Nổi bật.
- Thanh tìm kiếm + bộ lọc theo danh mục.
- Nút "Thêm sản phẩm mới".

##### [NEW] `templates/admin/product-form.html`
Form thêm/sửa sản phẩm với các tab:
- **Thông tin chung**: Tên, Slug (auto-generate), Danh mục, Chất liệu, Giá, Giá gốc.
- **Hình ảnh**: Nhập URL ảnh, chọn ảnh chính (primary).
- **Nội dung**: Mô tả ngắn, Mô tả chi tiết, Ý nghĩa phong thủy, Thông số kỹ thuật.
- **Tùy chọn**: Sản phẩm nổi bật, Mới, Premium, Active.

##### [NEW] `templates/admin/category-list.html`
- Bảng danh sách danh mục dạng tree (parent → children).
- Inline form thêm/sửa danh mục.

---

#### CSS - Admin Stylesheet

##### [NEW] `static/css/admin.css`
Stylesheet riêng cho admin panel (~400 dòng):
- Layout: Sidebar 260px cố định, main content linh hoạt.
- Typography: Font Inter, kích thước body 14px.
- Components: Cards, Tables, Forms, Buttons, Badges, Alerts.
- Color palette: Dựa trên design tokens từ `base.css` (bronze, cream, dark).
- Responsive: Sidebar collapse trên tablet/mobile.

---

### Phase 2: Blog Management & Quản lý Yêu cầu Tư vấn (Leads)

##### [NEW] `templates/admin/post-list.html`
Bảng danh sách bài viết: Thumbnail, Tiêu đề, Chuyên mục, Trạng thái (Published/Draft), Ngày đăng.

##### [NEW] `templates/admin/post-form.html`
Form thêm/sửa bài viết: Tiêu đề, Slug, Chuyên mục, Ảnh đại diện (URL), Trích dẫn, Nội dung, Tác giả, Ngày đăng, Published.

##### [NEW] `templates/admin/blog-category-list.html`
Quản lý chuyên mục bài viết: Tên, Slug, Mô tả.

##### [NEW] `templates/admin/inquiry-list.html`
Bảng yêu cầu tư vấn: Tên khách, SĐT, Email, Sản phẩm quan tâm, Trạng thái, Ngày gửi.

##### [NEW] `templates/admin/inquiry-detail.html`
Chi tiết yêu cầu: Thông tin khách, sản phẩm nguồn, lời nhắn, nút chuyển trạng thái.

##### Controller routes mới trong `AdminController.java`:
- `GET /admin/posts`, `GET /admin/posts/new`, `GET /admin/posts/{id}/edit`
- `POST /admin/posts/save`, `POST /admin/posts/{id}/delete`
- `GET /admin/blog-categories`, `POST /admin/blog-categories/save`
- `GET /admin/inquiries`, `GET /admin/inquiries/{id}`
- `POST /admin/inquiries/{id}/status`

---

### Phase 3: Cài đặt Hệ thống (Settings) & CMS trang chủ

##### [NEW] `templates/admin/settings.html`
- **Thông tin chung**: Tên website, Logo URL, Favicon URL.
- **Footer**: Địa chỉ, Hotline, Email, Giờ làm việc.
- **Mạng xã hội**: Facebook, Zalo, YouTube.
- **Mã nhúng**: Google Analytics, Facebook Pixel, Zalo Chat Widget.

##### Sử dụng `SiteSettingEntity` (đã có) để lưu các cặp key-value.

##### Seed dữ liệu mặc định trong `DatabaseInitializer`:
```
site.name = "Đồ Đồng Mỹ Nghệ"
site.phone = "0987.654.321"
site.email = "info@dodongmynghe.vn"
site.address = "..."
social.facebook = "..."
social.zalo = "..."
scripts.ga = ""
scripts.fbpixel = ""
```

---

### Phase 4: Polish & Nâng cấp

- Thêm tính năng upload ảnh (MultipartFile) thay vì nhập URL.
- Tích hợp Rich Text Editor (TinyMCE CDN) cho các trường nội dung.
- Thêm pagination cho danh sách sản phẩm/bài viết.
- Thêm xác nhận xóa (modal confirm).
- Responsive hoàn chỉnh cho mobile admin.

---

## Tổng hợp File Structure

```
templates/admin/
├── fragments/
│   ├── admin-head.html
│   ├── admin-sidebar.html
│   └── admin-topbar.html
├── dashboard.html
├── product-list.html
├── product-form.html
├── category-list.html
├── post-list.html          (Phase 2)
├── post-form.html          (Phase 2)
├── blog-category-list.html (Phase 2)
├── inquiry-list.html       (Phase 2)
├── inquiry-detail.html     (Phase 2)
└── settings.html           (Phase 3)

static/css/
└── admin.css

controller/
└── AdminController.java

service/
└── AdminService.java

repository/
├── ContactInquiryRepository.java (NEW)
└── SiteSettingRepository.java    (NEW)
```

---

## Verification Plan

### Automated Tests
Không áp dụng (dự án không có test suite).

### Manual Verification
Sau mỗi Phase:
1. Khởi động server: `./mvnw spring-boot:run`.
2. Truy cập `http://localhost:8080/admin` → Kiểm tra Dashboard hiển thị đúng.
3. Thực hiện CRUD sản phẩm → Xác nhận dữ liệu lưu/sửa/xóa đúng trong DB.
4. Kiểm tra frontend (`/san-pham/...`) → Xác nhận dữ liệu thay đổi từ admin phản ánh đúng trên giao diện người dùng.
5. Kiểm tra responsive trên viewport 768px (tablet).
