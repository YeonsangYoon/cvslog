package com.srpinfotec.cvslog.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ShellCommandExecutor {

    // TODO 프로세스 실행 Timeout 기능 추가
    public static List<String> execute(String command){
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (os.contains("win")) {
            processBuilder.command("powershell.exe", "-Command", command);
        } else {
            processBuilder.command("bash", "-c", command);
        }

        try {
            List<String> readLines = new ArrayList<>();

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                readLines.add(line);
            }
            process.waitFor();

            return readLines;
        } catch (IOException | InterruptedException e) {
            throw new CustomException("Shell Command 실행 오류");
        }
    }
}
