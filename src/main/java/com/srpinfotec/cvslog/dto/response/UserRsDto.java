package com.srpinfotec.cvslog.dto.response;

import lombok.Data;

@Data
public class UserRsDto {
    private Long userId;
    private String username;

    public UserRsDto(String username) {
        this.username = username;
    }

    public UserRsDto(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
