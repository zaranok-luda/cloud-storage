package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private HashService hashService;
    @Autowired
    private EncryptionService encryptionService;

    public boolean isUsernameExist(String username) {
        return getUser(username) != null;
    }

    public int createUser(User user) {
        String key = encryptionService.createKey();
        return usersMapper.insert(
                User.builder()
                        .username(user.getUsername())
                        .salt(key)
                        .password(hashService.getHashedValue(user.getPassword(), key))
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build()
        );
    }

    public User getUser(String username) {
        return usersMapper.getUser(username);
    }
}