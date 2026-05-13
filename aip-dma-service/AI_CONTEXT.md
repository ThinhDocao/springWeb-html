# AI_CONTEXT.md - Project Snapshot

## 1. Cấu trúc thư mục chính
Dự án được đặt tại: `c:\Users\PC\Documents\springWeb-html\aip-dma-service`
- `src/main/java`: Mã nguồn Java (Spring Boot).
- `src/main/resources`: 
  - `templates/`: Giao diện Thymeleaf.
  - `static/`: CSS, JS, Images.
  - `application.yml`: Cấu hình hệ thống (MySQL, JPA).
- `target/`: Chứa file `.jar` sau khi build.
- `logs/`: Chứa các file log của ứng dụng.
- `pom.xml`: Quản lý phụ thuộc Maven.

## 2. Công nghệ sử dụng
- **Backend**: Java 8, Spring Boot 2.7.8, Spring Data JPA.
- **Database**: MySQL.
- **Frontend**: HTML5, Vanilla CSS, JavaScript (ES6+), Thymeleaf.
- **Tiện ích**: Lombok, Maven Wrapper.

## 3. Trạng thái hiện tại & Công việc đã hoàn thành

### Tính năng & Sửa lỗi:
- **Blog Detail**: Đã map thành công trường `content` từ database để hiển thị nội dung chi tiết bài viết thay vì chỉ hiển thị đoạn trích (excerpt).
- **Product Carousel**: Đã triển khai bộ trình chiếu ảnh tự động (4 giây/ảnh) trên trang chi tiết sản phẩm. Hỗ trợ điều khiển thủ công qua mũi tên và ảnh thu nhỏ.
- **Mock Data**: Đã cập nhật `ProductDataProvider` và `DatabaseInitializer` để tự động đồng bộ nhiều hình ảnh mẫu cho sản phẩm.
- **UI Spacing**: 
  - Đã xử lý vấn đề Header cố định che khuất nội dung.
  - Tăng khoảng cách `padding-top` lên **140px** cho trang chi tiết sản phẩm và bài viết để hiển thị Breadcrumb rõ ràng.
  - Tối ưu hóa Header Mobile để ngăn lỗi tràn chữ Logo.

### Ghi chú cho AI mới:
- Các thay đổi về Spacing được thực hiện trực tiếp qua inline style trong `product-detail.html` và `blog-detail.html` theo yêu cầu của User (sau khi discard phương án class global).
- Database được seed tự động mỗi khi khởi động server thông qua `DatabaseInitializer`. Nếu có thay đổi ở `ProductDataProvider`, cần đảm bảo cơ chế `syncProductDetailsAndImages` hoạt động để cập nhật dữ liệu hiện có.
