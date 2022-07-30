package org.xiaogang.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaogang.server.common.R;
import org.xiaogang.server.dto.SetmealDto;
import org.xiaogang.server.entity.Setmeal;
import org.xiaogang.server.entity.SetmealDish;
import org.xiaogang.server.service.SetmealDishService;
import org.xiaogang.server.service.SetmealService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * className: SetmealController
 * description:
 * author: xiaopangio
 * date: 2022/7/27 14:50
 * version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @PostMapping
    /*
     *@Description: 新增套餐
     * @return: org.xiaogang.server.common.R<java.lang.String>
     **/
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page pageInfo = setmealService.getPage(page, pageSize, name);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String>delete(@RequestParam List<Long> ids){
        setmealService.removeSetmealWithDish(ids);
        return R.success("删除套餐成功");
    }
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {
        log.info("update status: {}, idList: {}", status, ids);
        setmealService.updateStatus(status, ids);
        return R.success("更新状态成功");
    }
    /*
     *@Description: 根据条件获取套餐列表
     * @param setmeal:
     * @return: org.xiaogang.server.common.R<java.util.List<org.xiaogang.server.dto.SetmealDto>>
     **/
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!= null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!= null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}
