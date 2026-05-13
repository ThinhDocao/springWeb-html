# Project Snapshot: Bronze Craft Ecommerce (AIP-DMA Service)

This document provides the necessary context for the AI assistant to continue development of the "Đồ Đồng Mỹ Nghệ" ecommerce project.

## 1. Project Overview & Structure
A Spring Boot web application for a premium artisan bronze craft store.

**Main Directory Structure:**
- `/src/main/java/vn/com/ocb/aipdmaservice`:
    - `/controller`: Web and API controllers (HomeController, OrderController, SearchController).
    - `/entity`: JPA entities (Product, Category, Order).
    - `/service`: Business logic for products, categories, and orders.
    - `/model`: DTOs and request/response objects.
- `/src/main/resources`:
    - `/templates`: Thymeleaf HTML templates (index, product-detail, category, checkout, order-success).
    - `/templates/fragments`: Reusable components (header, footer, head).
    - `/static/css`: Vanilla CSS stylesheets (main.css imports others like cart.css, pages.css).
    - `/static/js`: Frontend logic (cart.js for LocalStorage cart, main.js for UI interactions).
- `pom.xml`: Maven dependencies.
- `application.yml`: Configuration (MySQL, Thymeleaf settings).

## 2. Technology Stack
- **Backend**: Java 8, Spring Boot 2.7.8, Spring Data JPA, Hibernate, MySQL.
- **Frontend**: HTML5, Thymeleaf, Vanilla CSS (Modern aesthetic, dark mode/luxury feel), Javascript.
- **Cart Logic**: LocalStorage-based `CartManager` in `cart.js`.
- **Checkout**: Multi-step flow with manual verification/confirmation methods.

## 3. Current State & Recent Accomplishments
We have just completed the implementation and verification of the core e-commerce flow.

### Completed Tasks:
1. **Core Cart Functionality**: Implemented a side-drawer cart using LocalStorage.
2. **Script Loading Fix**: Resolved an issue where `cart.js` was not loading due to incorrect fragment placement.
3. **Quick Add Fix**: Prevented event bubbling on the "+" quick-add button to stop unwanted redirects.
4. **Checkout Flow**: Implemented order submission via `/api/order` and redirection to a success page.
5. **Confirmation Method**: Added display of the selected confirmation method (Bank Transfer, COD, etc.) on the success page.

### Current Status:
- The service is fully functional for browsing, adding to cart, and placing orders.
- UI/UX has been polished for a "Luxury Minimal" feel with smooth animations and responsive layouts.
- **Next Potential Steps**: Admin dashboard for order management, user accounts, or advanced product filtering.

## 4. How to Run
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_202"
./mvnw spring-boot:run
```
URL: `http://localhost:8080`
