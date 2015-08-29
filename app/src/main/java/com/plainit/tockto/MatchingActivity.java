package com.plainit.tockto;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
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
public class MatchingActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    final static String TAG = "Matching";
    GestureDetectorCompat mDetector;
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
        mDetector = new GestureDetectorCompat(this, this);

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
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().close();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        int action = MotionEventCompat.getActionMasked(event);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX(); float y = event.getY();
                String xStr = String.valueOf(x); String yStr = String.valueOf(y);
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

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        String e1XStr = String.valueOf(e1.getX());
        String e1YStr = String.valueOf(e1.getY());
        String e2XStr = String.valueOf(e2.getX());
        String e2YStr = String.valueOf(e2.getY());
        Log.d(TAG, e1XStr+" e1 "+e1YStr);
        Log.d(TAG, ""+velocityX);
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG, "onLongPress");

    }
}
