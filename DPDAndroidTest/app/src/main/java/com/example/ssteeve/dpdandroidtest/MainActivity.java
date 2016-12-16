package com.example.ssteeve.dpdandroidtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ssteeve.dpd_android.DPDObject;
import com.example.ssteeve.dpd_android.DPDUser;
import com.example.ssteeve.dpd_android.MappableResponseCallBack;

import org.json.JSONException;

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
    }

    void login() {
        try {
            DPDUser.login("userss" + "/login", "steevensylveus@gmail.com", "mvbe26", User.class, new MappableResponseCallBack() {
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

}
