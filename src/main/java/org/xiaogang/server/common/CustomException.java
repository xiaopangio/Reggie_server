package org.xiaogang.server.common;

/**
 * className: CustomException
 * description: 业务异常
 * author: xiaopangio
 * date: 2022/7/26 16:28
 * version: 1.0
 */
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
