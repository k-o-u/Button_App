package com.websarva.wings.android.button_game;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    TextView mMainText;
    Button[] mBtList = new Button[9];
    List<Integer> mQuestionList = new ArrayList<>();
    private boolean startQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

        // 全ボタンを無効化
        setButtonDisable(mBtList);

        // スタートまでカウントダウン
        mMainText = findViewById(R.id.textView);
        startCount();

        // 正解数
        int answer = 0;

        while(true) {
            while(startQuestion) {
                // 出題
                askQuestion(answer+1);

                // 回答

                // 間違えた場合その時点で結果表示

                answer++;
                break;
            }
        }

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

    // ゲーム開始までのカウントダウン
    private void startCount() {
        // 3秒 = 3000 msec
        long countNumber = 3000;
        // インターバル msec
        long interval = 1000;

        // タイマー生成
        final CountDown countDown = new CountDown(countNumber, interval);
        // カウントダウン開始
        countDown.start();
    }

    // 出題
    private void askQuestion(int questionNum) {
        // 出題時間は 問題数*1秒
        long countNumber = questionNum * 1000 + 300;
        // ボタンの光る時間・消える時間は0.5秒
        long interval = 500;

        final Question question = new Question(countNumber, interval);
        question.start();
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

    class CountDown extends CountDownTimer {
        private int mCount = 3;

        CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 完了
            mMainText.setText("");
            startQuestion = true;
        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisUntilFinished) {
            mMainText.setText(String.valueOf(mCount--));
        }
    }

    // 問題を出題
    class Question extends CountDownTimer {
        // onTickを呼び出した回数
        private int mCountOnTick = 0;
        // 点灯中のボタンを記憶
        private int lightButton;

        Question(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisUntilFinished) {
            // 奇数回目の場合はボタンを点灯
            // 偶数回目の場合はボタンを消灯
            if (mCountOnTick % 2 != 0) {
                Random random = new Random();
                lightButton = random.nextInt(9);

                mBtList[lightButton].setBackgroundColor(Color.parseColor("#99CCFF"));
            } else {
                mBtList[lightButton].setBackgroundResource(R.drawable.button_style);
            }

            mCountOnTick++;
//            mMainText.setText(mCountOnTick);
        }
    }
}