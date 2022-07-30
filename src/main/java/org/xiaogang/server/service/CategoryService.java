package org.xiaogang.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaogang.server.entity.Category;

public interface CategoryService extends IService<Category> {
    boolean remove(Long id);
}
