# AI_CONTEXT.md - Project Snapshot

Last updated: 2026-05-18

## 1. Project Structure

This is a Spring Boot + Thymeleaf website for a premium Vietnamese bronze handicraft store (`Do Dong My Nghe`). The local source project is currently at:

`c:\Users\PC\Documents\springWeb-html\aip-dma-service`

The deployment/demo folder to remember for the next account is:

`/home/dtc/download-file`

Expected deploy layout under `/home/dtc/download-file`:

```text
/home/dtc/download-file/
├── *.jar                         # Built Spring Boot runnable JAR from target/
├── config/ or application.yml      # External runtime config if used
├── logs/                          # Application logs
├── uploads/                       # Runtime uploaded images; DB stores only URL paths
├── backups/                       # MySQL dumps for demo/restore
└── static/runtime files if copied by deployment script
```

Main source layout:

```text
.
├── pom.xml                        # Maven project config
├── mvnw, mvnw.cmd                 # Maven wrapper
├── src/main/java/vn/com/ocb/aipdmaservice/
│   ├── controller/                # Public controllers: Home, App, Search, Order
│   ├── controller/admin/          # Admin controllers: dashboard, catalog, blog, leads, media, settings, upload, auth
│   ├── entity/                    # JPA entities: product, category, blog, order, lead, settings...
│   ├── repository/                # Spring Data repositories
│   ├── service/                   # AppService, UploadService, SiteSettingService, MediaLibraryService, AdminLeadService
│   ├── config/                    # SecurityConfig, WebMvcConfig, DatabaseInitializer...
│   └── util/SlugUtils.java
├── src/main/resources/
│   ├── application.yml            # Datasource, JPA, admin login, upload dir
│   ├── templates/                 # Thymeleaf public pages
│   ├── templates/fragments/       # Public head/header/footer
│   ├── templates/admin/           # Admin UI
│   └── static/                    # CSS/JS/images
├── uploads/                       # Local uploaded files, mounted/served as /uploads/**
├── backups/                       # DB dump files, including full schema/data dump
├── logs/                          # Runtime logs
└── target/                        # Build output; JAR appears here after mvn package
```

Important templates/static files:

- Public: `home.html`, `product-detail.html`, `blog-detail.html`, fragments `head.html`, `header.html`, `footer.html`.
- Admin: `templates/admin/dashboard.html`, `products/list.html`, `products/form.html`, `categories/*`, `materials/*`, `blog/*`, `blog-categories/*`, `leads/*`, `media.html`, `settings.html`, `login.html`.
- CSS/JS: `static/css/main.css`, `layout.css`, `pages.css`, `admin.css`; `static/js/main.js`, `admin.js`, `cart.js`.

## 2. Technology

- Java 8.
- Spring Boot 2.7.8.
- Maven / Maven Wrapper.
- Thymeleaf server-rendered UI.
- Spring Data JPA + Hibernate.
- MySQL 8.
- Spring Security with simple admin form login.
- CKEditor CDN for rich text fields in admin.
- Log4j2/logging context to preserve for deployment notes; logs are expected under `logs/` on runtime server.
- Uploads are stored on filesystem via `UploadService`; database stores relative URLs like `/uploads/products/...`.

Common commands:

```bash
./mvnw test
./mvnw package
java -jar target/aip-dma-service-0.0.1-SNAPSHOT.jar
```

On Windows local dev, Java 8 may need:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk1.8.0_202'
.\mvnw.cmd test
```

## 3. Database / ERD Snapshot

Core tables:

- `category`: parent/child category tree, SEO fields.
- `product`: product content, pricing, flags, descriptions, SEO fields.
- `product_image`: product gallery, primary image, sort order.
- `material`: product material list.
- `blog_category`, `blog_post`: blog taxonomy and posts, SEO fields, publish status.
- `contact_inquiry`: lead/contact requests.
- `customer_order`, `order_item`: old order/cart flow still exists.
- `site_setting`: key-value site settings such as `siteName`, `logoUrl`, `faviconUrl`, hotline, email, address, social links, scripts.

Backups currently exist under `backups/`; important full schema/data dumps include `db-full-20260515-102319.sql` and `db-full-create-insert-20260516-091854.sql`.

## 4. Current Implementation Status

Public user UI is already done. The admin panel has also been implemented under `/admin/**` with a clean/minimal sidebar + topbar layout, brand colors, and admin-specific CSS/JS.

Completed admin work:

- Admin login/session protection for `/admin/**`; credentials are configurable via `ADMIN_USERNAME` and `ADMIN_PASSWORD`.
- Dashboard with quick stats and recent lead activity.
- Product CRUD:
  - list/search/filter products;
  - newest updated/created products are ordered first;
  - create/edit/delete product;
  - gallery upload with preview thumbnails;
  - primary image selection;
  - CKEditor fields for rich descriptions;
  - SEO fields `metaTitle`, `metaDescription`.
- Category CRUD:
  - parent/child display;
  - child rows visually distinguished from parent rows;
  - SEO fields;
  - category image upload.
- Material CRUD.
- Blog management:
  - blog category CRUD;
  - blog post CRUD;
  - publish checkbox means visible on public blog when checked, draft/hidden when unchecked;
  - CKEditor content, thumbnail upload, SEO fields.
- Leads:
  - lead list/detail screens;
  - status handling for consulting workflow.
- Settings:
  - manage `site_setting`;
  - upload logo/favicon;
  - header and footer logos now read from `site_setting.logoUrl`;
  - site favicon/title/meta use settings where available.
- Media library:
  - `/admin/media` is a read-only browser for folder structure and images under `uploads/`.
- Upload organization:
  - products: `/uploads/products/{slug}/gallery` and `/uploads/products/{slug}/content`;
  - categories: `/uploads/categories/{slug}`;
  - blog: `/uploads/blog/{slug}/thumbnail` and content folders where needed;
  - settings: `/uploads/settings/logo`, `/uploads/settings/favicon`.
- CKEditor image handling:
  - images uploaded from CKEditor are saved to disk;
  - DB stores only relative image paths;
  - image resize/alignment styles are allowed so frontend display can preserve left/right/center layout.
- SEO:
  - `meta_title` is the Google/browser title override;
  - `meta_description` is the search result description;
  - public `head.html` and detail pages should keep mapping these fields into `<title>` and `<meta name="description">`.

## 5. Implementation Plan Summary

The saved admin plan (`ADMIN_PLAN.md` and `implementation_plan.md`) designed the admin as V1 with these modules:

1. Foundation: admin layout, login, dashboard, shared CSS/JS, sidebar/topbar.
2. Catalog: products, categories, materials, upload gallery, SEO fields.
3. Blog: blog posts and categories with CKEditor.
4. Leads: contact/consulting requests instead of online payment focus.
5. Settings: site-wide logo, favicon, footer/contact/social/script settings.
6. Media: inspect uploaded folder/image structure.

V1 intentionally avoids complex role-based authorization and online payment. The business goal is product showcase, SEO content, and collecting customer leads.

## 6. Notes For The Next AI Session

- Do not overwrite user changes blindly. The worktree may contain many generated/admin files and uploaded assets.
- Prefer reading these files first: `AI_CONTEXT.md`, `ADMIN_PLAN.md`, `ERD.md`, `implementation_plan.md`, `application.yml`.
- When editing code, preserve the existing Spring Boot + Thymeleaf style.
- Use `rg` for searching.
- Use `apply_patch` for file edits.
- After Java changes, run `mvnw test` with Java 8.
- If demoing to a customer with Docker, include MySQL + app + mounted `uploads/`; use a full DB dump, not data-only, because data-only does not create tables.
