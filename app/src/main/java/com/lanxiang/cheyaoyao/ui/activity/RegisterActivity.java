package com.lanxiang.cheyaoyao.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.lanxiang.cheyaoyao.CheYaoYaoApp;
import com.lanxiang.cheyaoyao.R;
import com.lanxiang.cheyaoyao.base.activity.BaseActivity;
import com.lanxiang.cheyaoyao.base.presenter.BasePresenter;
import com.lanxiang.cheyaoyao.domain.User;
import com.lanxiang.cheyaoyao.utils.StringUtils;
import com.lanxiang.cheyaoyao.widget.CustomShowLoadDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Desc :注册
 * Created by WangDong on 2017/8/17.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.et_user_name)
    TextInputEditText etUserName;
    @BindView(R.id.et_pass_word)
    TextInputEditText etPassWord;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.til_username)
    TextInputLayout tilUsername;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    private String mUsername;
    private String mPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        // 是否可以弹出错误提示语
        tilUsername.setErrorEnabled(true);
        // 当EditText中的文本发生变化时处理TextInputLayout的错误提示语及其显隐
        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if ("".equals(s + "")) {
                        // 设置错误提示语为null，即不显示错误提示语
                        tilUsername.setError(null);
                    } else if (s.length() > 11) {
                        // 如果输入长度超过11位，则显示错误提示
                        tilUsername.setError("账号长度超过上限！");
                    }
                    else {
                        Integer.parseInt(s + "");
                        tilUsername.setError(null);
                    }
                } catch (Exception e) {
                    // 设置错误提示语为具体的文本
//                    tilUsername.setError("输入内容不是数字！");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 是否可以弹出错误提示语
        tilPassword.setErrorEnabled(true);
        // 当EditText中的文本发生变化时处理TextInputLayout的错误提示语及其显隐
        etPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if ("".equals(s + "")) {
                        // 设置错误提示语为null，即不显示错误提示语
                        tilPassword.setError(null);
                    } else if (s.length() > 6) {
                        // 如果输入长度超过11位，则显示错误提示
                        tilPassword.setError("密码长度超过上限！");
                    } else {
                        Integer.parseInt(s + "");
                        tilPassword.setError(null);
                    }
                } catch (Exception e) {
                    // 设置错误提示语为具体的文本
//                    tilUsername.setError("输入内容不是数字！");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @OnClick({R.id.et_user_name, R.id.et_pass_word, R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_user_name:
                break;
            case R.id.et_pass_word:
                break;
            case R.id.bt_register:
                mUsername = etUserName.getText().toString().trim();
                mPassword = etPassWord.getText().toString().trim();
                if (!StringUtils.isMobile(mUsername)){
                    tilUsername.setError("请输入正确的手机号");
                    return;
                }
                if (mPassword.length()>6){
                    // 如果输入长度超过11位，则显示错误提示
                    tilPassword.setError("密码长度超过上限！");
                    return;
                }
                if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
                    Toast.makeText(CheYaoYaoApp.getsContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                register(mUsername, mPassword);
                break;
        }
    }

    private void register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        //Bmob所有回调方法都是在主线程被调用的
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {//他讲成功和失败封装到一起了
                if (e == null) {//注册成功
                    Toast.makeText(CheYaoYaoApp.getsContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginBmobActivity.class);
                    startActivity(intent);
                    finish();
                } else {//注册失败
                    LogUtils.e(e);
                    if (e.getErrorCode()==202){
                        Toast.makeText(CheYaoYaoApp.getsContext(), "用户已存在", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(CheYaoYaoApp.getsContext(), "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
