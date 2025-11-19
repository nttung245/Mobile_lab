package com.example.lab4_01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnFadeInXml, btnFadeInCode, btnFadeOutXml, btnFadeOutCode, btnBlinkXml,
            btnBlinkCode, btnZoomInXml, btnZoomInCode, btnZoomOutXml, btnZoomOutCode, btnRotateXml,
            btnRotateCode, btnMoveXml, btnMoveCode, btnSlideUpXml, btnSlideUpCode, btnBounceXml,
            btnBounceCode, btnCombineXml, btnCombineCode;

    private ImageView ivUitLogo;
    private Animation.AnimationListener animationListener;

    private void findViewsByIds() {
        ivUitLogo = findViewById(R.id.iv_uit_logo);
        btnFadeInXml = findViewById(R.id.btn_fade_in_xml);
        btnFadeInCode = findViewById(R.id.btn_fade_in_code);
        btnFadeOutXml = findViewById(R.id.btn_fade_out_xml);
        btnFadeOutCode = findViewById(R.id.btn_fade_out_code);
        btnBlinkXml = findViewById(R.id.btn_blink_xml);
        btnBlinkCode = findViewById(R.id.btn_blink_code);
        btnZoomInXml = findViewById(R.id.btn_zoom_in_xml);
        btnZoomInCode = findViewById(R.id.btn_zoom_in_code);
        btnZoomOutXml = findViewById(R.id.btn_zoom_out_xml);
        btnZoomOutCode =  findViewById(R.id.btn_zoom_out_code);
        btnRotateXml =  findViewById(R.id.btn_rotate_xml);
        btnRotateCode =  findViewById(R.id.btn_rotate_code);
        btnMoveXml =  findViewById(R.id.btn_move_xml);
        btnMoveCode =  findViewById(R.id.btn_move_code);
        btnSlideUpXml =  findViewById(R.id.btn_slide_up_xml);
        btnSlideUpCode =  findViewById(R.id.btn_slide_up_code);
        btnBounceXml =  findViewById(R.id.btn_bounce_xml);
        btnBounceCode =  findViewById(R.id.btn_bounce_code);
        btnCombineXml =  findViewById(R.id.btn_combine_xml);
        btnCombineCode =  findViewById(R.id.btn_combine_code);
    }

    private void initVariables() {
        animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(getApplicationContext(), "Animation Stopped",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };
    }

    private void handleClickAnimationXml(Button btn, final Animation animation) {
        // Handle onclickListenner to start animation
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ivUitLogo.startAnimation(animation);
            }
        });
    }

    private void handleClickAnimationCode(Button btn, final Animation animation) {
        // Handle onclickListenner to start animation
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ivUitLogo.startAnimation(animation);
            }
        });
    }

    private Animation initFadeInAnimation() {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setAnimationListener(animationListener);
        return animation;
    }

    private Animation initFadeOutAnimation() {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setAnimationListener(animationListener);
        return animation;
    }

    private Animation initBlinkAnimation() {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(300);
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setFillAfter(true);
        animation.setAnimationListener(animationListener);
        return animation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsByIds();
        initVariables();

        handleClickAnimationXml(btnFadeInXml, AnimationUtils.loadAnimation(this, R.anim.anim_fade_in));
        handleClickAnimationXml(btnFadeOutXml, AnimationUtils.loadAnimation(this, R.anim.anim_fade_out));
        handleClickAnimationXml(btnBlinkXml, AnimationUtils.loadAnimation(this, R.anim.anim_blink));
        handleClickAnimationXml(btnZoomInXml, AnimationUtils.loadAnimation(this, R.anim.anim_zoom_in));
        handleClickAnimationXml(btnZoomOutXml, AnimationUtils.loadAnimation(this, R.anim.anim_zoom_out));
        handleClickAnimationXml(btnRotateXml, AnimationUtils.loadAnimation(this, R.anim.anim_rotate));
        handleClickAnimationXml(btnMoveXml, AnimationUtils.loadAnimation(this, R.anim.anim_move));
        handleClickAnimationXml(btnSlideUpXml, AnimationUtils.loadAnimation(this, R.anim.anim_slide_up));
        handleClickAnimationXml(btnBounceXml, AnimationUtils.loadAnimation(this, R.anim.anim_bounce));
        handleClickAnimationXml(btnCombineXml, AnimationUtils.loadAnimation(this, R.anim.anim_combine));

        handleClickAnimationCode(btnFadeInCode, initFadeInAnimation());
        handleClickAnimationCode(btnFadeOutCode, initFadeOutAnimation());
        handleClickAnimationCode(btnBlinkCode, initBlinkAnimation());

        ivUitLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iNewActivity = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(iNewActivity);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

}