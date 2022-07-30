package org.xiaogang.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaogang.server.entity.AddressBook;
import org.xiaogang.server.mapper.AddressBookMapper;
import org.xiaogang.server.service.AddressBookService;

/**
 * className: AddressBookServiceImpl
 * description:
 * author: xiaopangio
 * date: 2022/7/29 8:19
 * version: 1.0
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook>  implements AddressBookService {

}
