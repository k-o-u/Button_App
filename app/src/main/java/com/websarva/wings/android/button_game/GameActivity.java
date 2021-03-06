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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private TextView mMainText;
    private Button[] mBtList = new Button[9];
    private Queue<Integer> queue = new ArrayDeque<>();
    private  Question question;
    private int mCount = 4;
    // 正解した問題数
    private int mAnswer = 0;

    private Handler mHandler;

    private Runnable runnable;

    Random random = new Random();

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

        // 全ボタンを無効化
        setButtonDisable(mBtList);

        question = new Question();

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
                    question.startQuestion(0);
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

    public void onAnswer(View view) {
        Button button = findViewById(view.getId());
        Button answer = mBtList[queue.poll()];

        if (answer != button) {
            mMainText.setText("不正解です\n正解数：" + mAnswer);
            setButtonDisable(mBtList);
        } else {
            if (queue.size() == 0) {
                mMainText.setText("正解!!");
                setButtonDisable(mBtList);
                mAnswer++;
                question.startQuestion(2000);
            }
        }
    }

    class Question {
        private int mQuestionNum;
//        private int mQuestionNum = 3;
        // 点灯しているボタンを記憶
        private int mActiveButton;

        public void startQuestion(int delay) {
            setQuestionNum();
            mHandler.postDelayed(buttonOnRunnable, delay);
        }

        public void answerQuestion() {
            mMainText.setText("回答してください");
            setButtonEnable(mBtList);
        }

        public void setQuestionNum() {
            mQuestionNum = mAnswer;
        }

        // ボタンをランダムに光らせる
        Runnable buttonOnRunnable = new Runnable() {
            @Override
            public void run() {
                mMainText.setText("");
                mActiveButton = random.nextInt(9);
                queue.add(mActiveButton);
                mBtList[mActiveButton].setBackgroundColor(Color.parseColor("#99CCFF"));

                mHandler.postDelayed(buttonOffRunnable, 500);
            }
        };

        // 点灯しているボタンを消灯する
        Runnable buttonOffRunnable = new Runnable() {
            @Override
            public void run() {
                mBtList[mActiveButton].setBackgroundResource(R.drawable.button_style);

                if (mQuestionNum == 0) {
                    answerQuestion();
                } else {
                    mHandler.postDelayed(buttonOnRunnable, 500);
                    mQuestionNum--;
                }
            }
        };
    }
}