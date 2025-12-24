package com.abc.SpringBootSecqurityEx.service;

import com.abc.SpringBootSecqurityEx.dtos.CategoryDTO;
import com.abc.SpringBootSecqurityEx.entity.Category;
import com.abc.SpringBootSecqurityEx.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;

    public CategoryDTO create(CategoryDTO dto, MultipartFile image) {

        Category category = new Category();
        category.setName(dto.getName());

        if (dto.getParentId() != null) {
            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }

        if (image != null && !image.isEmpty()) {
            category.setImageUrl(fileUploadService.upload(image));
        }

        categoryRepository.save(category);

        dto.setId(category.getId());
        return dto;
    }

    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryDTO(c.getId(), c.getName(),c.getImageUrl(),
                        c.getParent() != null ? c.getParent().getId() : null))
                .toList();
    }
}
