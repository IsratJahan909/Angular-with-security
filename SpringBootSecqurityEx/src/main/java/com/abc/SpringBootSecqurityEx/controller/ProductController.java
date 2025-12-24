package com.abc.SpringBootSecqurityEx.controller;

import com.abc.SpringBootSecqurityEx.dtos.ProductRequestDTO;
import com.abc.SpringBootSecqurityEx.dtos.ProductResponseDTO;
import com.abc.SpringBootSecqurityEx.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> create(
            @RequestPart("product") @Valid ProductRequestDTO dto,
            @RequestPart(value = "images", required = false)
            List<MultipartFile> images) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(dto, images));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @RequestPart("product") @Valid ProductRequestDTO dto,
            @RequestPart(value = "images", required = false)
            List<MultipartFile> images) {

        return ResponseEntity.ok(productService.update(id, dto, images));
    }
}
