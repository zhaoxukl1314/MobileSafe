package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/7/6.
 */
public abstract class BaseSetupActivity extends Activity {

    private static final String TAG = "BaseSetupActivity";
    private GestureDetector gestureDetector;

    public SharedPreferences sharedPreferences;

    public abstract void showNext();

    public abstract void showPre();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new myGestureListener());
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
    }

    public void next(View v) {
        showNext();
    }

    public void pre(View v) {
        showPre();
    }

    private class myGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

            if ((motionEvent.getRawX() - motionEvent1.getRawX()) > 200) {
                showNext();
                return true;
            }

            if ((motionEvent1.getRawX() - motionEvent.getRawX()) > 200) {
                showPre();
                return true;
            }

            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
