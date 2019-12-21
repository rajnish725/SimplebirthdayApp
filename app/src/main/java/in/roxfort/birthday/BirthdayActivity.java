package in.roxfort.birthday;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class BirthdayActivity extends AppCompatActivity {
    private final String TAG = BirthdayActivity.class.getSimpleName();
    private TextView txt_birthday, txt_birthday_name;
    private PublisherAdView mPublisherAdView;
    Context mContext = null;
    boolean isYes = true;
    Dialog dialog = null;
    EditText edittext;
    private String name;
    Button btn_stop;


    /*ads declaration*/

    private static final String AD_UNIT_ID = "ca-app-pub-6805683297486423/3691042196";

    private InterstitialAd interstitialAd;
    private CountDownTimer countDownTimer;
    private boolean gameIsInProgress;
    AdRequest requestAd;
    private long timerMilliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_birthday_screen);
        mContext = this;
        hideSoftKeyboard(mContext);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (isYes) {
            dialogName();
            isYes = false;
        }

        // TODO: 20/12/19
        //todo Initialize the Mobile Ads SDK.
        requestAd = new AdRequest.Builder().build();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mPublisherAdView = findViewById(R.id.adView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        // Display Banner ad
        mPublisherAdView.loadAd(adRequest);
//        loadRewardedVideoAd();

        edittext = findViewById(R.id.edittext);
        txt_birthday = findViewById(R.id.txt_birthday);
        txt_birthday_name = findViewById(R.id.txt_birthday_name);
        btn_stop = findViewById(R.id.btn_stop);

        txt_birthday_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BackgroundSoundService.class);
                stopService(intent);
                return true;
            }
        });


        // Create the InterstitialAd and set the adUnitId.
        interstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        interstitialAd.setAdUnitId(AD_UNIT_ID);


        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                showInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

                txt_birthday_name.setVisibility(View.VISIBLE);
                txt_birthday_name.setText(name);
                Intent intent = new Intent(getApplicationContext(), BackgroundSoundService.class);
                startService(intent);
                RunAnimation();

            }

            @Override
            public void onAdClosed() {
//                startGame();

                txt_birthday_name.setVisibility(View.VISIBLE);
                txt_birthday_name.setText(name);
                Intent intent = new Intent(getApplicationContext(), BackgroundSoundService.class);
                startService(intent);
                RunAnimation();
            }
        });

    }

    private void showInterstitial() {
        try {
            Log.e(TAG, "inside   try");
            if (interstitialAd != null) {
                interstitialAd.loadAd(requestAd);
                interstitialAd.show();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());

        }

    }


    private void dialogName() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_name, null, false);
        //*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*//*
        ImageView imag_back = view.findViewById(R.id.img_back);
        final EditText edt_name = view.findViewById(R.id.edt_name);
        final Button btn_continue = view.findViewById(R.id.btn_continue);
        final TextView txt_error = view.findViewById(R.id.txt_error);


        ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        window.setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.9f);
        dialog.setCancelable(false);
        dialog.show();

        Toast.makeText(mContext, "Make sure input HesdPhone !!!!....", Toast.LENGTH_SHORT).show();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edt_name.getText().toString().trim();
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (TextUtils.isEmpty(name)) {

                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    txt_error.setVisibility(View.VISIBLE);

                } else {
                    name = edt_name.getText().toString();
                    showInterstitial();


                    dialog.dismiss();
                }
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
    }

    private void RunAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.text_anim);
        a.reset();
        txt_birthday_name.clearAnimation();
        txt_birthday_name.startAnimation(a);
    }

    public void hideSoftKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager)
                mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        invalidate();
//        requestLayout();\


    }


}
