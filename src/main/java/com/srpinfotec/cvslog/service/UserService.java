package com.srpinfotec.cvslog.service;

import com.srpinfotec.cvslog.common.CustomException;
import com.srpinfotec.cvslog.domain.User;
import com.srpinfotec.cvslog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void addCvsUser(String name){
        userRepository.findByName(name)
                .ifPresent(user -> {
                    throw new CustomException(user.getName() + " 중복된 CVS 유저");
                });

        User user = new User(name);
        userRepository.save(user);
    }

}
