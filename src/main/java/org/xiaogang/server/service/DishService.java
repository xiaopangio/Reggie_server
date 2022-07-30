package org.xiaogang.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaogang.server.dto.DishDto;
import org.xiaogang.server.entity.Category;
import org.xiaogang.server.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    boolean saveWithFlavor(DishDto dishDto);
    DishDto getDishByIdWithFlavor(Long id);
    Page<DishDto> getPage(int page, int pageSize, String name);
    boolean updateWithFlavor(DishDto dishDto);
    boolean updateStatus(Integer status, List<Long> idList);
    List<DishDto> getDishDtoListByCondition(Dish dish);
}
