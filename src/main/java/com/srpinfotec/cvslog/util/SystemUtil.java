package com.srpinfotec.cvslog.util;

import com.srpinfotec.cvslog.common.OsType;

public class SystemUtil {
    public static OsType currentOs(){
        String os = System.getProperty("os.name").toLowerCase();

        if(os.contains("win")){
            return OsType.WINDOW;
        } else {
            return OsType.LINUX;
        }
    }
}
