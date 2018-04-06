package joetde.com.pleasedarts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private static final int REQ_CODE_SPEECH_INPUT = 123;

    private boolean mTouchDown = false;

    private View mContentView;
    private TextView mDebugView;
    private TextView mDebug2View;
    private CricketModel mModel;
    private int dartCount = 0;
    private int player = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mContentView = findViewById(R.id.fullscreen_content);
        mDebugView = findViewById(R.id.debug_content);
        mDebug2View = findViewById(R.id.debug_content_2);
        mContentView.setBackgroundColor(0xff0099cc);

        mModel = new CricketModel();

        // Hide title
        getSupportActionBar().hide();

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!mTouchDown && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("TouchTest", "Touch down");
                    mTouchDown = true;
                    onMainDown();
                } else if (mTouchDown && motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("TouchTest", "Touch up");
                    mTouchDown = false;
                    onMainUp();
                }

                return true;
            }
        });

        show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void onMainDown() {
        mContentView.setBackgroundColor(0xff00ffcc);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
    }

    private void onMainUp() {
        mContentView.setBackgroundColor(0xff0099cc);
    }

    private void setDebugText(String text) {
        mDebugView.setText(text);
    }

    private void setDebug2Text(String text) {
        mDebug2View.setText(text);
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    List<NDLP.DartScore> l = NDLP.parseInput(result.get(0));
                    if (!l.isEmpty() && l.get(0) != null) {
                        setDebugText("\n\n" + l.toString());
                        processScore(l);
                    }
                    setDebug2Text("\n\n" + result.get(0));
                }
                break;
            }

        }
    }

    private void processScore(List<NDLP.DartScore> scores) {
        for (NDLP.DartScore s : scores) {
            mModel.addScore(player, s);

            dartCount += s.getQuantifierInt();
            if (dartCount == 3) {
                dartCount = 0;
                player = 1 - player;
            }
        }

        String dartString = "> ";
        for (int i = 0; i < dartCount; i++) {
            dartString += "I";
        }

        if (player == 0) {
            ((TextView) findViewById(R.id.player_0_progress)).setText(dartString);
            ((TextView) findViewById(R.id.player_1_progress)).setText("");
        } else {
            ((TextView) findViewById(R.id.player_0_progress)).setText("");
            ((TextView) findViewById(R.id.player_1_progress)).setText(dartString);
        }

        ((TextView) findViewById(R.id.player_0_15)).setText(mModel.getText(0, NDLP.Region.FIFTEEN));
        ((TextView) findViewById(R.id.player_0_16)).setText(mModel.getText(0, NDLP.Region.SIXTEEN));
        ((TextView) findViewById(R.id.player_0_17)).setText(mModel.getText(0, NDLP.Region.SEVENTEEN));
        ((TextView) findViewById(R.id.player_0_18)).setText(mModel.getText(0, NDLP.Region.EIGHTEEN));
        ((TextView) findViewById(R.id.player_0_19)).setText(mModel.getText(0, NDLP.Region.NINETEEN));
        ((TextView) findViewById(R.id.player_0_20)).setText(mModel.getText(0, NDLP.Region.TWENTY));
        ((TextView) findViewById(R.id.player_0_B)).setText(mModel.getText(0, NDLP.Region.BULLSEYE));
        ((TextView) findViewById(R.id.player_1_15)).setText(mModel.getText(1, NDLP.Region.FIFTEEN));
        ((TextView) findViewById(R.id.player_1_16)).setText(mModel.getText(1, NDLP.Region.SIXTEEN));
        ((TextView) findViewById(R.id.player_1_17)).setText(mModel.getText(1, NDLP.Region.SEVENTEEN));
        ((TextView) findViewById(R.id.player_1_18)).setText(mModel.getText(1, NDLP.Region.EIGHTEEN));
        ((TextView) findViewById(R.id.player_1_19)).setText(mModel.getText(1, NDLP.Region.NINETEEN));
        ((TextView) findViewById(R.id.player_1_20)).setText(mModel.getText(1, NDLP.Region.TWENTY));
        ((TextView) findViewById(R.id.player_1_B)).setText(mModel.getText(1, NDLP.Region.BULLSEYE));

        ((TextView) findViewById(R.id.player_0_remain)).setText("" + mModel.getAdvance(0));
        ((TextView) findViewById(R.id.player_1_remain)).setText("" + mModel.getAdvance(1));
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

}
