package com.example.myapplication;

import static com.example.myapplication.CouponItem.purchaseArrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
    private static final String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

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

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    Log.i("user", oAuthToken.getAccessToken() + " " + oAuthToken.getRefreshToken());
                }
                if (throwable != null) {
                    // TBD
                    Log.w(TAG, "invoke: " + throwable.getLocalizedMessage());
                }
                updateKakaoLoginUi();

                return null;
            }
        };

        ImageButton kakaoAuth = findViewById(R.id.kakaoLogin);

        kakaoAuth.setOnClickListener(new View.OnClickListener() {   // 로그인 버튼 클릭 시
            @Override
            public void onClick(View v) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(InitActivity.this)) {
                    // 카카오톡이 있을 경우?
                    UserApiClient.getInstance().loginWithKakaoTalk(InitActivity.this, callback);
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(InitActivity.this, callback);
                }
            }
        });
        updateKakaoLoginUi();
    }

    public void updateKakaoLoginUi() {
        // 카카오 UI 가져오는 메소드 (로그인 핵심 기능)
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {

            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null) {
                    // 유저 정보가 정상 전달 되었을 경우
                    Log.i(TAG, "id " + user.getId());   // 유저의 고유 아이디를 불러옵니다.
                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());  // 유저의 닉네임을 불러옵니다.
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    finishAffinity();
                    intent.putExtra("id", user.getKakaoAccount().getProfile().getNickname()); /*송신*/
                    startActivity(intent);
                    // 이 부분에는 로그인이 정상적으로 되었을 경우 어떤 일을 수행할 지 적으면 됩니다.
                }
                if (throwable != null) {
                    // 로그인 시 오류 났을 때
                    // 키해시가 등록 안 되어 있으면 오류 납니다.
                    Log.w(TAG, "invoke: " + throwable.getLocalizedMessage());
                }
                return null;
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
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