package com.srpinfotec.cvslog.common;

import com.srpinfotec.cvslog.util.SystemUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CvsCommandExecutor extends ShellCommandExecutor{
    private final CVSProperties cvsProperties;

    public List<String> executeHistoryCommand(int dayBefore){
        String command;
        if(SystemUtil.currentOs() == OsType.WINDOW){
            command = "type .\\src\\main\\resources\\commitLog.txt";
        } else {
            command = "cvs -d " + cvsProperties.getRoot() + " history -a -x AMR -D " + LocalDate.now().minusDays(dayBefore);
        }

        return execute(command);
    }

    public List<String> executeHistoryCommand(){
        return executeHistoryCommand(1);
    }


}
