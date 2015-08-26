package com.plainit.tockto;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import com.kakao.auth.APIErrorResult;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LogoutResponseCallback;
import com.kakao.usermgmt.UserManagement;

/**
 * Created by newpouy on 15. 8. 25.
 */
public class MatchingActivity extends AppCompatActivity {
    final static String TAG = "Matching";
    Session kakao_session = Session.getCurrentSession();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(kakao_session.isClosed()){
            Toast.makeText(getApplicationContext(), "로그인해야 합니다", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MatchingActivity.this, MainActivity.class);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                        Intent intent = new Intent(MatchingActivity.this, MainActivity.class);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                String xStr = String.valueOf(x);
                String yStr = String.valueOf(y);
                Log.d(TAG, xStr+" "+yStr);
                
                return true;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
