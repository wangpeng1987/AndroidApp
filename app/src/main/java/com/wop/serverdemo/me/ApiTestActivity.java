package com.wop.serverdemo.me;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wop.serverdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApiTestActivity extends AppCompatActivity implements ApiTestContract.View {


    private String[] apiName = {
            "/v1/users/login          用户登陆(OK)",
            "/v1/users/sigup          用户注册(OK)",
            "/v1/users/phone/code     发送短信验证码(OK)",
            "/v1/users/phone/verify   验证 phone 的有效性(OK)",
            "/v1/users/username/set   修改用户的 username(OK)",
            "/v1/users/phone/set      修改用户的 phone(OK)",
            "/v1/users/email/code     发送 email 验证码(服务器不支持)",
            "/v1/users/email/verify   验证 email 的有效性(服务器不支持)",
            "/v1/users/logout         用户注销登陆(OK)",
            "/v1/users/sync           同步用户设备数据(服务器不支持)",
            "/v1/user/profile         获取当前用户信息(OK)",
            "/v1/user/info            批量获取用户信息(OK)",
            "/v1/user/profile/set     用户 profile 更新(OK)",
            "/v1/user/password/forget 找回密码发送短信验证(服务器不支持)",
            "/v1/user/password/reset  重置密码(服务器不支持)",
            "/v1/user/password/change 用户修改密码(OK)",
            "/v1/user/setting         修改当前用户的安全和隐私设置",
            "/v1/user/setting         获取用户安全隐私设置",
            "/v1/user/block           block 用户接口",
            "/v1/user/block           获取 block 用户接口",
            "/v1/user/block/del       删除 block 用户接口"
    };


    private ListView apiLV = null;
    private ApiTestContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);

        mPresenter = new ApiTestPresenter(this);


        apiLV = (ListView) findViewById(R.id.api_lv);
        apiLV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, apiName));
        apiLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ApiTestActivity", "onItemClick = " + position);
                if (position == 0) {
                    mPresenter.login("WANGPENG1", "wwwwww");
                }
                if (position == 1) {
                    mPresenter.sigup("406663624@qq.com", "WANGPENG1", "wwwwww", "boo", "86", 1988, "http://www.pptok.com/wp-content/uploads/2012/08/xunguang-4.jpg", "13022891213");
                }
                if (position == 2) {
                    mPresenter.getPhoneCode("18066745561", "86");
                }

                if (position == 3) {
                    mPresenter.getPhoneVerify("8087", "18066745561", "86");
                }

                if (position == 4) {
                    mPresenter.setUserName("WANGPENG1");
                }

                if (position == 5) {
                    mPresenter.setPhone("18066745561", "86", "9495");
                }

                if (position == 6) {
                    mPresenter.getEmailCode("406663624@qq.com");
                }

                if (position == 7) {
                    mPresenter.getEmailVerify("13022891213", "86");
                }

                if (position == 8) {
                    mPresenter.logout();
                }

                if (position == 9) {
                    mPresenter.sync();
                }

                if (position == 10) {
                    mPresenter.profile();
                }

                if (position == 11) {
                    ArrayList<Integer> ids = new ArrayList();
                    ids.add(100004);
                    mPresenter.userInfo(ids);
                }
                if (position == 12) {
                    mPresenter.setProfile(null, "wp1111111111", null, null, null);
                }

                if (position == 13) {
                    mPresenter.getForgetPassword("18066745561", "86", "");
                }

                if (position == 14) {
                    mPresenter.resetPassword("18066745561", "wwwwww", "wwwwww");
                }

                if (position == 15) {
                    mPresenter.changePassword("wwwwww", "wwwwww");
                }

                if (position == 16) {
                    Map<String, Integer> params = new HashMap<>();
                    params.put("notice_show_previews", 0);
                    params.put("notice_moments_push", 0);
                    params.put("notice_sound", 0);
                    params.put("notice_vibrate", 0);
                    params.put("search_phone", 0);
                    params.put("search_email", 0);
                    params.put("search_username", 0);
                    params.put("quick_add", 0);

                    mPresenter.setting(params);
                }

                if (position == 17) {
                    mPresenter.getSetting();
                }

                if (position == 18) {
                    ArrayList<Integer> blocks = new ArrayList();
                    blocks.add(100004);
                    mPresenter.setBlock(blocks);
                }

                if (position == 19) {
                    mPresenter.getBlock();
                }

                if (position == 20) {
                    ArrayList<Integer> blocks = new ArrayList();
                    blocks.add(100004);
                    mPresenter.delBlock(blocks);
                }

            }
        });
    }

    @Override
    public void showUser(JSONObject jsonObject) {
        if (jsonObject == null) {
            Toast.makeText(this, "请求 成功！！！！！", Toast.LENGTH_LONG).show();
        } else {
            String result = jsonObject.toJSONString();
            Toast.makeText(this, "请求 成功！！！！！\n" + result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(this, "onError 返回 ： msg = " + msg, Toast.LENGTH_LONG).show();
    }
}
