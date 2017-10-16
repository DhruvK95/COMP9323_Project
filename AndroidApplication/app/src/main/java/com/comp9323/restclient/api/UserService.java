package com.comp9323.restclient.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.comp9323.data.DataHolder;
import com.comp9323.data.beans.User;
import com.comp9323.main.R;
import com.comp9323.restclient.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserService {
    private static final String TAG = "UserService";

    private static final UserApi api = RestClient.getClient().create(UserApi.class);

//    public static boolean postUser(String username) {
//        return postUser(new User(username, createUUID()));
//    }

//    public static boolean deleteUser() {
//        return deleteUser(DataHolder.getInstance().getUser().getId());
//    }
//
//    public static boolean putUser(HashMap<String, String> couples) {
//        //load user
//        User userSelf = DataHolder.getInstance().getUser();
//        //set fields
//        for (String item : couples.keySet()) {
//            String value = couples.get(item);
//            if (item.equals("username")) {
//                userSelf.setUsername(value);
//            } else if (item.equals("deviceid")) {
//                userSelf.setDeviceId(value);
//            }
//        }
//
//        return putUser(userSelf.getId(), userSelf);
//    }
//
//    public static boolean patchUser(HashMap<String, String> couples) {
//        //create new user template
//        int Id = DataHolder.getInstance().getUser().getId();
//        User templateUser = new User();
//        for (String item : couples.keySet()) {
//            String value = couples.get(item);
//            if (item.equals("username")) {
//                templateUser.setUsername(value);
//            } else if (item.equals("deviceid")) {
//                templateUser.setDeviceId(value);
//            }
//        }
//
//        return patchUser(Id, templateUser);
//    }

    public static void getUser(int id, Callback<User> callback) {
        api.getUser(id).enqueue(callback);
    }

    public static void getUsers(Callback<List<User>> callback) {
        api.getUsers().enqueue(callback);
    }

    public static void postUser(User user, Callback<User> callback) {
        user.setDeviceId(createUUID());
        api.postUser(user).enqueue(callback);
    }

    public static void deleteUser(int id, Callback<Void> callback) {
        api.deleteUser(id).enqueue(callback);
    }

    public static void putUser(int id, User user, Callback<User> callback) {
        api.putUser(id, user).enqueue(callback);
    }

    public static void patchUser(int id, User user, Callback<User> callback) {
        api.patchUser(id, user).enqueue(callback);
    }

    private static String createUUID() {
        return UUID.randomUUID().toString();
    }
}
