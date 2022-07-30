package org.xiaogang.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaogang.server.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
