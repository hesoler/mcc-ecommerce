package com.ecommerce.product_service.dataloader;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;

import java.math.BigDecimal;

//@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String @NonNull ... args) throws Exception {

        Product product = Product.builder()
                .name("Samsung Galaxy S24")
                .description("Smartphone with AI")
                .price(BigDecimal.valueOf(999.99))
                .build();

        productRepository.save(product);
    }

}
