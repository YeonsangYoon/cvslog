package com.srpinfotec.cvslog.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RevisionTypeTest {
    @Test
    void enumTest(){
        System.out.println(RevisionType.valueOf("M"));
    }

}