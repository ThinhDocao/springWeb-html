# Đồ Đồng Mỹ Nghệ — Premium E-commerce Website

Build a complete 6-page luxury bronze crafts e-commerce website on the existing Spring Boot 2.7.8 + Thymeleaf project.

## Background

The existing project at `aip-dma-service` has:
- Spring Boot 2.7.8 with Thymeleaf, Lombok, Security (all paths `/**` are already `permitAll`)
- Existing `AppController` with `/customer-journey` and `/login` routes
- Templates directory with old `auth.html`, `login.html`, `error.html`, and empty `fragments/`
- Static assets: `css/`, `js/`, `images/`

We will **add** new controllers, templates, CSS, JS, and images **without removing** existing files.

---

## User Review Required

> [!IMPORTANT]
> **No database** — All product/blog data will be hardcoded in Java model classes and served from controllers. This makes the site fully functional as a **frontend showcase** without needing a DB setup.

> [!IMPORTANT]
> **Images** — I will generate placeholder product/hero images using the `generate_image` tool and place them in `static/images/`. These will be bronze-themed art to match the luxury aesthetic.

> [!WARNING]
> **Security Config** — The existing `SecurityConfig` already permits all paths (`/**`). No changes needed.

---

## Open Questions

1. **Port** — The app currently runs on the default port. Should I keep it as `8080` or change it?
2. **Google Map** — For the Contact page, should I embed a real Google Maps iframe with a specific address, or use a placeholder map image?

---

## Proposed Changes

### File Structure (New Files)

```
src/main/java/vn/com/ocb/aipdmaservice/
├── controller/
│   └── HomeController.java          [NEW] — Routes for all 6 pages
├── model/
│   ├── Product.java                 [NEW] — Product POJO
│   ├── Category.java                [NEW] — Category POJO
│   └── BlogPost.java                [NEW] — Blog post POJO
│   └── ProductDataProvider.java     [NEW] — Static data provider

src/main/resources/
├── templates/
│   ├── fragments/
│   │   ├── header.html              [NEW] — Shared header with mega menu
│   │   ├── footer.html              [NEW] — Shared footer
│   │   └── head.html                [NEW] — Shared <head> meta + CSS links
│   ├── index.html                   [MODIFY] — Homepage (replaces existing)
│   ├── category.html                [NEW] — Category listing page
│   ├── product-detail.html          [NEW] — Product detail page
│   ├── about.html                   [NEW] — About/giới thiệu page
│   ├── blog.html                    [NEW] — Blog listing page
│   ├── blog-detail.html             [NEW] — Single blog post page
│   └── contact.html                 [NEW] — Contact page
├── static/
│   ├── css/
│   │   └── main.css                 [NEW] — Full design system + all page styles
│   ├── js/
│   │   └── main.js                  [NEW] — Animations, interactions, mega menu
│   └── images/
│       ├── hero-statue.png          [NEW] — Hero section bronze statue
│       ├── logo.png                 [NEW] — Brand logo
│       ├── category-*.png           [NEW] — 6 category card images
│       ├── product-*.png            [NEW] — Product images
│       ├── artisan.png              [NEW] — Artisan storytelling image
│       └── blog-*.png               [NEW] — Blog thumbnails
```

---

### Component 1: Java Controllers & Models

#### [NEW] [HomeController.java](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/java/vn/com/ocb/aipdmaservice/controller/HomeController.java)

Routes:
| Method | Path | Template | Description |
|--------|------|----------|-------------|
| GET | `/` | `index` | Homepage |
| GET | `/gioi-thieu` | `about` | About page |
| GET | `/san-pham` | `category` | All products |
| GET | `/tuong-dong` | `category` | Bronze statues category |
| GET | `/tranh-dong` | `category` | Bronze paintings category |
| GET | `/do-tho-dong` | `category` | Bronze worship items |
| GET | `/do-phong-thuy` | `category` | Feng shui items |
| GET | `/qua-tang` | `category` | Gift items |
| GET | `/san-pham/{slug}` | `product-detail` | Product detail |
| GET | `/tin-tuc` | `blog` | Blog listing |
| GET | `/tin-tuc/{slug}` | `blog-detail` | Blog detail |
| GET | `/lien-he` | `contact` | Contact page |

Each route populates the Thymeleaf model with hardcoded data.

#### [NEW] [Product.java](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/java/vn/com/ocb/aipdmaservice/model/Product.java)

Fields: `id`, `name`, `slug`, `price`, `originalPrice`, `description`, `shortDescription`, `category`, `material`, `size`, `imageUrl`, `images[]`, `isBestSeller`, `isNew`, `isPremium`, `fengShuiMeaning`

#### [NEW] [Category.java](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/java/vn/com/ocb/aipdmaservice/model/Category.java)

Fields: `id`, `name`, `slug`, `imageUrl`, `productCount`

#### [NEW] [BlogPost.java](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/java/vn/com/ocb/aipdmaservice/model/BlogPost.java)

Fields: `id`, `title`, `slug`, `excerpt`, `content`, `category`, `imageUrl`, `publishDate`, `author`

#### [NEW] [ProductDataProvider.java](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/java/vn/com/ocb/aipdmaservice/model/ProductDataProvider.java)

Static utility providing hardcoded lists of products, categories, and blog posts.

---

### Component 2: CSS Design System

#### [NEW] [main.css](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/static/css/main.css)

Design tokens:
```css
:root {
  --color-primary: #E1F0C4;
  --color-bronze: #8C6239;
  --color-bronze-light: #B8860B;
  --color-dark: #2B2B2B;
  --color-cream: #FAFAF5;
  --color-white: #FFFFFF;
  --font-heading: 'Poppins', sans-serif;
  --font-body: 'Inter', sans-serif;
  --shadow-card: 0 4px 20px rgba(0,0,0,0.08);
  --shadow-hover: 0 12px 40px rgba(140,98,57,0.15);
  --radius: 12px;
  --transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
```

Sections covered:
- Global reset & typography
- Header with mega menu dropdown
- Hero banner (split layout, gradient background)
- Category grid cards with hover lift
- Product cards (uniform height, image zoom, wishlist)
- Trust section (4 icon cards)
- Brand story (2-column split)
- Blog grid cards
- Footer (multi-column)
- Category page sidebar + product grid
- Product detail gallery + info layout
- About page sections
- Blog listing & detail
- Contact form + map
- Responsive breakpoints (mobile-first)
- Scroll animations (fade-up, stagger)
- Quick view modal

---

### Component 3: Thymeleaf Templates

#### [NEW] [head.html](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/templates/fragments/head.html)
Shared `<head>` fragment: meta tags, Google Fonts (Poppins + Inter), `main.css` link, favicon.

#### [NEW] [header.html](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/templates/fragments/header.html)
- Logo
- Mega menu: Giới thiệu, Sản phẩm (dropdown with subcategories), Tin tức, Liên hệ
- Search bar (cosmetic)
- Icons: Cart, Hotline, Zalo

#### [NEW] [footer.html](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/templates/fragments/footer.html)
- Company info, map placeholder, hotline, social icons
- Policy links, newsletter signup

#### [MODIFY] [index.html](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/templates/index.html)
**Homepage** with all sections:
1. Hero Banner — full-width, split layout (headline left, statue image right), gradient cream+bronze BG
2. Danh mục nổi bật — 6-card grid
3. Sản phẩm nổi bật — filter tabs (Bán chạy / Mới nhất / Cao cấp) + product cards
4. Vì sao chọn chúng tôi — 4 trust cards
5. Story thương hiệu — 2-column (artisan image + storytelling)
6. Tin tức/Blog — 3-card grid

#### [NEW] [category.html](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/templates/category.html)
- Sidebar filters (category, price range, material, size)
- 3-4 column product grid
- Uniform product cards

#### [NEW] [product-detail.html](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/templates/product-detail.html)
- Gallery (main image + thumbnails, zoom on hover)
- Product info (name, price, description, specs, material, size)
- CTAs: Mua ngay, Liên hệ tư vấn
- Tabs: Mô tả chi tiết, Thông số, Ý nghĩa phong thủy, Đánh giá
- Related products carousel

#### [NEW] [about.html](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/templates/about.html)
- Hero
- Brand history timeline
- Artisan section
- Workshop showcase
- Values & CTA

#### [NEW] [blog.html](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/templates/blog.html)
- Category filters
- Blog cards grid

#### [NEW] [blog-detail.html](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/templates/blog-detail.html)
- Full article content
- Related posts sidebar

#### [NEW] [contact.html](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/templates/contact.html)
- Contact form
- Google Map embed
- Hotline + Zalo
- FAQ accordion

---

### Component 4: JavaScript

#### [NEW] [main.js](file:///c:/Users/PC/Documents/springWeb-html/aip-dma-service/src/main/resources/static/js/main.js)

Features:
- **Scroll animations** — IntersectionObserver for fade-up/stagger
- **Mega menu** — Hover-based dropdown
- **Mobile menu** — Hamburger toggle
- **Product tabs** — Filter switching (Bán chạy / Mới nhất / Cao cấp)
- **Image gallery** — Thumbnail click + zoom on hover
- **Quick view modal** — Product preview popup
- **FAQ accordion** — Toggle expand/collapse
- **Smooth scroll** — For anchor links
- **Search overlay** — Toggle search bar
- **Back to top** — Floating button

---

### Component 5: Generated Images

I will use `generate_image` to create:
1. **Hero statue** — Premium bronze statue on transparent/cream background
2. **Logo** — Minimalist bronze/gold logo text
3. **6 category cards** — Tượng đồng, Đỉnh đồng, Tranh đồng, Đồ phong thủy, Đồ thờ, Quà tặng
4. **8+ product images** — Various bronze items
5. **Artisan image** — Craftsman at work
6. **3 blog thumbnails** — Feng shui, bronze care, statue meanings

---

## Verification Plan

### Automated Tests
```bash
# Build the project
./mvnw clean compile

# Run the application
./mvnw spring-boot:run
```

### Browser Verification
- Open `http://localhost:8080/` and verify all 6 pages
- Check responsive design at mobile breakpoints
- Verify all navigation links work
- Test hover animations and scroll effects
- Verify product detail page gallery interaction
