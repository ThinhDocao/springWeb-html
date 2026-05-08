# Project Snapshot: Bronze Craft E-Commerce (Đồ Đồng Thành Đạt - Migration)

## 1. Cấu trúc thư mục chính
Dự án được tổ chức theo chuẩn Maven Spring Boot tại thư mục: `c:\Users\PC\Documents\springWeb-html\aip-dma-service`

```text
aip-dma-service/
├── src/
│   ├── main/
│   │   ├── java/vn/com/ocb/aipdmaservice/
│   │   │   ├── config/          # Chứa cấu hình ứng dụng, DatabaseInitializer (seed dữ liệu)
│   │   │   ├── controller/      # Web controllers (HomeController), GlobalControllerAdvice (inject categories)
│   │   │   ├── entity/          # JPA Entities (CategoryEntity, ProductEntity, BlogPostEntity...)
│   │   │   ├── model/           # DTOs, ProductDataProvider (dữ liệu mẫu)
│   │   │   ├── repository/      # Spring Data JPA Repositories
│   │   │   └── service/         # Business logic (AppService)
│   │   └── resources/
│   │       ├── static/          # CSS, JS, Images (pages.css, main.js chứa hiệu ứng)
│   │       ├── templates/       # Thymeleaf templates (fragments/, index.html, category.html, product-detail.html...)
│   │       └── application.yml  # Cấu hình db, hibernate ddl-auto...
├── pom.xml                      # Quản lý dependencies
└── mvnw / mvnw.cmd              # Maven wrapper
```

## 2. Công nghệ sử dụng
- **Backend**: Java 8, Spring Boot 2.7.8, Spring Data JPA
- **Frontend**: Thymeleaf, HTML5, Vanilla CSS, JS (Không dùng framework JS nặng)
- **Database**: MySQL 8.0 (kết nối qua jdbc:mysql://localhost:3306/db)
- **Build Tool**: Maven
- **Logging**: Log4j2 / SLF4J mặc định của Spring Boot

## 3. Trạng thái hiện tại
Dự án đang trong quá trình hoàn thiện các tính năng cốt lõi của một trang thương mại điện tử chuyên về đồ đồng mỹ nghệ cao cấp. Mọi blocker về giao diện và điều hướng đã được giải quyết.

### Các thành phần đã hoàn thành (Tasks Done):
- **Thiết kế CSDL & ERD**: Đã xây dựng hoàn chỉnh cấu trúc phân cấp danh mục (Cha - Con) với `CategoryEntity` và `ProductEntity`.
- **Đồng bộ Dữ liệu mẫu (Seeding)**: File `DatabaseInitializer` tự động tạo bảng (khi ddl-auto: create/update) và chèn dữ liệu mẫu từ `ProductDataProvider`. Gần đây nhất đã cào và thêm thành công data thực tế sản phẩm *"Tượng Bác Hồ Bán Thân Bằng Đồng Đỏ 69cm Màu Mộc"*.
- **Routing & Controllers**: 
  - Đã triển khai Dynamic Routing: `/san-pham/{slug}` (Chi tiết sản phẩm) và `/{slug}` (Trang danh mục).
  - Khắc phục lỗi thiếu menu Dropdown trên các trang con bằng cách tạo `GlobalControllerAdvice` để tự động đẩy dữ liệu `categories` vào mọi `Model` Thymeleaf.
- **Giao diện (UI/UX)**:
  - Cấu trúc layout thống nhất với Header, Footer, và Mega-menu động.
  - Sửa lỗi giật/rung (jitter) khi rê chuột vào hình ảnh sản phẩm trong thư viện ảnh (kết hợp CSS transitions và JS setTimeout).

### Ghi chú cấu hình hiện tại:
- Cơ sở dữ liệu đang trỏ đến `jdbc:mysql://localhost:3306/db` với user: `root` / pass: `123321`.
- Chế độ `spring.jpa.hibernate.ddl-auto` hiện tại đang là `update` để giữ nguyên dữ liệu.
- Project chạy bình thường trên cổng `8080` thông qua lệnh: `.\mvnw.cmd spring-boot:run`.
