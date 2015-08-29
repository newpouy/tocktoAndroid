package com.plainit.tockto;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kakao.auth.APIErrorResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.auth.SessionCallback;
import com.kakao.kakaotalk.KakaoTalkService;
import com.kakao.usermgmt.LogoutResponseCallback;
import com.kakao.usermgmt.MeResponseCallback;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.plainit.tockto.model.User;
import com.plainit.tockto.util.UseServerREST;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MA";
    Session kakao_session;
    SessionCallback kakao_sessionCallBack;

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.email_btn)
    Button emailBtn;
    @Bind(R.id.fb_btn)
    Button fbBtn;
    @Bind(R.id.kakao_btn)
    Button kakaoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Session.initialize(this);
        //kakao
        kakao_session = Session.getCurrentSession();
        kakao_sessionCallBack = new MySessionStatusCallback();
        kakao_session.addCallback(kakao_sessionCallBack);

        if (kakao_session.isOpened()){
            Log.d(TAG, "session is opened");
            onSessionOpened();
        } else {
            Log.d(TAG, "session is closed");
        }

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "hehe");
                Toast.makeText(getApplicationContext(), email.getText(), Toast.LENGTH_LONG).show();


            }
        });
        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        kakaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getCurrentSession().open(AuthType.KAKAO_TALK, MainActivity.this);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        kakao_session.removeCallback(kakao_sessionCallBack);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(kakao_session.isClosed()){
            return false;
        }
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onSuccess(long l) {
                        finish();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(APIErrorResult apiErrorResult) {
                        Toast.makeText(getApplicationContext(), "logout failure", Toast.LENGTH_LONG);
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    private class MySessionStatusCallback implements SessionCallback {
        /**
         * 세션이 오픈되었으면 가입페이지로 이동 한다.
         */
        @Override
        public void onSessionOpened() {
            //뺑글이 종료
            // 프로그레스바를 보이고 있었다면 중지하고 세션 오픈후 보일 페이지로 이동
            //Logger.i("onSessionOpened");
            Log.d(TAG, "onSessionOpened");
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onSuccess(UserProfile userProfile) {
                    Log.d(TAG, userProfile.getNickname());
                    Log.d(TAG, String.valueOf(userProfile.getId()));
                    String kakaoID = String.valueOf(userProfile.getId());
                    UseServerREST useServerREST = new UseServerREST();
                    useServerREST.execute("testID");
                    //User user = useServerREST.getUserInfo(kakaoID);
                    //Log.d(TAG, user.getAuthID());

                }
                @Override
                public void onNotSignedUp() {}
                @Override
                public void onSessionClosedFailure(APIErrorResult apiErrorResult) {}
                @Override
                public void onFailure(APIErrorResult apiErrorResult) {}
            });
            Intent intent = new Intent(MainActivity.this, MatchingActivity.class);
            startActivity(intent);
        }

        /**
         * 세션이 삭제되었으니 로그인 화면이 보여야 한다.
         * @param exception  에러가 발생하여 close가 된 경우 해당 exception
         */
        @Override
        public void onSessionClosed(final KakaoException exception) {
            //뺑글이 종료
            // 프로그레스바를 보이고 있었다면 중지하고 세션 오픈을 못했으니 다시 로그인 버튼 노출.
            //Logger.i("onSessionClosed()");
            Log.d(TAG, "onSessionClosed");
            kakaoBtn.setVisibility(View.VISIBLE);

            if(exception != null) {
              //  Logger.e(exception);
            }
        }

        @Override
        public void onSessionOpening() {
            //뺑글이 시작
            //Logger.d("onSessionOpening");
            Log.d(TAG, "onSessionOpening");
        }

    }
    protected void onSessionOpened(){
        email.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        emailBtn.setVisibility(View.GONE);
        fbBtn.setVisibility(View.GONE);
        kakaoBtn.setVisibility(View.GONE);

    }
}
