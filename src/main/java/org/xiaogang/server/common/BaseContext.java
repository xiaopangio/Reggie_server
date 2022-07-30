package org.xiaogang.server.common;

/**
 * className: BaseContext
 * description: 基于ThreadLocal实现对登录用户id的保存和获取
 * author: xiaopangio
 * date: 2022/7/26 15:13
 * version: 1.0
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void set(Long employeeId){
        threadLocal.set(employeeId);
    }
    public static Long get(){
       return  threadLocal.get();
    }
}
