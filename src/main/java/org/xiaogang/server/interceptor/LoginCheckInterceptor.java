package org.xiaogang.server.interceptor;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.xiaogang.server.common.BaseContext;
import org.xiaogang.server.common.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * className: LoginCheckInterceptor
 * description:
 * author: xiaopangio
 * date: 2022/7/25 8:51
 * version: 1.0
 */
@Slf4j
@Component
public class LoginCheckInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截到请求：{}",request.getRequestURI());
        log.info("线程id：{}",Thread.currentThread().getId());
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }
        if(request.getSession().getAttribute("employee") != null) {
            Long employeeId = (Long) request.getSession().getAttribute("employee");
            log.info("用户id：{}",employeeId);
            BaseContext.set(employeeId);
            return true;
        }
        if(request.getSession().getAttribute("user") != null) {
            Long userId = (Long) request.getSession().getAttribute("user");
            log.info("用户id：{}",userId);
            BaseContext.set(userId);
            return true;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return false;
    }

}
