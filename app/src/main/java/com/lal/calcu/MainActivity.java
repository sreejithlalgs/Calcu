package com.lal.calcu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends Activity {


    private static final String CALCU_PREFS = "calcuPrefs";
    private static final String HIGH_SCORE = "highScore";

    private TimeCalculator calc;
    private CircularProgressBar timeProgressBar;
    private TextSwitcher numberTextSwitcher;
    private TextSwitcher successTextSwitcher;
    private TextSwitcher failureTextSwitcher;

    private List<String> buttonQuotes;


    private int highScore;


    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button buttonGo;
    private Button buttonReset;

    private TimerTask timerTask;
    private Timer timer;

    private int successCount;
    private int failureCount;
    private boolean paused;


    public MainActivity() {
        calc = new TimeCalculator();
        successCount = 0;
        buttonQuotes = Arrays.asList("BE.YOURSELF", "MOVE.ON", "FREE.YOURSELF", "COME.BACK", "LOOK.UP", "BREAM.BIG", "START.LIVING", "I.AM", "DEFINE.YOURSELF", "BE.HAPPY", "BE.FEARLESS", "ACCEPT.YOURSELF", "I.CAN", "BEAT.ME", "STAY.POSITIVE", "WORK.HARD", "BE.POSITIVE", "GAME.ON", "DON'T STOP", "STAY.STRONG", "TRY.AGAIN", "LET'S.GO", "ENJOY.LIFE");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonGo = (Button) findViewById(R.id.button_go);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        buttonReset = (Button) findViewById(R.id.button_reset);


        timeProgressBar = (CircularProgressBar) findViewById(R.id.time_progress_bar);
        timeProgressBar.setMax(60);
        timeProgressBar.setShadow(Color.GRAY);

        numberTextSwitcher = (TextSwitcher) findViewById(R.id.text_number_disp);
        numberTextSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_up_in));
        numberTextSwitcher.setOutAnimation(this, R.anim.push_up_out);
        numberTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView myText = new TextView(MainActivity.this);
                myText.setGravity(Gravity.CENTER);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER);
                myText.setLayoutParams(params);

                myText.setTextSize(80);
                myText.setTextColor(Color.rgb(211, 216, 194));
                return myText;
            }
        });


        successCount = 0;
        failureCount = 0;


        Animation slide_in_left = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        Animation slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);


        successTextSwitcher = (TextSwitcher) findViewById(R.id.text_current_success);
        successTextSwitcher.setInAnimation(slide_in_left);
        successTextSwitcher.setOutAnimation(slide_out_right);
        successTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView myText = new TextView(MainActivity.this);
                myText.setGravity(Gravity.CENTER);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER);
                myText.setLayoutParams(params);

                myText.setTextSize(30);
                myText.setTextColor(Color.GREEN);
                return myText;
            }
        });


        failureTextSwitcher = (TextSwitcher) findViewById(R.id.text_current_failure);
        failureTextSwitcher.setInAnimation(slide_out_right);
        failureTextSwitcher.setOutAnimation(slide_in_left);
        failureTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView myText = new TextView(MainActivity.this);
                myText.setGravity(Gravity.CENTER);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER);
                myText.setLayoutParams(params);

                myText.setTextSize(30);
                myText.setTextColor(Color.RED);
                return myText;
            }
        });

        addListenerOnButton();
        SharedPreferences settings = getSharedPreferences(CALCU_PREFS, MODE_PRIVATE);
        highScore = settings.getInt(HIGH_SCORE, 0);
        buttonReset.setClickable(false);
        buttonReset.setText("HS: " + String.valueOf(highScore));
        resetAll();
    }

    public void addListenerOnButton() {
        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
        button4.setOnClickListener(onClickListener);
        button5.setOnClickListener(onClickListener);
        button6.setOnClickListener(onClickListener);
        button7.setOnClickListener(onClickListener);
        button8.setOnClickListener(onClickListener);
        button9.setOnClickListener(onClickListener);
        buttonGo.setOnClickListener(onClickListener);
        buttonReset.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button1:
                    updateResults(1, R.id.button1);
                    break;
                case R.id.button2:
                    updateResults(2, R.id.button2);
                    break;
                case R.id.button3:
                    updateResults(3, R.id.button3);
                    break;
                case R.id.button4:
                    updateResults(4, R.id.button4);
                    break;
                case R.id.button5:
                    updateResults(5, R.id.button5);
                    break;
                case R.id.button6:
                    updateResults(6, R.id.button6);
                    break;
                case R.id.button7:
                    updateResults(7, R.id.button7);
                    break;
                case R.id.button8:
                    updateResults(8, R.id.button8);
                    break;
                case R.id.button9:
                    updateResults(9, R.id.button9);
                    break;
                case R.id.button_go:
                    updateTimer();
                    break;
                case R.id.button_reset:
                    resetAll();
                    break;

            }

        }
    };


    private void resetAll() {
        disableAllButtons();

        if (timer != null) {
            timer.cancel();
            timer.purge();
        }


        if (highScore < successCount - failureCount) {
            highScore = successCount - failureCount;
            SharedPreferences settings = getSharedPreferences(CALCU_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(HIGH_SCORE, highScore);
            editor.commit();
        }

        timeProgressBar.setTitleSize(60);
        timeProgressBar.setSubtitleSize(20);

      //  int index = (int)(Math.random() * (buttonQuotes.size()+1));
        //String[] quotesWords = buttonQuotes.get(index).split("\\.");

        timeProgressBar.setTitle("...");
        timeProgressBar.setSubTitle(".    .    .");
        buttonGo.setClickable(true);
        buttonReset.setClickable(false);
        successTextSwitcher.setText("");
        failureTextSwitcher.setText("");
        timeProgressBar.setProgress(0);
        numberTextSwitcher.setText("");
        buttonReset.setText("HS: " + String.valueOf(highScore));

    }

    private void updateResults(int value, int id) {
        disableAllButtons();

        if (calc.verifySum(value)) {
            successTextSwitcher.setText(String.valueOf(++successCount));
        } else {
            failureTextSwitcher.setText(String.valueOf(++failureCount));
        }
        updateNumber();

    }

    private void updateTimer() {
        successCount = 0;
        failureCount = 0;
        buttonGo.setClickable(false);
        buttonReset.setClickable(true);
        final Handler h = new Handler();

        timer = new Timer();
        final AtomicInteger counter = new AtomicInteger();
        timeProgressBar.setTitleSize(60);
        timeProgressBar.setSubtitleSize(30);
        timeProgressBar.setTitle("60");
        timeProgressBar.setSubTitle("seconds");
        timerTask = new TimerTask() {
            @Override
            public void run() {
                while (paused) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                h.post(new Runnable() {

                    public void run() {

                        if (counter.get() < 60) {
                            int count = counter.incrementAndGet();
                            timeProgressBar.setProgress(count);
                            timeProgressBar.setTitle(String.valueOf(60 - count));
                        } else {


                            resetAll();

                        }

                    }
                });
            }
        };
        timer.schedule(timerTask, 1000, 1000);
        buttonReset.setText("RESET");
        updateNumber();
    }


    private void updateNumber() {
        numberTextSwitcher.setText(String.valueOf(calc.getNumber()));
        buttonReset.setClickable(true);
        enableAllButtons();
    }

    private void disableAllButtons() {
        button1.setClickable(false);
        button2.setClickable(false);
        button3.setClickable(false);
        button4.setClickable(false);
        button5.setClickable(false);
        button6.setClickable(false);
        button7.setClickable(false);
        button8.setClickable(false);
        button9.setClickable(false);
    }

    private void enableAllButtons() {
        button1.setClickable(true);
        button2.setClickable(true);
        button3.setClickable(true);
        button4.setClickable(true);
        button5.setClickable(true);
        button6.setClickable(true);
        button7.setClickable(true);
        button8.setClickable(true);
        button9.setClickable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;

    }

    @Override
    public void onResume() {
        super.onResume();
        paused = false;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

}
