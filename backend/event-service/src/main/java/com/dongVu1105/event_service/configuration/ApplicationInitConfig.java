package com.dongVu1105.event_service.configuration;



import com.dongVu1105.event_service.entity.Category;
import com.dongVu1105.event_service.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@Slf4j
public class ApplicationInitConfig {

    CategoryRepository categoryRepository;

    @Bean
    ApplicationRunner applicationRunner() {
        log.info("Initializing application.....");
        return args -> {
            if(categoryRepository.count() == 0){
                Category category1 = new Category("TRONG_CAY", "Trồng cây");
                Category category2 = new Category("DON_RAC", "Dọn rác");
                Category category3 = new Category("TU_THIEN", "Từ thiện");
                Category category4 = new Category("BINH_DAN_HOC_VU_SO", "Bình dân học vụ số");
                categoryRepository.saveAll(List.of(category1, category2, category3, category4));
                log.info("Initiate category data");
            }
        };
    }
}
