package org.xiaogang.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiaogang.server.common.CustomException;
import org.xiaogang.server.dto.SetmealDto;
import org.xiaogang.server.entity.Category;
import org.xiaogang.server.entity.Dish;
import org.xiaogang.server.entity.Setmeal;
import org.xiaogang.server.entity.SetmealDish;
import org.xiaogang.server.mapper.SetmealMapper;
import org.xiaogang.server.service.CategoryService;
import org.xiaogang.server.service.SetmealDishService;
import org.xiaogang.server.service.SetmealService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * className: SetmealServiceImpl
 * description:
 * author: xiaopangio
 * date: 2022/7/26 16:14
 * version: 1.0
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    /*
     *@Description: 新增套餐
     *@param setmealDto: 
     * @return: void
     **/
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes = dishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
    }

    @Override
    public Page getPage(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        this.page(pageInfo, queryWrapper);
        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtoList);
        return setmealDtoPage;
    }

    @Override
    @Transactional
    public void removeSetmealWithDish(List<Long> ids) {
//        查询套餐状态，判断是否可以删除
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(setmealLambdaQueryWrapper);
        if(count > 0){
//            不能删除，抛出异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }
//        可以删除
//        删除setmeal数据
        this.removeByIds(ids);
//        删除setmeal_dish数据
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper);
    }

    @Override
    public void updateStatus(Integer status, List<Long> idList) {
//        判断状态是否可以更改
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, idList);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus, status);
        int count = this.count(setmealLambdaQueryWrapper);
        if(count>0){
            throw new CustomException("该套餐已"+(status==1?"启售":"停售")+"，不能重复操作");
        }
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, idList);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        this.update(setmeal, queryWrapper);
    }
}
