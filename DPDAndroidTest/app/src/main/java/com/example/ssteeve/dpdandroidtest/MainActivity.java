package com.example.ssteeve.dpdandroidtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ssteeve.dpd_android.DPDObject;
import com.example.ssteeve.dpd_android.DPDQuery;
import com.example.ssteeve.dpd_android.DPDUser;
import com.example.ssteeve.dpd_android.MappableResponseCallBack;
import com.example.ssteeve.dpd_android.QueryCondition;
import com.example.ssteeve.dpd_android.ResponseCallBack;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login();

        //createUser();

        //updateUser();

        //logout();

    }


    void login() {
        try {
            DPDUser.login("users", "dpd-android", "dpd-android", User.class, new MappableResponseCallBack() {
                @Override
                public void onResponse(List<DPDObject> response) {
                    if (response != null) {
                        users = (List<User>)(List<?>) response;
                    }
                }

                @Override
                public void onFailure(Call call, Response response, Exception e) {
                    Log.d(this.getClass().getSimpleName(), "error occured");
                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void createUser() {
        try {
            DPDUser.createUser("users", "dpdAndroid", "dpdAndroi", User.class, new MappableResponseCallBack() {
                @Override
                public void onResponse(List<DPDObject> response) {
                    Log.d(this.getClass().getSimpleName(), "User created successfully");
                }

                @Override
                public void onFailure(Call call, Response response, Exception e) {
                    Log.d(this.getClass().getSimpleName(), "Failed to create user");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void updateUser() {
        try {
            User user = (User) DPDUser.getInstance().currentUser(User.class);
            user.firstName = "John";
            user.lastName = "Doe";

            user.updateObject("users", User.class, new MappableResponseCallBack() {
                @Override
                public void onResponse(List<DPDObject> response) {
                    Log.d(this.getClass().getSimpleName(), "User updated successfully");
                }

                @Override
                public void onFailure(Call call, Response response, Exception e) {
                    Log.d(this.getClass().getSimpleName(), "Failed to update user");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    void loadStroe() {
        DPDQuery query = new DPDQuery(QueryCondition.LESS_THAN, null, null, null, "zip", "60012", null);
        query.findMappableObject("stores", Store.class, new MappableResponseCallBack() {
            @Override
            public void onResponse(List<DPDObject> response) {
                Log.d(this.getClass().getSimpleName(), "response");
            }

            @Override
            public void onFailure(Call call, Response response, Exception e) {
                Log.d(this.getClass().getSimpleName(), "error occured");
            }

        });
    }

    void logout() {
        DPDUser.logout(new ResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(this.getClass().getSimpleName(), response);
            }

            @Override
            public void onFailure(Call call, Response response, Exception e) {
                Log.d(this.getClass().getSimpleName(), "Failed to logout");
            }
        });
    }

}
