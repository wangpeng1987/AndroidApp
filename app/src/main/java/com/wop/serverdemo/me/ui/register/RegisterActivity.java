package com.wop.serverdemo.me.ui.register;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wop.serverdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    @BindView(R.id.register_username_atv)
    EditText registerUsenameAtv;

    @BindView(R.id.register_password_et)
    EditText registerPasswordEt;

    @BindView(R.id.register_email_et)
    EditText registerEmailEt;

    @BindView(R.id.register_phone_et)
    EditText registerPhoneEt;

    @BindView(R.id.register_bt)
    Button registerBt;

    private RegisterContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mPresenter = new RegisterPresenter(this);

//        registerUsenameAtv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String temp = s.toString();
////                registerUsenameAtv.setText(temp.toUpperCase());
//            }
//        });

        registerUsenameAtv.setText("wangpeng1");
        registerPasswordEt.setText("wwwwww");
        registerEmailEt.setText("40666@qq.com");
        registerPhoneEt.setText("13089897878");
        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = registerUsenameAtv.getText().toString().toUpperCase();
                String password = registerPasswordEt.getText().toString();
                String email = registerEmailEt.getText().toString();
                String phone = registerPhoneEt.getText().toString();

                if (username == null || username.isEmpty()) {

                    return;
                }

                if (password == null || password.isEmpty()) {

                    return;
                }

                mPresenter.sigup(email, username, password, "86", "http://www.pptok.com/wp-content/uploads/2012/08/xunguang-4.jpg", phone);
            }
        });

    }

    @Override
    public void onRegisterSuccess(JSONObject jsonObject) {
        Toast.makeText(this, "注册 成功！！！！！", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRegisterError(String msg) {
        Toast.makeText(this, "onError 返回 ： msg = " + msg, Toast.LENGTH_LONG).show();
    }
}
