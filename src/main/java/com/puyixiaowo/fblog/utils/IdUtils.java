package com.puyixiaowo.fblog.utils;

/**
 * ID生成工具
 * @author feihong
 * @date 2017-08-10
 */
public class IdUtils {
   private static SnowflakeIdWorker idWorker;

    static {
        idWorker = new SnowflakeIdWorker(0, 0);
    }

    public static Long generateId(){

        return idWorker.nextId();
    }

    public static void main(String[] args) {
        System.out.println(generateId());
    }
}
