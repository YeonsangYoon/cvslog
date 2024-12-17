package com.srpinfotec.core.value;

import lombok.Getter;

@Getter
public enum RevisionType {
    ADD("A"),
    MODIFY("M"),
    REMOVE("R");

    private final String code;

    RevisionType(String code) {
        this.code = code;
    }

    public static RevisionType getType(String code){
        if(code.equalsIgnoreCase("A")){
            return RevisionType.ADD;
        } else if(code.equalsIgnoreCase("M")){
            return RevisionType.MODIFY;
        } else if(code.equalsIgnoreCase("R")){
            return RevisionType.REMOVE;
        }
        return null;
    }
}
