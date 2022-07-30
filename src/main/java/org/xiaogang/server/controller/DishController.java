package org.xiaogang.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaogang.server.common.R;
import org.xiaogang.server.dto.DishDto;
import org.xiaogang.server.entity.Category;
import org.xiaogang.server.entity.Dish;
import org.xiaogang.server.entity.DishFlavor;
import org.xiaogang.server.service.CategoryService;
import org.xiaogang.server.service.DishFlavorServiece;
import org.xiaogang.server.service.DishService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * className: DishController
 * description:
 * author: xiaopangio
 * date: 2022/7/26 19:45
 * version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorServiece dishFlavorServiece;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("save dishDto: {}", dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<DishDto> dishDtoPage = dishService.getPage(page, pageSize, name);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable("id") Long id) {
        DishDto dishDto = dishService.getDishByIdWithFlavor(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("save dishDto: {}", dishDto);
        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {
        log.info("update status: {}, idList: {}", status, ids);
        dishService.updateStatus(status, ids);
        return R.success("更新状态成功");
    }
    @DeleteMapping
    public R<String > delete(@RequestParam List<Long> ids) {
        log.info("delete idList: {}", ids);
        dishService.removeByIds(ids);
        return R.success("删除成功");
    }
    @GetMapping("/list")
    public R<List<DishDto> >list(Dish dish) {
        List<DishDto> dishDtoList = dishService.getDishDtoListByCondition(dish);
        return R.success(dishDtoList);
    }
}
