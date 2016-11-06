package org.skalit.mylab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.skalit.mylab.models.Post;
import org.skalit.mylab.models.User;
import org.skalit.mylab.service.UserService;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "ObserverLab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUsers();
    }


    List<User> getUsers() {

        final UserService userService = new UserService();

        Observable<List<Post>> observablePosts = userService.fetchUsers()
                .flatMap(new Func1<List<User>, Observable<List<Post>>>() {

                    @Override
                    public Observable<List<Post>> call(List<User> users) {

                        return Observable.from(users).concatMap(new Func1<User, Observable<? extends List<Post>>>() {
                            @Override
                            public Observable<? extends List<Post>> call(User user) {
                                return userService.fetchUserPost(user.getId());
                            }
                        });

                    }

                });

        observablePosts.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Post>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "no more posts");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        for (Post post : posts) {
                            Log.d(TAG, "Post fetch " + post.getId() + " | " + post.getTitle());
                        }
                    }
                });
        List<User> userList = null;

        return userList;
    }

}
