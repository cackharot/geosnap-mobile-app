package com.example.cackharot.geosnap.services;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.cackharot.geosnap.contract.ILoginCallback;
import com.example.cackharot.geosnap.contract.IUserService;
import com.example.cackharot.geosnap.db.UserRepository;
import com.example.cackharot.geosnap.lib.ConfigurationHelper;
import com.example.cackharot.geosnap.lib.GsonHelper;
import com.example.cackharot.geosnap.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserService extends BaseService<User> implements IUserService {
    private static final String fetchUrl = ConfigurationHelper.BaseUrl + "/users";
    private static final String loginUrl = ConfigurationHelper.LoginUrl;
    private final UserRepository repository;
    private final Gson gson;
    private Type userArrayType = new TypeToken<Collection<User>>() {
    }.getType();

    public UserService(Context context) {
        super(context);
        this.repository = new UserRepository(context);
        this.gson = GsonHelper.getGson();
        checkAndAndDefaultUser();
    }

    private void checkAndAndDefaultUser() {
        Integer count = this.repository.getUserCount();
        if (count == 0) {
            User entity = new User();
            entity.name = "john";
            entity.api_key = ConfigurationHelper.Api_key;
            entity.password = "pass@123";
            entity.email = "john@example.com";
            entity.security_question = "What is you first pet name?";
            entity.security_answer = "Tiger";
            entity.created_at = new Date();
            entity.status = true;
            this.repository.Create(entity);
        }
    }

    public void GetUserByEmail(String email, IEntityDownloadCallback<User> callback) {
        HashMap<String, String> queryArgs = new HashMap<String, String>();
        queryArgs.put("email", email);
        doHttpRequest(fetchUrl, queryArgs, null, new IDownloadCallBack<User>() {
            @Override
            public void doPostExecute(String results, IEntityDownloadCallback<User> innerCallback) {
                if (results == null) {
                    innerCallback.doAfterGet(null);
                }
                try {
                    List<User> items = gson.fromJson(results, userArrayType);
                    if (items != null && !items.isEmpty()) {
                        innerCallback.doAfterGet(items.get(0));
                    } else {
                        innerCallback.doAfterGet(null);
                    }
                } catch (Exception ignored) {
                    Log.e("SiteService", ignored.getLocalizedMessage());
                }
            }
        }, callback);
    }

    public void ValidateUser(String email, String password, final ILoginCallback callback) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            callback.onValidate(false);
        }

        doOnlineAuthentication(email, password, new IEntityDownloadCallback<User>() {
            @Override
            public void doAfterGetAll(Collection<User> results) {

            }

            @Override
            public void doAfterCreate(User entity) {

            }

            @Override
            public void doAfterGet(User item) {
                boolean isValid = item != null && item.api_key != null && !item.api_key.isEmpty();
                if (isValid) {
                    userSessionManager.setApiKey(item.api_key);
                }
                callback.onValidate(isValid);
            }
        });
    }

    private void doOnlineAuthentication(String email, String password, IEntityDownloadCallback<User> callback) {
        String post_data = gson.toJson(new LoginRequest(email, password));

        doHttpRequest(loginUrl, null, post_data, new IDownloadCallBack<User>() {
            @Override
            public void doPostExecute(String results, IEntityDownloadCallback<User> innerCallback) {
                if (results == null) {
                    innerCallback.doAfterGet(null);
                }
                try {
                    UserLoginResponse item = gson.fromJson(results, UserLoginResponse.class);
                    if (item.status.equals("success")) {
                        User user = new User();
                        user.api_key = item.api_key;
                        innerCallback.doAfterGet(user);
                    } else {
                        Log.i("UserLogin", item.message);
                        innerCallback.doAfterGet(null);
                    }
                } catch (Exception ex) {
                    if (ex.getMessage() != null) {
                        Log.e("UserLogin", ex.getMessage());
                    }
                    innerCallback.doAfterGet(null);
                }
            }
        }, callback);
    }

    private class UserLoginResponse {
        public Object _id;
        public String status;
        public String api_key;
        public String message;
    }

    private class LoginRequest {
        public final String email;
        public final String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}
