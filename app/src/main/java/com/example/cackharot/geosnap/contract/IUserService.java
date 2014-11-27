package com.example.cackharot.geosnap.contract;

import com.example.cackharot.geosnap.model.User;
import com.example.cackharot.geosnap.services.IEntityDownloadCallback;

public interface IUserService {
    void GetUserByEmail(String email, IEntityDownloadCallback<User> callBack);

    void ValidateUser(String email, String password, ILoginCallback callback);
}