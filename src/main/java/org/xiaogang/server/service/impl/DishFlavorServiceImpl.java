package org.xiaogang.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaogang.server.entity.DishFlavor;
import org.xiaogang.server.mapper.DishFlavorMapper;
import org.xiaogang.server.service.DishFlavorServiece;

/**
 * className: DishFlavorServiceImpl
 * description:
 * author: xiaopangio
 * date: 2022/7/26 19:43
 * version: 1.0
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorServiece {
}
