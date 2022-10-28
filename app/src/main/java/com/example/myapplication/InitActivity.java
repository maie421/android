package com.example.myapplication;

import static com.example.myapplication.CouponItem.purchaseArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class InitActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences preferences_auto;
    private static final String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        preferences_auto = getSharedPreferences("AutoUser", MODE_PRIVATE);

        Button joinBtn = findViewById(R.id.join);
        joinBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
            startActivity(intent);
        });


        Button loginBtn = findViewById(R.id.login);
        loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        Function2<OAuthToken, Throwable, Unit> callback = (token, error) -> {
            //로그인 성공
            if (token != null) {
                Log.i("user", token.getAccessToken() + " " + token.getRefreshToken());
                updateKakaoLoginUi();
            }
            if (error != null) {
                Log.w(TAG, "invoke: " + error.getLocalizedMessage());
            }

            return null;
        };

        ImageButton kakaoAuth = findViewById(R.id.kakaoLogin);

        kakaoAuth.setOnClickListener(new View.OnClickListener() {   // 로그인 버튼 클릭 시
            @Override
            public void onClick(View v) {
                // 카카오톡이 있을 경우
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(InitActivity.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(InitActivity.this, callback);
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(InitActivity.this, callback);
                }
            }
        });

    }

    // 카카오 ui
    public void updateKakaoLoginUi() {
        UserApiClient.getInstance().me((user, throwable) -> {
            //성공
            if (user != null) {
                SharedPreferences.Editor editor_auto = preferences_auto.edit();
                editor_auto.putString("login_id",user.getKakaoAccount().getProfile().getNickname());
                editor_auto.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finishAffinity();
                intent.putExtra("id", user.getKakaoAccount().getProfile().getNickname()); /*송신*/
                startActivity(intent);
            }
            if (throwable != null) {
                Log.w(TAG, "invoke: " + throwable.getLocalizedMessage());
            }
            return null;
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        //위치 권한 체크
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        //자동 로그인
        preferences = getSharedPreferences("AutoUser", MODE_PRIVATE);
        if (preferences.getString("login_id", null) != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finishAffinity();
            intent.putExtra("id", preferences.getString("login_id", null)); /*송신*/
            startActivity(intent);
            return;
        }
    }
}