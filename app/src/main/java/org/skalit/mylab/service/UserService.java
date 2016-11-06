package org.skalit.mylab.service;

import org.skalit.mylab.models.Post;
import org.skalit.mylab.models.User;

import java.util.List;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by pascal on 06/11/16.
 */

public class UserService {
    private final LabUserService userService;

    public UserService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build();

        userService = retrofit.create(LabUserService.class);

    }

    private interface LabUserService {
        @GET("users")
        Observable<List<User>> fetchUsers();

        @GET("posts")
        Observable<List<Post>> fetchUserPost(@Query("userId") int userId);
    }

    public Observable<List<User>> fetchUsers() {
        return userService.fetchUsers();
    }

    public Observable<List<Post>> fetchUserPost(int userId) {
        return userService.fetchUserPost(userId);
    }

}
