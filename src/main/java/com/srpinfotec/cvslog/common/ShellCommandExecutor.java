package com.srpinfotec.cvslog.common;

import com.srpinfotec.cvslog.error.ShellCommandException;
import com.srpinfotec.cvslog.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShellCommandExecutor {

    // TODO 프로세스 실행 Timeout 기능 추가
    public static List<String> execute(String command){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();

            List<String> commandList = new ArrayList<>();

            if (SystemUtil.currentOs() == OsType.WINDOW) {
                processBuilder.command("powershell.exe", "-Command", command);
            } else {
                processBuilder.command("bash", "-c", command);
            }

            processBuilder.redirectErrorStream(true);

            List<String> readLines = new ArrayList<>();

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                readLines.add(line);
                log.debug(line);
            }

            int exitCode = process.waitFor();
            if(exitCode != 0){
                throw new ShellCommandException("Shell Command 실행 오류", command);
            }

            return readLines;
        } catch (IOException | InterruptedException e) {
            throw new ShellCommandException("Shell Command IO 예외", command);
        }
    }
}
