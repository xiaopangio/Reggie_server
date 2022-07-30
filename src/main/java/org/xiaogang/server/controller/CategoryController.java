package org.xiaogang.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaogang.server.common.R;
import org.xiaogang.server.entity.Category;
import org.xiaogang.server.service.CategoryService;

import java.util.List;

/**
 * className: CategoryController
 * description:
 * author: xiaopangio
 * date: 2022/7/26 15:31
 * version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @DeleteMapping
    public R<String>delete(Long ids){
        log.info("删除分类id：{}",ids);
        categoryService.remove(ids);
        return R.success("删除分类成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("更新分类成功");
    }
    @GetMapping("/list")
    public R<List<Category>>list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(queryWrapper);
        return R.success(categoryList);
    }


}
