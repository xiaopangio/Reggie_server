package org.xiaogang.server.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * className: MyMetaObjectHandler
 * description:元数据对象处理器，用来设置创建时间和更新时间
 * author: xiaopangio
 * date: 2022/7/26 14:43
 * version: 1.0
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("线程id：{}",Thread.currentThread().getId());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long empId = BaseContext.get();
        metaObject.setValue("createUser", empId);
        metaObject.setValue("updateUser", empId );
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("线程id：{}",Thread.currentThread().getId());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long empId = BaseContext.get();
        metaObject.setValue("updateUser", empId );
    }
}
