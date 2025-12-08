package com.datn.website_xem_tin_tuc.component;

import com.datn.website_xem_tin_tuc.config.security.AppConfig;
import com.datn.website_xem_tin_tuc.entity.*;
import com.datn.website_xem_tin_tuc.enums.Active;
import com.datn.website_xem_tin_tuc.enums.Role;
import com.datn.website_xem_tin_tuc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@ConditionalOnBean(AppConfig.class)
public class DataSeeder implements CommandLineRunner  {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args)  {
        seederRole();
        createAccountAdmin();
        seedProducts();
    }
    public void seederRole () {
        if (roleRepository.count() == 0) {
            var roleAdmin = new  RoleEntity();
            var roleUser = new RoleEntity();
            var roleManage= new RoleEntity();
            roleAdmin.setName(Role.ROLE_ADMIN);
            roleAdmin.setActive(Active.HOAT_DONG);
            roleUser.setName(Role.ROLE_USER);
            roleUser.setActive(Active.HOAT_DONG);
            roleManage.setName(Role.ROLE_MANAGE);
            roleManage.setActive(Active.HOAT_DONG);
            roleRepository.save(roleAdmin);
            roleRepository.save(roleUser);
            roleRepository.save(roleManage);
        }
    }

    public void createAccountAdmin() {
        Optional<UserEntity> admin = userRepository.findByUsername("admin");
        if (!admin.isPresent()) {
            UserEntity user = new UserEntity();
            user.setUsername("admin");
            String passwordEncrypted = "$2a$10$NznSkz9.j8a2JAKa2aGYaOMD3wi6dB5Qt/6mwLmtB/Iqd6o38JlrC";
            user.setPassword(passwordEncrypted); // Mã hóa mật khẩu
            user.setAvatar("https://res.cloudinary.com/dpbo17rbt/image/upload/v1745083551/wvtsccbrg6oymhvgxv3k.png");
            user.setActive(Active.HOAT_DONG);
            userRepository.save(user);

            Optional<RoleEntity> roleEntity = roleRepository.findRoleEntityByName(Role.ROLE_ADMIN);
            if (roleEntity.isPresent()) {
                RoleEntity role = roleEntity.get();
                UserRoleEntity userRole = new UserRoleEntity();
                userRole.setRoleId(role);
                userRole.setUserId(user);
                userRoleRepository.save(userRole);
            }
        }
    }

    public void seedProducts() {
        // Chỉ seed nếu chưa có sản phẩm nào
        if (productRepository.count() > 0) {
            return;
        }

        // Lấy category đầu tiên hoặc tạo category mặc định cho sản phẩm
        CategoryEntity category = categoryRepository.findAll().stream()
                .findFirst()
                .orElse(null);

        // Danh sách sản phẩm áo đá bóng mẫu
        String[][] products = {
            // Tên, Slug, Mô tả, Giá, Giá sale, Hình ảnh, Số lượng, Size, Màu, Thương hiệu, SKU
            {"Áo đấu Manchester United 2024", "ao-dau-manchester-united-2024", 
                "Áo đấu chính thức của Manchester United mùa giải 2024. Chất liệu vải cao cấp, thấm hút mồ hôi tốt.", 
                "850000", "750000", "https://picsum.photos/800/800?random=1", "50", "S,M,L,XL,XXL", "Đỏ,Trắng", "Nike", "MU-2024-001"},
            {"Áo đấu Barcelona Home 2024", "ao-dau-barcelona-home-2024",
                "Áo đấu sân nhà của FC Barcelona mùa giải 2024. Thiết kế cổ điển với sọc xanh đỏ đặc trưng.",
                "900000", "800000", "https://picsum.photos/800/800?random=2", "45", "S,M,L,XL", "Xanh dương,Đỏ", "Nike", "BAR-2024-001"},
            {"Áo đấu Real Madrid 2024", "ao-dau-real-madrid-2024",
                "Áo đấu chính thức của Real Madrid mùa giải 2024. Màu trắng tinh khiết với logo Adidas.",
                "850000", "750000", "https://picsum.photos/800/800?random=3", "60", "S,M,L,XL,XXL", "Trắng", "Adidas", "RM-2024-001"},
            {"Áo đấu Liverpool Home 2024", "ao-dau-liverpool-home-2024",
                "Áo đấu sân nhà Liverpool mùa giải 2024. Màu đỏ đặc trưng với logo Nike.",
                "800000", "700000", "https://picsum.photos/800/800?random=4", "55", "S,M,L,XL", "Đỏ", "Nike", "LIV-2024-001"},
            {"Áo đấu PSG 2024", "ao-dau-psg-2024",
                "Áo đấu chính thức của Paris Saint-Germain mùa giải 2024. Thiết kế hiện đại với màu xanh navy.",
                "950000", "850000", "https://picsum.photos/800/800?random=5", "40", "S,M,L,XL,XXL", "Xanh navy,Đỏ", "Nike", "PSG-2024-001"},
            {"Áo đấu Bayern Munich 2024", "ao-dau-bayern-munich-2024",
                "Áo đấu sân nhà Bayern Munich mùa giải 2024. Màu đỏ đậm với sọc trắng.",
                "900000", "800000", "https://picsum.photos/800/800?random=6", "50", "S,M,L,XL", "Đỏ,Trắng", "Adidas", "BAY-2024-001"},
            {"Áo đấu Chelsea 2024", "ao-dau-chelsea-2024",
                "Áo đấu chính thức của Chelsea FC mùa giải 2024. Màu xanh hoàng gia đặc trưng.",
                "850000", "750000", "https://picsum.photos/800/800?random=7", "45", "S,M,L,XL,XXL", "Xanh dương", "Nike", "CHE-2024-001"},
            {"Áo đấu Arsenal 2024", "ao-dau-arsenal-2024",
                "Áo đấu sân nhà Arsenal mùa giải 2024. Màu đỏ với sọc trắng cổ điển.",
                "800000", "700000", "https://picsum.photos/800/800?random=8", "55", "S,M,L,XL", "Đỏ,Trắng", "Adidas", "ARS-2024-001"},
            {"Áo đấu Juventus 2024", "ao-dau-juventus-2024",
                "Áo đấu chính thức của Juventus mùa giải 2024. Sọc đen trắng đặc trưng.",
                "900000", "800000", "https://picsum.photos/800/800?random=9", "50", "S,M,L,XL,XXL", "Đen,Trắng", "Adidas", "JUV-2024-001"},
            {"Áo đấu AC Milan 2024", "ao-dau-ac-milan-2024",
                "Áo đấu sân nhà AC Milan mùa giải 2024. Sọc đỏ đen truyền thống.",
                "850000", "750000", "https://picsum.photos/800/800?random=10", "45", "S,M,L,XL", "Đỏ,Đen", "Puma", "ACM-2024-001"},
            {"Áo đấu Inter Milan 2024", "ao-dau-inter-milan-2024",
                "Áo đấu chính thức của Inter Milan mùa giải 2024. Sọc xanh đen đặc trưng.",
                "850000", "750000", "https://picsum.photos/800/800?random=11", "50", "S,M,L,XL,XXL", "Xanh dương,Đen", "Nike", "INT-2024-001"},
            {"Áo đấu Manchester City 2024", "ao-dau-manchester-city-2024",
                "Áo đấu sân nhà Manchester City mùa giải 2024. Màu xanh sky đặc trưng.",
                "900000", "800000", "https://picsum.photos/800/800?random=12", "55", "S,M,L,XL", "Xanh sky", "Puma", "MCI-2024-001"},
            {"Áo đấu Tottenham 2024", "ao-dau-tottenham-2024",
                "Áo đấu chính thức của Tottenham Hotspur mùa giải 2024. Màu trắng với viền xanh.",
                "800000", "700000", "https://picsum.photos/800/800?random=13", "50", "S,M,L,XL,XXL", "Trắng,Xanh", "Nike", "TOT-2024-001"},
            {"Áo đấu Atletico Madrid 2024", "ao-dau-atletico-madrid-2024",
                "Áo đấu sân nhà Atletico Madrid mùa giải 2024. Sọc đỏ trắng đặc trưng.",
                "850000", "750000", "https://picsum.photos/800/800?random=14", "45", "S,M,L,XL", "Đỏ,Trắng", "Nike", "ATM-2024-001"},
            {"Áo đấu Borussia Dortmund 2024", "ao-dau-borussia-dortmund-2024",
                "Áo đấu chính thức của Borussia Dortmund mùa giải 2024. Màu vàng đen đặc trưng.",
                "900000", "800000", "https://picsum.photos/800/800?random=15", "50", "S,M,L,XL,XXL", "Vàng,Đen", "Puma", "BVB-2024-001"},
            {"Áo đấu AS Roma 2024", "ao-dau-as-roma-2024",
                "Áo đấu sân nhà AS Roma mùa giải 2024. Màu đỏ vàng đặc trưng.",
                "850000", "750000", "https://picsum.photos/800/800?random=16", "40", "S,M,L,XL", "Đỏ,Vàng", "Adidas", "ROM-2024-001"},
            {"Áo đấu Napoli 2024", "ao-dau-napoli-2024",
                "Áo đấu chính thức của Napoli mùa giải 2024. Màu xanh sky đặc trưng.",
                "800000", "700000", "https://picsum.photos/800/800?random=17", "55", "S,M,L,XL,XXL", "Xanh sky", "EA7", "NAP-2024-001"},
            {"Áo đấu Leicester City 2024", "ao-dau-leicester-city-2024",
                "Áo đấu sân nhà Leicester City mùa giải 2024. Màu xanh hoàng gia.",
                "750000", "650000", "https://picsum.photos/800/800?random=18", "50", "S,M,L,XL", "Xanh dương", "Adidas", "LEI-2024-001"},
            {"Áo đấu West Ham 2024", "ao-dau-west-ham-2024",
                "Áo đấu chính thức của West Ham United mùa giải 2024. Màu đỏ và xanh.",
                "800000", "700000", "https://picsum.photos/800/800?random=19", "45", "S,M,L,XL,XXL", "Đỏ,Xanh", "Umbro", "WHU-2024-001"}
        };

        for (String[] productData : products) {
            // Kiểm tra xem sản phẩm đã tồn tại chưa
            if (productRepository.existsBySlug(productData[1])) {
                continue;
            }

            ProductEntity product = new ProductEntity();
            product.setName(productData[0]);
            product.setSlug(productData[1]);
            product.setDescription(productData[2]);
            product.setPrice(new BigDecimal(productData[3]));
            product.setPriceSale(new BigDecimal(productData[4]));
            product.setImage(productData[5]);
            product.setQuantity(Integer.parseInt(productData[6]));
            product.setSize(productData[7]);
            product.setColor(productData[8]);
            product.setBrand(productData[9]);
            product.setSku(productData[10]);
            product.setActive(Active.HOAT_DONG);
            
            // Gán category nếu có
            if (category != null) {
                product.setCategory(category);
            }

            productRepository.save(product);
        }
    }

}