package com.srpinfotec.batch.job.mapper;

import com.srpinfotec.batch.job.dto.LogBuffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CvsLogUtil {
    public static String getCommitMessageFromLog(long version, Iterator<String> logs){
        List<LogBuffer> logBuffers = new ArrayList<>();

        while(logs.hasNext()){
            String line = logs.next();

            if(line.startsWith("revision")){
                LogBuffer logBuffer = new LogBuffer();

                logBuffer.setVersionLine(line);
                logBuffer.setUpdateInfoLine(logs.next());

                StringBuffer msg = new StringBuffer();
                while(logs.hasNext()){
                    String next = logs.next();

                    if(next.equals("----------------------------") || next.equals("=============================================================================")){
                        break;
                    }
                    msg.append(next).append(System.lineSeparator());
                }
                logBuffer.setMessage(msg.toString());

                logBuffers.add(logBuffer);
            }
        }

        for (LogBuffer logBuffer : logBuffers) {
            if(logBuffer.getVersion() == version){
                return logBuffer.getMessage();
            }
        }

        return null;
    }
}
