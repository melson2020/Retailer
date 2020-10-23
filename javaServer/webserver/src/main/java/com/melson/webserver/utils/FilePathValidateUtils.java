package com.melson.webserver.utils;

import java.io.File;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/21
 */
public class FilePathValidateUtils {
    /**
     * 检查文件夹是否存在，若不存在则创建
     * @param path
     * @return
     */
    public boolean validateFolderPath(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) file.mkdirs();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查文件是否重名，如重名自动添加（数字）
     * @param path
     * @param fileType
     * @return
     */
    public String ValidateFilePath(String path, String fileType) {
        File file = new File(path + fileType);
        int count = 1;
        while (file.exists()) {
            file = new File(path +"("+count+")"+ fileType);
            count++;
        }
        return count>1?path +"("+(count-1)+")"+ fileType:path+fileType;
    }
}
