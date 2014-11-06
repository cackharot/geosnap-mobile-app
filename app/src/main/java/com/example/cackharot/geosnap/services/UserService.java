package com.example.cackharot.geosnap.services;

import android.content.Context;
import android.text.TextUtils;

import com.example.cackharot.geosnap.contract.IUserService;
import com.example.cackharot.geosnap.db.UserRepository;
import com.example.cackharot.geosnap.model.User;

import java.util.Date;

public class UserService implements IUserService {

    private final Context context;
    private final UserRepository repository;

    public UserService(Context context) {
        this.context = context;
        this.repository = new UserRepository(this.context);

        checkAndAndDefaultUser();
    }

    private void checkAndAndDefaultUser() {
        Integer count = this.repository.getUserCount();
        if(count == 0){
            User entity = new User();
            entity.Name = "john";
            entity.Password = "pass@123";
            entity.Email = "john@example.com";
            entity.SecurityQuestion = "What is you first pet name?";
            entity.SecurityAnswer = "Tiger";
            entity.CreatedDate = new Date();
            entity.Status = true;
            this.repository.Create(entity);
        }
    }

    @Override
    public boolean validateUser(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return false;
        }
        return this.repository.ValidateUser(username, password);
    }
}
