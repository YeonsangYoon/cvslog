package com.srpinfotec.cvslog.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OsType {
    WINDOW("win"),
    LINUX("linux");

    private final String name;
}
