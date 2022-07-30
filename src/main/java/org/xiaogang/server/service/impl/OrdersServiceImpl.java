package org.xiaogang.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiaogang.server.common.BaseContext;
import org.xiaogang.server.common.CustomException;
import org.xiaogang.server.entity.*;
import org.xiaogang.server.mapper.OrdersMapper;
import org.xiaogang.server.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * className: OrdersServiceImpl
 * description:
 * author: xiaopangio
 * date: 2022/7/29 16:07
 * version: 1.0
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Override
    @Transactional
    public void submit(Orders orders) {
        //获取用户id
        Long userId = BaseContext.get();
        //获取该用户的购物车信息
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        if(list==null||list.size()==0){
            throw new CustomException("购物车为空");
        }
        //查询用户数据
        User user = userService.getById(userId);
        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("地址不存在");
        }
        //生成订单号
        long orderId = IdWorker.getId();
        //总价格
        AtomicInteger amount = new AtomicInteger(0);
        //获得订单详情列表
        List<OrderDetail> orderDetailList = list.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setName(item.getName());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        //设置订单信息
        orders.setNumber(String.valueOf(orderId));
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        orders.setPhone(user.getPhone());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAddress((addressBook.getProvinceName() == null ?"": addressBook.getProvinceName())
                        +(addressBook.getCityName()==null?"":addressBook.getCityName())
                        +(addressBook.getDistrictName()==null?"":addressBook.getDistrictName())
                        +(addressBook.getDetail()==null?"":addressBook.getDetail()));
        orders.setStatus(2);
        orders.setConsignee(addressBook.getConsignee());
        orders.setAmount(new BigDecimal(amount.get()));
        //向order表中插入一条订单信息
        this.save(orders);
        //向order_detail表中插入多条订单详情信息
        orderDetailService.saveBatch(orderDetailList);
        //清空购物车信息
        shoppingCartService.remove(queryWrapper);
    }
}
