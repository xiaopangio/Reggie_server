package org.xiaogang.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaogang.server.common.CustomException;
import org.xiaogang.server.entity.Category;
import org.xiaogang.server.entity.Dish;
import org.xiaogang.server.entity.Setmeal;
import org.xiaogang.server.mapper.CategoryMapper;
import org.xiaogang.server.service.CategoryService;
import org.xiaogang.server.service.DishService;
import org.xiaogang.server.service.SetmealService;

/**
 * className: CategoryServiceImpl
 * description:
 * author: xiaopangio
 * date: 2022/7/26 15:30
 * version: 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public boolean remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id).last("limit 1");
        Dish dish = dishService.getOne(dishLambdaQueryWrapper);
        if (dish != null) {
            throw new CustomException("该分类下有菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<Setmeal>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id).last("limit 1");
        Setmeal setmeal = setmealService.getOne(setmealLambdaQueryWrapper);
        if (setmeal != null) {
            throw new CustomException("该分类下有套餐，不能删除");
        }
        return super.removeById(id);
    }
}
