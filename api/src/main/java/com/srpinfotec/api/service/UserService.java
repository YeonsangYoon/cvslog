package com.srpinfotec.api.service;

import com.srpinfotec.api.dto.response.UserRsDto;
import com.srpinfotec.api.exception.ApiException;
import com.srpinfotec.core.repository.UserRepository;
import com.srpinfotec.api.repository.UserQueryRepository;
import com.srpinfotec.core.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

    @Transactional
    public void addCvsUser(String name){
        userRepository.findByName(name)
                .ifPresent(user -> {
                    throw new ApiException(user.getName() + " 중복된 CVS 유저");
                });

        User user = new User(name);
        userRepository.save(user);
    }

    @Transactional
    public List<UserRsDto> getAllUser(){
        return userQueryRepository.findAllDtos();
    }
}
