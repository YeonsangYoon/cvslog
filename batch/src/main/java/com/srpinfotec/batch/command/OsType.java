package com.srpinfotec.batch.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OsType {
    WINDOW("win"),
    LINUX("linux");

    private final String name;
}
