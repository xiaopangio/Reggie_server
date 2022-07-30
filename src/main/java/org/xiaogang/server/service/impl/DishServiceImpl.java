package org.xiaogang.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiaogang.server.common.CustomException;
import org.xiaogang.server.dto.DishDto;
import org.xiaogang.server.entity.Category;
import org.xiaogang.server.entity.Dish;
import org.xiaogang.server.entity.DishFlavor;
import org.xiaogang.server.mapper.DishMapper;
import org.xiaogang.server.service.CategoryService;
import org.xiaogang.server.service.DishFlavorServiece;
import org.xiaogang.server.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * className: DishServiceImpl
 * description:
 * author: xiaopangio
 * date: 2022/7/26 16:13
 * version: 1.0
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorServiece dishFlavorServiece;

    @Autowired
    private CategoryService categoryService;

    @Override
    @Transactional
    public boolean saveWithFlavor(DishDto dishDto) {
//        保存dish信息
        this.save(dishDto);
//        保存dish与flavor的关系
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        saveDishFlavorsWithDishId(flavors, dishId);
        return true;
    }

    @Override
    public DishDto getDishByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavorList = dishFlavorServiece.list(queryWrapper);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }

    @Override
    public Page<DishDto> getPage(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        this.page(pageInfo, queryWrapper);
//        对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            Category category = categoryService.getById(dish.getCategoryId());
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return dishDtoPage;
    }

    @Override
    @Transactional
    public boolean updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorServiece.remove(queryWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        saveDishFlavorsWithDishId(flavors, dishDto.getId());
        return true;
    }

    @Override
    public boolean updateStatus(Integer status, List<Long> idList) {
//        判断状态是否可以更改
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Dish::getId, idList);
        dishLambdaQueryWrapper.eq(Dish::getStatus, status);
        int count = this.count(dishLambdaQueryWrapper);
        if(count>0){
            throw new CustomException("该菜品已"+(status==1?"启售":"停售")+"，不能重复操作");
        }
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, idList);
        Dish dish = new Dish();
        dish.setStatus(status);
        return this.update(dish, queryWrapper);
    }

    @Override
    public List<DishDto> getDishDtoListByCondition(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = this.list(queryWrapper);
        List<DishDto> dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> flavors = dishFlavorServiece.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());
        return dishDtoList;
    }

    private void saveDishFlavorsWithDishId(List<DishFlavor> flavors, long dishId) {
        List<DishFlavor> list = flavors.stream().map(flavor -> {
            flavor.setDishId(dishId);
            return flavor;
        }).collect(Collectors.toList());
//         保存菜品的口味信息
        dishFlavorServiece.saveBatch(list);
    }


}
