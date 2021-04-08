package top.enatsu.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTool {
    public static Boolean isPathLegal(String path) {
        if (path.contains("..") || path.contains("?") || path.contains("*") || path.contains(":")
                || path.contains("<") || path.contains(">"))
            return false;
        return true;
    }

    public static Boolean isFileNameLegal(String fileName) {
        if (fileName.contains("/") || fileName.contains("\\")) return false;
        return isPathLegal(fileName);
    }

    private static String fluidLeft(String str, int maxLen) {
        String newStr = "";
        for (int i = 0; i < maxLen - str.length(); i++) {
            newStr += " ";
        }
        return newStr + str;
    }

    public static Boolean printFileInfo(File[] files) {
        if(files==null) return false;
        System.out.println(files.length);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int maxLen = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().length() > maxLen) {
                maxLen = files[i].getName().length();
            }
        }
        for (int i = 0; i < files.length; i++) {
            Date date = new Date(files[i].lastModified());

            System.out.println(fluidLeft(files[i].getName(), maxLen) + '\t'
                    + (files[i].isDirectory()) + '\t'
                    + files[i].getParent() + '\t'
                    + files[i].length() + '\t'
                    + sdf.format(date));
        }
        return true;
    }
}
