package org.xiaogang.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaogang.server.entity.AddressBook;

/**
 * className: AddressBookMapper
 * description:
 * author: xiaopangio
 * date: 2022/7/29 8:18
 * version: 1.0
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
