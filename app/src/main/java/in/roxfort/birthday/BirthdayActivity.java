package in.roxfort.birthday;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

public class BirthdayActivity extends AppCompatActivity {
    private TextView txt_birthday, txt_birthday_name;
    Dialog dialog;
    Button btn_stop;
    Context mContext;
    boolean isYes = true;
    EditText edittext;
    private PublisherAdView mPublisherAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_birthday_screen);
        mContext = this;
        hideSoftKeyboard(mContext);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        getSupportActionBar().hide();
        if (isYes) {
            dialogName();
            isYes = false;
        }
      /*  MobileAds.initialize(this,
                getString(R.string.admob_app_id));*/

        mPublisherAdView = findViewById(R.id.adView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        // Display Banner ad
        mPublisherAdView.loadAd(adRequest);

        edittext = findViewById(R.id.edittext);
        txt_birthday = findViewById(R.id.txt_birthday);
        txt_birthday_name = findViewById(R.id.txt_birthday_name);
        btn_stop = findViewById(R.id.btn_stop);


        txt_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txt_birthday_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BackgroundSoundService.class);
                stopService(intent);

                return true;
            }
        });


        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }


    private void dialogName() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog = new Dialog(mContext);
//         dialog.set(android.app.DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
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
//        window.setLayout(600, 250);
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
                String name = edt_name.getText().toString().trim();
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (TextUtils.isEmpty(name)) {

                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    txt_error.setVisibility(View.VISIBLE);

                } else {

                    txt_birthday_name.setVisibility(View.VISIBLE);
                    txt_birthday_name.setText(edt_name.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), BackgroundSoundService.class);
                    startService(intent);
                    RunAnimation();
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
