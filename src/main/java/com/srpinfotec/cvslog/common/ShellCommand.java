package com.srpinfotec.cvslog.common;

import com.srpinfotec.cvslog.util.SystemUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cvs.command")
public class ShellCommand {
    private final String recentHistory;
    private final Window window;
    private final Linux linux;

    public ShellCommand(String recentHistory, Window window, Linux linux) {
        this.window = window;
        this.linux = linux;

        if(SystemUtil.currentOs() == OsType.WINDOW){
            this.recentHistory = window.recentHistory;
        } else {
            this.recentHistory = linux.recentHistory;
        }
    }


    @Getter
    @RequiredArgsConstructor
    public static class Window{
        private final String recentHistory;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Linux{
        private final String recentHistory;
    }
}
