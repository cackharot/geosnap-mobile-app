package com.example.cackharot.geosnap.services;

import android.text.TextUtils;

import com.example.cackharot.geosnap.contract.IUserService;

public class UserService implements IUserService {

    @Override
    public boolean validateUser(String userName, String password) {
        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
            return false;
        }
        return userName.equals("test") && password.equals("test");
    }
}
