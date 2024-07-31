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
        try {
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
                throw new ShellCommandException("Shell Command 실행 오류", String.join(" ", Arrays.stream(command).toList()));
            }

            return readLines;
        } catch (IOException | InterruptedException e) {
            throw new ShellCommandException("Shell Command IO 예외", String.join(" ", Arrays.stream(command).toList()));
        }
    }
}
