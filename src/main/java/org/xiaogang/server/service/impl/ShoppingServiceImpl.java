package org.xiaogang.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaogang.server.entity.ShoppingCart;
import org.xiaogang.server.mapper.ShoppingCartMapper;
import org.xiaogang.server.service.ShoppingCartService;

/**
 * className: ShoppingServiceImpl
 * description:
 * author: xiaopangio
 * date: 2022/7/29 15:08
 * version: 1.0
 */
@Service
public class ShoppingServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
