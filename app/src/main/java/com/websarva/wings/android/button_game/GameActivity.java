package com.websarva.wings.android.button_game;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private TextView mMainText;
    private Button[] mBtList = new Button[9];
    private List<Integer> mQuestionList = new ArrayList<>();
    private boolean startQuestion = false;
    private int mCount = 4;
    // 正解数
    private int mAnswer = 0;

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mMainText = findViewById(R.id.textView);

        int[] btId = {
                R.id.button1,
                R.id.button2,
                R.id.button3,
                R.id.button4,
                R.id.button5,
                R.id.button6,
                R.id.button7,
                R.id.button8,
                R.id.button9,
        };

        // ボタンをリストで管理
        for (int i = 0; i < mBtList.length; i++) {
            mBtList[i] = findViewById(btId[i]);
        }

        Question question = new Question();

        // 全ボタンを無効化
        setButtonDisable(mBtList);

        // スタートまでカウントダウン
        mHandler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                mCount--;
                mMainText.setText(String.valueOf(mCount));
                mHandler.postDelayed(this, 1000);

                if (mCount == 0) {
                    mMainText.setText("");
                    mHandler.removeCallbacks(runnable);
                    // 出題
                    question.start();
                }
            }
        };
        mHandler.post(runnable);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            default:
                break;
        }
    }

    private void setButtonEnable(Button[] btList) {
        for (Button bt : btList) {
            bt.setEnabled(true);
        }
    }

    private void setButtonDisable(Button[] btList) {
        for (Button bt : btList) {
            bt.setEnabled(false);
        }
    }

    // 出題
    private void askQuestion(int questionNum) {

    }

    public void onChange(View view) {
        Button button1 = findViewById(R.id.button1);
        switch (view.getId()) {
            case R.id.button_test:
                button1.setEnabled(false);
                button1.setBackgroundColor(Color.parseColor("#99CCFF"));
                break;
            case R.id.button_test2:
                button1.setEnabled(true);
                button1.setBackgroundResource(R.drawable.button_style);
                break;
        }
    }

    // 問題を出題
    class Question {
        // 点灯中のボタンを記憶
        private int activeButton;

        public void start() {
            lightButton();

            mMainText.setText("回答してください");
        }

        private void lightButton() {
            Random random = new Random();
            activeButton = random.nextInt(9);

            mBtList[activeButton].setBackgroundColor(Color.parseColor("#99CCFF"));
        }
        // インターバルで呼ばれる
//        @Override
//        public void onTick(long millisUntilFinished) {
//            // 奇数回目の場合はボタンを点灯
//            // 偶数回目の場合はボタンを消灯
//            if (mCountOnTick % 2 != 0) {
//                Random random = new Random();
//                lightButton = random.nextInt(9);
//
//                mBtList[lightButton].setBackgroundColor(Color.parseColor("#99CCFF"));
//            } else {
//                mBtList[lightButton].setBackgroundResource(R.drawable.button_style);
//            }
//
//            mCountOnTick++;
//            mMainText.setText(mCountOnTick);
    }
}