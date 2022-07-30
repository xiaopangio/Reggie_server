package org.xiaogang.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaogang.server.dto.SetmealDto;
import org.xiaogang.server.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
    Page getPage(int page, int pageSize, String name);
    void removeSetmealWithDish(List<Long> ids);

    void updateStatus(Integer status, List<Long> ids);
}
