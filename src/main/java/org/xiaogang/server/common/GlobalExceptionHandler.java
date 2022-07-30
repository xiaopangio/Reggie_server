package org.xiaogang.server.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * className: GlobalExceptionHandler
 * description:
 * author: xiaopangio
 * date: 2022/7/25 9:49
 * version: 1.0
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    /*
    * 新增员工时，重复id异常
    */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
     public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e) {
        if(e.getMessage().contains("Duplicate entry")){
            String[] split = e.getMessage().split(" ");
            return R.error(split[2]+"已存在");
        }
         return R.error("未知错误");
     }
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException e) {
        log.info("异常信息：{}",e.getMessage());

        return R.error(e.getMessage());
    }
}
