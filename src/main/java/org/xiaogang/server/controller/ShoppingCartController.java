package org.xiaogang.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaogang.server.common.BaseContext;
import org.xiaogang.server.common.R;
import org.xiaogang.server.entity.ShoppingCart;
import org.xiaogang.server.service.ShoppingCartService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * className: ShoppingCartController
 * description:
 * author: xiaopangio
 * date: 2022/7/29 15:09
 * version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController
{
    @Autowired
    private ShoppingCartService shoppingCartService;

    /*
     *@Description: 添加菜品或者套餐到购物车
     * @param shoppingCart:
     * @return: org.xiaogang.server.common.R<org.xiaogang.server.entity.ShoppingCart>
     **/
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        //获取用户id
        Long id = BaseContext.get();
        shoppingCart.setUserId(id);
        //判断添加的是菜品还是套餐
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, id);
        if(dishId != null){
            //是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        //判断购物车中是否存在该菜品或者套餐
        if(cart!=null){
            //存在，且菜品口味一致
            cart.setNumber(cart.getNumber()+1);
            shoppingCartService.updateById(cart);
        }else{
            //不存在，添加到购物车
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cart=shoppingCart;
        }
        return R.success(cart);
    }
    /*
     *@Description: 对购物车中的菜品或者套餐进行数量的减少
     * @return: org.xiaogang.server.common.R<java.lang.String>
     **/
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<ShoppingCart>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.get());
        if(dishId!=null){
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        if(cart.getNumber()>1){
            //菜品数量大于1，减少一个
            cart.setNumber(cart.getNumber()-1);
            shoppingCartService.updateById(cart);
        }else{
            //菜品数量等于1，删除该菜品
            shoppingCartService.removeById(cart.getId());
        }
        return R.success(cart);
    }

    /*
     *@Description: 查看购物车
     * @return: org.xiaogang.server.common.R<java.util.List<org.xiaogang.server.entity.ShoppingCart>>
     **/
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.get());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.get());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }


}
