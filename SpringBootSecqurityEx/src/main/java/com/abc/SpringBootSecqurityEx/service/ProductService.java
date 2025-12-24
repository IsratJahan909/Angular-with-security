package com.abc.SpringBootSecqurityEx.service;

import com.abc.SpringBootSecqurityEx.dtos.ProductRequestDTO;
import com.abc.SpringBootSecqurityEx.dtos.ProductResponseDTO;
import com.abc.SpringBootSecqurityEx.entity.Product;
import com.abc.SpringBootSecqurityEx.entity.ProductImage;
import com.abc.SpringBootSecqurityEx.repository.BrandRepository;
import com.abc.SpringBootSecqurityEx.repository.CategoryRepository;
import com.abc.SpringBootSecqurityEx.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;

    public ProductResponseDTO create(
            ProductRequestDTO dto,
            List<MultipartFile> images) {

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountPrice(dto.getDiscountPrice());
        product.setSku(dto.getSku());
        product.setStockQuantity(dto.getStockQuantity());
        product.setActive(true);

        // Brand
        if (dto.getBrandId() != null) {
            product.setBrand(
                    brandRepository.findById(dto.getBrandId())
                            .orElseThrow(() -> new RuntimeException("Brand not found"))
            );
        }

        // Categories
        if (dto.getCategoryIds() != null) {
            product.setCategories(
                    new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()))
            );
        }

        productRepository.save(product);

        // Upload images
        if (images != null && !images.isEmpty()) {
            List<ProductImage> imageList = images.stream().map(file -> {
                String url = fileUploadService.upload(file);
                ProductImage img = new ProductImage();
                img.setImageUrl(url);
                img.setProduct(product);
                return img;
            }).toList();
            product.setImages(imageList);
        }

        return mapToResponse(product);
    }

    public ProductResponseDTO update(
            Long productId,
            ProductRequestDTO dto,
            List<MultipartFile> images) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountPrice(dto.getDiscountPrice());
        product.setStockQuantity(dto.getStockQuantity());

        // Add new images (old ones kept)
        if (images != null && !images.isEmpty()) {
            images.forEach(file -> {
                String url = fileUploadService.upload(file);
                ProductImage img = new ProductImage();
                img.setImageUrl(url);
                img.setProduct(product);
                product.getImages().add(img);
            });
        }

        return mapToResponse(product);
    }

    private ProductResponseDTO mapToResponse(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .images(
                        product.getImages()
                                .stream()
                                .map(ProductImage::getImageUrl)
                                .toList()
                )
                .build();
    }
}
