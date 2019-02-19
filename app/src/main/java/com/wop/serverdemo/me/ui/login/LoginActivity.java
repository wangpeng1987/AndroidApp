package com.wop.serverdemo.me.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.wop.serverdemo.MainActivity;
import com.wop.serverdemo.R;
import com.wop.serverdemo.core.base.BaseActivity;
import com.wop.serverdemo.core.utils.MMKVUtils;
import com.wop.serverdemo.me.model.LoginData;
import com.wop.serverdemo.me.ui.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.login_username_atv)
    EditText loginUsenameAtv;

    @BindView(R.id.login_password_et)
    EditText loginPasswordEt;

    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        boolean loginState = MMKVUtils.getLoginInfoBool(getResources().getString(R.string.login_state));
        if (loginState) {
            startHome();
        }

        mPresenter = new LoginPresenter(this);

        String username = MMKVUtils.getLoginInfoString(getResources().getString(R.string.login_username));
        loginUsenameAtv.setText(username);
        loginUsenameAtv.setSelection(loginUsenameAtv.length());

        loginUsenameAtv.setText("wangpeng1");
        loginPasswordEt.setText("wwwwww");

        findViewById(R.id.sign_in_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = loginUsenameAtv.getText().toString().toUpperCase();
                String password = loginPasswordEt.getText().toString();

                if (username == null || username.isEmpty()) {

                    return;
                }

                if (password == null || password.isEmpty()) {

                    return;
                }

                mPresenter.login(username, password);
            }
        });

        findViewById(R.id.goto_register_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public void onLoginSuccess(LoginData loginData) {
        if (loginData != null) {
//            MMKVUtils.saveLoginInfoValue(getResources().getString(R.string.login_state), true);
//            MMKVUtils.saveLoginInfoValue(getResources().getString(R.string.login_token), loginData.getToken());
//            MMKVUtils.saveLoginInfoValue(getResources().getString(R.string.login_username), loginData.getUser().getUsername());
//            HttpHeadInterceptor.ACCESS_TOKEN = loginData.getToken();

            startHome();
        }
    }

    private void startHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onLoginError(String msg) {
        Toast.makeText(this, "onError 返回 ： msg = " + msg, Toast.LENGTH_LONG).show();
    }
}

