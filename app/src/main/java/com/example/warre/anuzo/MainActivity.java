package com.example.warre.anuzo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        EditText edit = findViewById(R.id.edit);
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e(LOG_TAG, actionId + "");
                return false;
            }
        });


//        EditText edit = findViewById(R.id.edit);
//        edit.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        edit.setImeOptions(EditorInfo.IME_ACTION_GO);
//        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId,
//                                          KeyEvent event) {
//                Log.e(LOG_TAG, "a"+actionId);
//                // 点击虚拟键盘的done
//                if (actionId == EditorInfo.IME_ACTION_DONE
//                        || actionId == EditorInfo.IME_ACTION_GO) {
//                    Log.e(LOG_TAG, "b");
//
//                }
//                return false;
//            }
//        });

    }
}
