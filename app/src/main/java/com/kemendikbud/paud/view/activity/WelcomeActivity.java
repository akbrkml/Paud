package com.kemendikbud.paud.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.kemendikbud.paud.Main;
import com.kemendikbud.paud.R;
import com.kemendikbud.paud.util.Constant;
import com.kemendikbud.paud.util.SessionManager;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import static com.kemendikbud.paud.view.activity.SignInActivity.USER_SESSION;

public class WelcomeActivity extends AwesomeSplash {

    @Override
    public void initSplash(ConfigSplash configSplash) {
        /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.ic_logo); //or any other drawable
        configSplash.setAnimLogoSplashDuration(500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Wobble); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash("");
    }

    @Override
    public void animationsFinished() {
        final Activity a = this;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSessionLogin()){
                    Main.start(a);
                    WelcomeActivity.this.finish();
                } else {
                    SignInActivity.start(a);
                    WelcomeActivity.this.finish();
                }
            }
        }, Constant.SPLASH_DELAY);

    }

    boolean isSessionLogin() {
        return SessionManager.getUser(this, USER_SESSION) != null;
    }
}
