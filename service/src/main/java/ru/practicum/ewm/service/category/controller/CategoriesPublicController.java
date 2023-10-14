package ru.practicum.ewm.service.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.category.dto.CategoryDto;
import ru.practicum.ewm.service.category.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesPublicController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll(@Valid @RequestParam(defaultValue = "0") int from,
                                    @Valid @RequestParam(defaultValue = "10") int size) {
        log.info("Поступил запрос на получение списка категорий с следующими параметрами: начиная с {} страницы получить {} значений", from, size);
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable Long catId) {
        log.info("Поступил запрос на получение информации о категории с id={}", catId);
        return categoryService.getById(catId);
    }
}
