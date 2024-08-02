package com.srpinfotec.cvslog.dto.response;

import lombok.Data;

@Data
public class UserRsDto {
    private String username;

    public UserRsDto(String username) {
        this.username = username;
    }
}
