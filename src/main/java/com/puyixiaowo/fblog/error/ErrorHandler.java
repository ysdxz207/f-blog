package com.puyixiaowo.fblog.error;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ErrorHandler {

    public static String init(){
        try {
            return FileUtils.readFileToString(new File("D:\\workspace\\idea\\f-blog\\web\\error\\404.html"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "404";
        }
    }
}
