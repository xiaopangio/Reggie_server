package org.xiaogang.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaogang.server.common.R;
import org.xiaogang.server.entity.Orders;
import org.xiaogang.server.service.OrderDetailService;
import org.xiaogang.server.service.OrdersService;

/**
 * className: OrderController
 * description:
 * author: xiaopangio
 * date: 2022/7/29 16:09
 * version: 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    /*
     *@Description: 用户下单
     * @param orders:
     * @return: org.xiaogang.server.common.R<java.lang.String>
     **/
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }
}
