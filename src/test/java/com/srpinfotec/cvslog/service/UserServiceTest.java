package com.srpinfotec.cvslog.service;

import com.srpinfotec.cvslog.common.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService service;

    @Test
    public void 중복생성(){
        service.addCvsUser("tom");

        assertThrows(CustomException.class, () -> {
            service.addCvsUser("tom");
        });
    }
}