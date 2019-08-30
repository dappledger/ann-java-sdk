package com.genesis.api.util;

public class OsUtil {
    /**
     * 判断当前操作是否Windows.
     * @return true---是Windows操作系统
     */
    public static boolean isWindowsOS(){
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if(osName.toLowerCase().contains("windows")){
            isWindowsOS = true;
        }
        return isWindowsOS;
    }
}
