package com.kemendikbud.paud.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.kemendikbud.paud.Main;
import com.kemendikbud.paud.MainActivity;
import com.kemendikbud.paud.R;
import com.kemendikbud.paud.model.User;
import com.kemendikbud.paud.network.LoginService;
import com.kemendikbud.paud.network.config.ApiClient;
import com.kemendikbud.paud.network.config.ApiInterface;
import com.kemendikbud.paud.model.MessageResponse;
import com.kemendikbud.paud.util.SessionManager;
import com.medialablk.easytoast.EasyToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kemendikbud.paud.util.AlertDialogManager.showAlertDialog;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();

    @BindView(R.id.email)EditText inputEmail;
    @BindView(R.id.password)EditText inputPassword;

    private ProgressDialog progressDialog;

    private String email, password;

    private LoginService loginService;

    public static void start(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        context.startActivity(intent);
    }

    public static String USER_SESSION = "user_session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if(isSessionLogin()) {
            Main.start(this);
            SignInActivity.this.finish();
        }

        ButterKnife.bind(this);

        initComponents();

    }

    private void initComponents(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sign In. Please wait...");
    }

    private void hideProgress() {
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void showProgress() {
        progressDialog.show();
    }

    private void getData(){
        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();
    }

    @OnClick(R.id.btn_login)
    public void doSignIn(){
        getData();

        authenticateUser(email, password);
    }

    private boolean isValidateForm(){
        boolean result = true;

        getData();

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Enter email address!");
            result = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Invalid email!");
            result = false;
        } else {
            inputEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Enter password!");
            result = false;
        } else if (password.length() < 6){
            inputPassword.setError(getString(R.string.warning_minimum_password));
            result = false;
        } else {
            inputPassword.setError(null);
        }

        return result;
    }

    private void authenticateUser(final String email, String password){

        if (!isValidateForm()){
            return;
        }

        showProgress();

        loginService = new LoginService();
        loginService.doLogin(email, password, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                User user = (User) response.body();
                hideProgress();
                if (user != null){
                    if (!user.isError()){
                        onSuccessLogin(SignInActivity.this, USER_SESSION, user);
                        Main.start(SignInActivity.this);
                        SignInActivity.this.finish();
                        EasyToast.info(getApplicationContext(), "Welcome " + user.getData().getName());
                    } else {
                        showAlertDialog(SignInActivity.this, getString(R.string.app_name), getString(R.string.auth_failed), false);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG + " -> onFailure: ", t.getMessage());
                hideProgress();
            }
        });
    }

    private void onSuccessLogin(Context context, String key, User user){
        SessionManager.putUser(context, key, user);
    }

    boolean isSessionLogin() {
        return SessionManager.getUser(this, USER_SESSION) != null;
    }


}