package com.example.factory;

import com.example.annotation.Cumponent;
import com.example.facade.UserFacade;
import com.example.model.User;

@Cumponent
public class UserFactory {

    public User create(Long id, UserFacade facade) {
        return User.newInstance(id, facade.username(), facade.displayName());
    }
}
