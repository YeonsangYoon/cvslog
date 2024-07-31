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

    public List<String> executeHistoryCommand(){
        if(SystemUtil.currentOs() == OsType.WINDOW){
            return execute("type", ".\\src\\main\\resources\\commitLog.txt");
        } else {
            return execute("cvs", "-d", cvsProperties.getRoot(), "-a -x AMR -D", LocalDate.now().minusDays(1).toString());
        }
    }
}
