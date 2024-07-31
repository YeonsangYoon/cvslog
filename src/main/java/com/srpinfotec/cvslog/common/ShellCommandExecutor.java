package com.srpinfotec.cvslog.common;

import com.srpinfotec.cvslog.error.CustomException;
import com.srpinfotec.cvslog.error.ShellCommandException;
import com.srpinfotec.cvslog.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ShellCommandExecutor {

    // TODO 프로세스 실행 Timeout 기능 추가
    public static List<String> execute(String... command){
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder = new ProcessBuilder();

        List<String> commandList = new ArrayList<>();

        if (SystemUtil.currentOs() == OsType.WINDOW) {
            commandList.add("powershell.exe");
            commandList.add("-Command");
        } else {
            commandList.add("bash");
            commandList.add("-c");
        }

        commandList.addAll(Arrays.asList(command));

        processBuilder.command(commandList);

        try {
            List<String> readLines = new ArrayList<>();

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                readLines.add(line);
                log.debug(line);
            }
            process.waitFor();

            return readLines;
        } catch (IOException | InterruptedException e) {
            String cmd = String.join(" ", commandList);

            throw new ShellCommandException("Shell Command 실행 오류");
        }
    }
}
