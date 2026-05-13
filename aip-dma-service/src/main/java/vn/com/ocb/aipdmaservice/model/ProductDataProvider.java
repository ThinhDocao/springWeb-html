package vn.com.ocb.aipdmaservice.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDataProvider {

    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        
        Category tuongDong = Category.builder().id(1L).parentId(null).level(0).name("Tượng Đồng").slug("tuong-dong")
                .imageUrl("/images/product-tuong-dong.png").description("Tượng đồng mỹ nghệ cao cấp").productCount(24)
                .subCategories(new ArrayList<>()).build();
        tuongDong.getSubCategories().add(Category.builder().id(7L).parentId(1L).level(1).name("Tượng Bác Hồ").slug("tuong-bac-ho").productCount(5).build());
        tuongDong.getSubCategories().add(Category.builder().id(8L).parentId(1L).level(1).name("Tượng Thần Tài").slug("tuong-than-tai").productCount(8).build());
        tuongDong.getSubCategories().add(Category.builder().id(9L).parentId(1L).level(1).name("Tượng Quan Công").slug("tuong-quan-cong").productCount(6).build());
        tuongDong.getSubCategories().add(Category.builder().id(10L).parentId(1L).level(1).name("Tượng Phật").slug("tuong-phat").productCount(5).build());

        Category dinhDong = Category.builder().id(2L).parentId(null).level(0).name("Đỉnh Đồng").slug("dinh-dong")
                .imageUrl("/images/product-dinh-dong.png").description("Đỉnh đồng thờ cúng tinh xảo").productCount(18)
                .subCategories(new ArrayList<>()).build();
        dinhDong.getSubCategories().add(Category.builder().id(11L).parentId(2L).level(1).name("Đỉnh Thờ Cúng").slug("dinh-tho-cung").productCount(10).build());
        dinhDong.getSubCategories().add(Category.builder().id(12L).parentId(2L).level(1).name("Đỉnh Trang Trí").slug("dinh-trang-tri").productCount(8).build());

        Category tranhDong = Category.builder().id(3L).parentId(null).level(0).name("Tranh Đồng").slug("tranh-dong")
                .imageUrl("/images/product-tranh-dong.png").description("Tranh đồng nghệ thuật treo tường").productCount(15)
                .subCategories(new ArrayList<>()).build();
        tranhDong.getSubCategories().add(Category.builder().id(13L).parentId(3L).level(1).name("Tranh Phong Cảnh").slug("tranh-phong-canh").productCount(7).build());
        tranhDong.getSubCategories().add(Category.builder().id(14L).parentId(3L).level(1).name("Tranh Thư Pháp").slug("tranh-thu-phap").productCount(8).build());

        Category phongThuy = Category.builder().id(4L).parentId(null).level(0).name("Đồ Phong Thủy").slug("do-phong-thuy")
                .imageUrl("/images/product-phong-thuy.png").description("Đồ phong thủy bằng đồng").productCount(20)
                .subCategories(new ArrayList<>()).build();

        Category doTho = Category.builder().id(5L).parentId(null).level(0).name("Đồ Thờ").slug("do-tho-dong")
                .imageUrl("/images/product-dinh-dong.png").description("Đồ thờ cúng bằng đồng").productCount(22)
                .subCategories(new ArrayList<>()).build();

        Category quaTang = Category.builder().id(6L).parentId(null).level(0).name("Quà Tặng Doanh Nghiệp").slug("qua-tang")
                .imageUrl("/images/product-tuong-dong.png").description("Quà tặng đồng cao cấp").productCount(12)
                .subCategories(new ArrayList<>()).build();

        categories.add(tuongDong);
        categories.add(dinhDong);
        categories.add(tranhDong);
        categories.add(phongThuy);
        categories.add(doTho);
        categories.add(quaTang);
        
        return categories;
    }
    
    public static List<Category> getAllCategoriesFlat() {
        List<Category> all = new ArrayList<>();
        for (Category root : getCategories()) {
            all.add(root);
            if (root.getSubCategories() != null) {
                all.addAll(root.getSubCategories());
            }
        }
        return all;
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        // Tượng Đồng
        products.add(Product.builder().id(1L).name("Tượng Quan Công Cưỡi Ngựa").slug("tuong-quan-cong-cuoi-ngua")
                .price("15.800.000₫").originalPrice("18.500.000₫")
                .shortDescription("Tượng Quan Công cưỡi ngựa Xích Thố, đúc đồng nguyên chất, phủ patina tự nhiên.")
                .description("Tượng Quan Công cưỡi ngựa Xích Thố được đúc hoàn toàn bằng đồng đỏ nguyên chất, qua quy trình thủ công truyền thống với hơn 20 công đoạn tinh xảo. Mỗi chi tiết trên tượng đều được chạm khắc tỉ mỉ bởi nghệ nhân lành nghề, từ nếp áo bay, đao Thanh Long đến dáng ngựa oai phong.")
                .category("Tượng Quan Công").categorySlug("tuong-quan-cong")
                .material("Đồng đỏ nguyên chất").size("Cao 45cm x Ngang 35cm x Sâu 20cm").weight("8.5 kg")
                .imageUrl("/images/product-tuong-dong.png")
                .images(Arrays.asList("/images/product-tuong-dong.png", "/images/product-dinh-dong.png", "/images/product-phong-thuy.png", "/images/hero-statue.png"))
                .bestSeller(true).newProduct(false).premium(true)
                .fengShuiMeaning("Tượng Quan Công tượng trưng cho lòng trung nghĩa, tài lộc và bảo vệ gia chủ khỏi tà khí. Đặt tượng hướng ra cửa chính để trấn trạch, thu hút may mắn.")
                .detailDescription("Tượng Quan Công cưỡi ngựa Xích Thố là một trong những tác phẩm tiêu biểu của nghệ thuật đúc đồng Việt Nam. Được chế tác thủ công hoàn toàn, mỗi tượng đều mang dấu ấn riêng của người nghệ nhân.")
                .specifications("Chất liệu: Đồng đỏ nguyên chất 99.9% | Kích thước: 45x35x20cm | Trọng lượng: 8.5kg | Hoàn thiện: Patina tự nhiên | Bảo hành: 10 năm")
                .build());

        products.add(Product.builder().id(2L).name("Tượng Phật Di Lặc Ngồi").slug("tuong-phat-di-lac-ngoi")
                .price("12.500.000₫").originalPrice("14.000.000₫")
                .shortDescription("Tượng Phật Di Lặc ngồi cười tươi, đúc đồng vàng, biểu tượng may mắn và hạnh phúc.")
                .description("Tượng Phật Di Lặc với nụ cười hiền hòa, bụng phệ tượng trưng cho sự sung túc và an lạc.")
                .category("Tượng Phật").categorySlug("tuong-phat")
                .material("Đồng vàng").size("Cao 30cm x Ngang 25cm").weight("5.2 kg")
                .imageUrl("/images/product-tuong-dong.png")
                .images(Arrays.asList("/images/product-tuong-dong.png", "/images/product-tranh-dong.png", "/images/blog-feng-shui.png"))
                .bestSeller(true).newProduct(true).premium(false)
                .fengShuiMeaning("Phật Di Lặc mang đến niềm vui, sự lạc quan và tài lộc. Đặt ở phòng khách để thu hút năng lượng tích cực.")
                .detailDescription("Tượng được đúc thủ công bởi nghệ nhân làng đồng Đại Bái, Bắc Ninh.")
                .specifications("Chất liệu: Đồng vàng | Kích thước: 30x25cm | Trọng lượng: 5.2kg")
                .build());

        products.add(Product.builder().id(11L).name("Tượng Bác Hồ Vẫy Tay Chào").slug("tuong-bac-ho-vay-tay")
                .price("18.500.000₫").originalPrice("20.000.000₫")
                .shortDescription("Tượng Bác Hồ vẫy tay chào bằng đồng nguyên chất, khắc họa chân thực thần thái vị lãnh tụ.")
                .description("Tác phẩm Tượng Bác Hồ được nghệ nhân lão làng chạm khắc tỉ mỉ từ đồng đỏ nguyên khối, thể hiện sự tôn kính sâu sắc. Kích thước chuẩn phong thủy, phù hợp đặt ở phòng khách, phòng làm việc hoặc cơ quan nhà nước.")
                .category("Tượng Bác Hồ").categorySlug("tuong-bac-ho")
                .material("Đồng đỏ").size("Cao 60cm").weight("15 kg")
                .imageUrl("/images/product-tuong-dong.png")
                .images(Arrays.asList("/images/product-tuong-dong.png"))
                .bestSeller(true).newProduct(false).premium(true)
                .fengShuiMeaning("Thể hiện sự tự hào dân tộc, nhắc nhở tinh thần học tập và làm việc theo tấm gương đạo đức Hồ Chí Minh.")
                .detailDescription("Khuôn mặt Bác được khắc họa vô cùng có hồn, nụ cười hiền từ, ánh mắt sáng.")
                .specifications("Chất liệu: Đồng đỏ 99% | Kích thước: Cao 60cm | Bảo hành: Trọn đời")
                .build());

        products.add(Product.builder().id(20L).name("Tượng Bác Hồ Bán Thân Bằng Đồng Đỏ 69cm Màu Mộc").slug("tuong-bac-ho-ban-than-bang-dong-do-69cm-mau-moc")
                .price("Liên hệ")
                .shortDescription("Tượng Bác Hồ bán thân bằng đồng đỏ 69cm màu mộc. Sản phẩm được các nghệ nhân tạo dựng, mô phỏng dựa trên mẫu tượng chuẩn Quốc gia.")
                .description("Tượng được chế tác từ đồng đỏ thanh kiết, quy cách đúc hoàn toàn thủ công 100%. Bề mặt tượng được hoàn thiện với màu sắc tự nhiên, diện tượng truyền thần. Pho tượng có kích thước nhỏ gọn, chế tác tinh xảo, thích hợp là tượng trưng bày hoặc thờ cúng.")
                .category("Tượng Bác Hồ").categorySlug("tuong-bac-ho")
                .material("Đồng đỏ thanh khiết").size("Cao 69cm").weight("Tùy khối lượng đúc")
                .imageUrl("/images/product-tuong-dong.png")
                .images(Arrays.asList("/images/product-tuong-dong.png", "/images/product-tuong-dong.png"))
                .bestSeller(true).newProduct(true).premium(true)
                .fengShuiMeaning("Thể hiện lòng tôn kính, niềm tự hào dân tộc và nhắc nhở thế hệ sau noi gương đạo đức Hồ Chí Minh.")
                .detailDescription("Tượng có kiểu dáng bán thân. Pho tượng được đúc thủ công dựa trên nguyên mẫu Bác Hồ với độ truyền thần cực cao. Bề mặt tượng giữ nguyên màu đỏ mộc của đồng thanh khiết, rất nổi bật và sang trọng.")
                .specifications("Chất liệu: Đồng đỏ thanh khiết | Kích thước: Cao 69cm | Chế tác: Thủ công 100% | Bảo hành: 10 năm")
                .build());

        products.add(Product.builder().id(12L).name("Tượng Thần Tài Thổ Địa").slug("tuong-than-tai-tho-dia")
                .price("5.500.000₫").originalPrice("6.000.000₫")
                .shortDescription("Bộ tượng Thần Tài Thổ Địa bằng đồng vàng sáng bóng, mang lại tài lộc cho gia chủ.")
                .description("Bộ tượng Thần Tài Thổ Địa là vật phẩm không thể thiếu trong các gia đình, cửa hàng kinh doanh. Được đúc thủ công tinh xảo, nụ cười hoan hỷ mang năng lượng tích cực.")
                .category("Tượng Thần Tài").categorySlug("tuong-than-tai")
                .material("Đồng vàng").size("Cao 20cm").weight("4.5 kg")
                .imageUrl("/images/product-tuong-dong.png")
                .images(Arrays.asList("/images/product-tuong-dong.png"))
                .bestSeller(true).newProduct(true).premium(false)
                .fengShuiMeaning("Chiêu tài lộc, giữ của cải, giúp công việc kinh doanh buôn bán thuận buồm xuôi gió.")
                .detailDescription("Sản phẩm đúc đặc nguyên khối, hoa văn chi tiết rõ nét.")
                .specifications("Chất liệu: Đồng vàng | Kích thước: Cao 20cm/tượng | Trọng lượng: 4.5kg cả bộ")
                .build());

        // Đỉnh Đồng
        products.add(Product.builder().id(3L).name("Đỉnh Đồng Song Long Chầu Nguyệt").slug("dinh-dong-song-long-chau-nguyet")
                .price("22.000.000₫").originalPrice("25.000.000₫")
                .shortDescription("Đỉnh đồng Song Long Chầu Nguyệt, chạm khắc tinh xảo, dùng cho thờ cúng trang trọng.")
                .description("Đỉnh đồng với họa tiết Song Long Chầu Nguyệt cổ điển, được đúc nguyên khối và chạm khắc thủ công.")
                .category("Đỉnh Thờ Cúng").categorySlug("dinh-tho-cung")
                .material("Đồng đỏ nguyên chất").size("Cao 60cm x Đường kính 35cm").weight("12 kg")
                .imageUrl("/images/product-dinh-dong.png")
                .images(Arrays.asList("/images/product-dinh-dong.png"))
                .bestSeller(false).newProduct(false).premium(true)
                .fengShuiMeaning("Đỉnh đồng Song Long thể hiện sự uy nghiêm, quyền lực và may mắn cho gia chủ.")
                .detailDescription("Đỉnh đồng Song Long Chầu Nguyệt là sản phẩm cao cấp nhất trong dòng đỉnh thờ.")
                .specifications("Chất liệu: Đồng đỏ | Kích thước: 60x35cm | Trọng lượng: 12kg | Bảo hành: 15 năm")
                .build());

        products.add(Product.builder().id(4L).name("Đỉnh Đồng Hoa Sòi").slug("dinh-dong-hoa-soi")
                .price("8.500.000₫").originalPrice("10.000.000₫")
                .shortDescription("Đỉnh đồng Hoa Sòi nhỏ gọn, phù hợp ban thờ gia đình.")
                .description("Đỉnh đồng hoa sòi kích thước nhỏ, phù hợp cho ban thờ gia đình hiện đại.")
                .category("Đỉnh Trang Trí").categorySlug("dinh-trang-tri")
                .material("Đồng vàng").size("Cao 40cm x Đường kính 25cm").weight("6 kg")
                .imageUrl("/images/product-dinh-dong.png")
                .images(Arrays.asList("/images/product-dinh-dong.png"))
                .bestSeller(true).newProduct(false).premium(false)
                .fengShuiMeaning("Đỉnh đồng giúp không gian thờ cúng thêm trang nghiêm và thanh tịnh.")
                .detailDescription("Sản phẩm được đúc thủ công tại làng nghề đúc đồng truyền thống.")
                .specifications("Chất liệu: Đồng vàng | Kích thước: 40x25cm | Trọng lượng: 6kg")
                .build());

        // Tranh Đồng
        products.add(Product.builder().id(5L).name("Tranh Đồng Thuận Buồm Xuôi Gió").slug("tranh-dong-thuan-buom-xuoi-gio")
                .price("18.000.000₫").originalPrice("21.000.000₫")
                .shortDescription("Tranh đồng Thuận Buồm Xuôi Gió, kích thước lớn, chạm nổi tinh xảo.")
                .description("Tranh đồng nghệ thuật với chủ đề Thuận Buồm Xuôi Gió, biểu tượng của sự hanh thông trong công việc và cuộc sống.")
                .category("Tranh Đồng").categorySlug("tranh-dong")
                .material("Đồng đỏ").size("80cm x 120cm").weight("15 kg")
                .imageUrl("/images/product-tranh-dong.png")
                .images(Arrays.asList("/images/product-tranh-dong.png"))
                .bestSeller(true).newProduct(true).premium(true)
                .fengShuiMeaning("Tranh Thuận Buồm Xuôi Gió mang ý nghĩa công việc thuận lợi, suôn sẻ, phù hợp treo phòng làm việc hoặc phòng khách.")
                .detailDescription("Tranh được chạm nổi thủ công trên tấm đồng nguyên khối.")
                .specifications("Chất liệu: Đồng đỏ | Kích thước: 80x120cm | Trọng lượng: 15kg")
                .build());

        products.add(Product.builder().id(6L).name("Tranh Đồng Mã Đáo Thành Công").slug("tranh-dong-ma-dao-thanh-cong")
                .price("25.000.000₫").originalPrice("28.000.000₫")
                .shortDescription("Tranh đồng tám ngựa phi, biểu tượng thành công và quyền lực.")
                .description("Tranh đồng Mã Đáo Thành Công với tám ngựa phi nước đại, tượng trưng cho sự thăng tiến.")
                .category("Tranh Đồng").categorySlug("tranh-dong")
                .material("Đồng vàng").size("100cm x 150cm").weight("20 kg")
                .imageUrl("/images/product-tranh-dong.png")
                .images(Arrays.asList("/images/product-tranh-dong.png"))
                .bestSeller(false).newProduct(true).premium(true)
                .fengShuiMeaning("Tám con ngựa phi tượng trưng cho tám hướng tài lộc, mang lại thành công vượt bậc.")
                .detailDescription("Tác phẩm nghệ thuật cao cấp, mỗi bức là duy nhất.")
                .specifications("Chất liệu: Đồng vàng | Kích thước: 100x150cm | Trọng lượng: 20kg")
                .build());

        // Đồ Phong Thủy
        products.add(Product.builder().id(7L).name("Cóc Ba Chân Ngậm Tiền").slug("coc-ba-chan-ngam-tien")
                .price("3.500.000₫").originalPrice("4.200.000₫")
                .shortDescription("Cóc ba chân ngậm tiền bằng đồng, biểu tượng tài lộc phong thủy.")
                .description("Cóc ba chân (Thiềm Thừ) ngậm đồng tiền vàng, một trong những linh vật phong thủy nổi tiếng nhất.")
                .category("Đồ Phong Thủy").categorySlug("do-phong-thuy")
                .material("Đồng vàng").size("Cao 15cm x Ngang 18cm").weight("2.5 kg")
                .imageUrl("/images/product-phong-thuy.png")
                .images(Arrays.asList("/images/product-phong-thuy.png"))
                .bestSeller(true).newProduct(false).premium(false)
                .fengShuiMeaning("Cóc ba chân tượng trưng cho tiền tài, phú quý. Đặt trên bàn làm việc hoặc quầy thu ngân để kích tài lộc.")
                .detailDescription("Sản phẩm phong thủy được chế tác từ đồng vàng nguyên chất.")
                .specifications("Chất liệu: Đồng vàng | Kích thước: 15x18cm | Trọng lượng: 2.5kg")
                .build());

        products.add(Product.builder().id(8L).name("Tỳ Hưu Đồng Phong Thủy").slug("ty-huu-dong-phong-thuy")
                .price("6.800.000₫").originalPrice("7.500.000₫")
                .shortDescription("Tỳ Hưu bằng đồng nguyên chất, linh vật chiêu tài hóa sát.")
                .description("Tỳ Hưu đồng với tư thế oai phong, được chế tác tỉ mỉ từng chi tiết.")
                .category("Đồ Phong Thủy").categorySlug("do-phong-thuy")
                .material("Đồng đỏ").size("Cao 20cm x Dài 25cm").weight("4 kg")
                .imageUrl("/images/product-phong-thuy.png")
                .images(Arrays.asList("/images/product-phong-thuy.png"))
                .bestSeller(false).newProduct(true).premium(true)
                .fengShuiMeaning("Tỳ Hưu là linh vật mạnh nhất trong chiêu tài, hóa sát. Chỉ ăn mà không nhả, tượng trưng cho việc giữ tài sản.")
                .detailDescription("Linh vật phong thủy cao cấp cho doanh nhân và người kinh doanh.")
                .specifications("Chất liệu: Đồng đỏ | Kích thước: 20x25cm | Trọng lượng: 4kg")
                .build());

        // Đồ Thờ
        products.add(Product.builder().id(9L).name("Bộ Tam Sự Đồng Vàng").slug("bo-tam-su-dong-vang")
                .price("9.500.000₫").originalPrice("11.000.000₫")
                .shortDescription("Bộ tam sự đồng vàng gồm đỉnh, hai chân nến, trang nghiêm cho ban thờ.")
                .description("Bộ tam sự (một đỉnh đồng và hai chân đèn) bằng đồng vàng nguyên chất.")
                .category("Đồ Thờ").categorySlug("do-tho-dong")
                .material("Đồng vàng").size("Đỉnh cao 50cm, Nến cao 40cm").weight("10 kg")
                .imageUrl("/images/product-dinh-dong.png")
                .images(Arrays.asList("/images/product-dinh-dong.png"))
                .bestSeller(true).newProduct(false).premium(false)
                .fengShuiMeaning("Bộ tam sự là đồ thờ cúng quan trọng, thể hiện lòng thành kính với tổ tiên.")
                .detailDescription("Bộ tam sự được đúc thủ công, hoàn thiện bóng mịn.")
                .specifications("Chất liệu: Đồng vàng | Đỉnh: 50cm, Nến: 40cm | Tổng: 10kg")
                .build());

        // Quà Tặng
        products.add(Product.builder().id(10L).name("Trống Đồng Đông Sơn Mini").slug("trong-dong-dong-son-mini")
                .price("2.800.000₫").originalPrice("3.500.000₫")
                .shortDescription("Trống đồng Đông Sơn phiên bản thu nhỏ, quà tặng văn hóa ý nghĩa.")
                .description("Trống đồng Đông Sơn thu nhỏ, tái hiện chính xác họa tiết trống đồng cổ.")
                .category("Quà Tặng Doanh Nghiệp").categorySlug("qua-tang")
                .material("Đồng đỏ").size("Đường kính 15cm x Cao 12cm").weight("1.5 kg")
                .imageUrl("/images/product-tuong-dong.png")
                .images(Arrays.asList("/images/product-tuong-dong.png"))
                .bestSeller(true).newProduct(true).premium(false)
                .fengShuiMeaning("Trống đồng là biểu tượng của văn hóa Việt Nam, mang ý nghĩa đoàn kết và thịnh vượng.")
                .detailDescription("Quà tặng ý nghĩa cho đối tác trong và ngoài nước.")
                .specifications("Chất liệu: Đồng đỏ | Đường kính: 15cm | Cao: 12cm | Trọng lượng: 1.5kg")
                .build());

        return products;
    }

    public static List<Product> getProductsByCategory(String categorySlug) {
        return getAllProducts().stream()
                .filter(p -> p.getCategorySlug().equals(categorySlug))
                .collect(Collectors.toList());
    }

    public static Product getProductBySlug(String slug) {
        return getAllProducts().stream()
                .filter(p -> p.getSlug().equals(slug))
                .findFirst()
                .orElse(null);
    }

    public static List<Product> getBestSellers() {
        return getAllProducts().stream()
                .filter(Product::isBestSeller)
                .collect(Collectors.toList());
    }

    public static List<Product> getNewProducts() {
        return getAllProducts().stream()
                .filter(Product::isNewProduct)
                .collect(Collectors.toList());
    }

    public static List<Product> getPremiumProducts() {
        return getAllProducts().stream()
                .filter(Product::isPremium)
                .collect(Collectors.toList());
    }

    public static List<BlogPost> getAllBlogPosts() {
        List<BlogPost> posts = new ArrayList<>();

        posts.add(BlogPost.builder().id(1L)
                .title("Ý Nghĩa Phong Thủy Của Tượng Quan Công Trong Nhà")
                .slug("y-nghia-phong-thuy-tuong-quan-cong")
                .excerpt("Tìm hiểu vị trí đặt tượng Quan Công chuẩn phong thủy, giúp gia chủ công danh hanh thông, tài lộc vượng phát...")
                .content("<p>Tượng Quan Công là một trong những linh vật phong thủy được sử dụng phổ biến nhất trong văn hóa Á Đông. Với hình ảnh oai phong, cầm đao Thanh Long Yển Nguyệt, Quan Công tượng trưng cho lòng trung nghĩa, sức mạnh và công lý.</p><h3>1. Ý Nghĩa Của Tượng Quan Công</h3><p>Quan Công (hay Quan Vũ) là một vị tướng thời Tam Quốc, nổi tiếng với lòng trung thành tuyệt đối. Trong phong thủy, tượng Quan Công có các ý nghĩa:</p><ul><li>Trấn trạch, bảo vệ gia đình</li><li>Thu hút tài lộc, đặc biệt cho người kinh doanh</li><li>Tăng cường uy quyền, sự tôn trọng</li></ul><h3>2. Cách Đặt Tượng Quan Công</h3><p>Đặt tượng Quan Công hướng ra cửa chính, ở vị trí cao, trang trọng. Tránh đặt trong phòng ngủ hoặc nhà bếp.</p>")
                .category("Kiến thức phong thủy")
                .imageUrl("/images/product-tuong-dong.png")
                .publishDate("15/04/2026").author("Nghệ nhân Văn Minh").build());

        posts.add(BlogPost.builder().id(2L)
                .title("Hướng Dẫn Bảo Quản Đồ Đồng Mỹ Nghệ Đúng Cách")
                .slug("huong-dan-bao-quan-do-dong")
                .excerpt("Đồ đồng cần được bảo quản đúng cách để giữ vẻ đẹp và giá trị qua hàng chục năm sử dụng...")
                .content("<p>Đồ đồng mỹ nghệ là những tác phẩm nghệ thuật có giá trị lâu dài. Tuy nhiên, để giữ được vẻ đẹp nguyên bản, bạn cần biết cách bảo quản đúng cách.</p><h3>1. Vệ Sinh Thường Xuyên</h3><p>Dùng khăn mềm khô lau bụi hàng tuần. Tránh dùng hóa chất tẩy rửa mạnh.</p><h3>2. Tránh Ẩm Ướt</h3><p>Đặt đồ đồng ở nơi khô ráo, thoáng mát. Độ ẩm cao sẽ gây oxy hóa bề mặt.</p><h3>3. Đánh Bóng Định Kỳ</h3><p>Mỗi 3-6 tháng, dùng kem đánh bóng đồng chuyên dụng để phục hồi độ sáng.</p>")
                .category("Bảo quản đồ đồng")
                .imageUrl("/images/blog-feng-shui.png")
                .publishDate("10/04/2026").author("Đội ngũ kỹ thuật").build());

        posts.add(BlogPost.builder().id(3L)
                .title("Top 5 Mẫu Tranh Đồng Treo Phòng Khách Sang Trọng Nhất")
                .slug("top-5-tranh-dong-phong-khach")
                .excerpt("Khám phá 5 mẫu tranh đồng được ưa chuộng nhất cho phòng khách hiện đại, vừa đẹp vừa hợp phong thủy...")
                .content("<p>Tranh đồng không chỉ là vật trang trí mà còn mang giá trị phong thủy sâu sắc. Dưới đây là 5 mẫu tranh đồng được yêu thích nhất:</p><h3>1. Tranh Thuận Buồm Xuôi Gió</h3><p>Phù hợp cho người kinh doanh, mang ý nghĩa công việc hanh thông.</p><h3>2. Tranh Mã Đáo Thành Công</h3><p>Tám ngựa phi, tượng trưng cho sự thăng tiến vượt bậc.</p><h3>3. Tranh Đồng Quê Việt Nam</h3><p>Gợi nhớ về quê hương, phù hợp phòng khách truyền thống.</p><h3>4. Tranh Chữ Phúc - Lộc - Thọ</h3><p>Ba chữ vàng cổ điển, mang phước lành cho gia đình.</p><h3>5. Tranh Tùng Hạc Diên Niên</h3><p>Biểu tượng trường thọ và sức khỏe dồi dào.</p>")
                .category("Nghệ thuật đồng")
                .imageUrl("/images/product-tranh-dong.png")
                .publishDate("05/04/2026").author("Nghệ nhân Văn Minh").build());

        return posts;
    }

    public static BlogPost getBlogPostBySlug(String slug) {
        return getAllBlogPosts().stream()
                .filter(p -> p.getSlug().equals(slug))
                .findFirst()
                .orElse(null);
    }
}
