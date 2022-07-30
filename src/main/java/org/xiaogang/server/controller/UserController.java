package org.xiaogang.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaogang.server.common.BaseContext;
import org.xiaogang.server.common.R;
import org.xiaogang.server.entity.User;
import org.xiaogang.server.service.UserService;
import org.xiaogang.server.utils.SMSUtils;
import org.xiaogang.server.utils.ValidateCodeUtils;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * className: UserController
 * description:
 * author: xiaopangio
 * date: 2022/7/27 17:43
 * version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String > sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            String validateCode = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码：{}",validateCode);
//            SMSUtils.sendMessage("瑞吉外卖","",phone,validateCode);
            session.setAttribute(phone,validateCode);
            return R.success("短息验证码发送成功");
        }
        return R.error("短信验证码发送失败");
    }
    @PostMapping("/login")
    public R<User>login(@RequestBody Map u,HttpSession session){
        String  phone = (String) u.get("phone");
        String  code = (String) u.get("code");
        String  validateCode = session.getAttribute(phone).toString();
        if(validateCode!=null&&validateCode.equals(code)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user==null){
                user=new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            BaseContext.set(user.getId());
            return R.success(user);
        }
        return R.error("短信验证码错误");
    }

}
