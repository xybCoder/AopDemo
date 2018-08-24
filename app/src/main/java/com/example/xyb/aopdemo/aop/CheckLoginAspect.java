package com.example.xyb.aopdemo.aop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.xyb.aopdemo.AppManager;
import com.example.xyb.aopdemo.LoginActivity;
import com.example.xyb.aopdemo.NotifyMessageManager;
import com.example.xyb.aopdemo.annotation.CheckLogin;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


@Aspect
public class CheckLoginAspect {

    private static final String TAG = "CheckLoginAspect";

    @Pointcut("execution(@com.example.xyb.aopdemo.annotation.CheckLogin * *(..))")
    public void executionAspectJ() {

    }

    @Around("executionAspectJ()")//在连接点进行方法替换
    public void aroundAspectJ(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Log.i(TAG, "aroundAspectJ(ProceedingJoinPoint joinPoint)");
        CheckLogin aspectJAnnotation = methodSignature.getMethod().getAnnotation(CheckLogin.class);
        final boolean isJump = aspectJAnnotation.isJump();
        String token = aspectJAnnotation.token();
        if (TextUtils.isEmpty(token)) {//未登录
            Context context = null;
            Object object = joinPoint.getThis();
            if (object instanceof Activity) {
                context = (Context) object;
            } else if (object instanceof android.app.Fragment) {
                context = ((android.app.Fragment) object).getActivity();
            } else if (object instanceof android.support.v4.app.Fragment) {
                context = ((android.support.v4.app.Fragment) object).getActivity();
            } else {
                context = AppManager.getInstance().getCurActivity();
            }
            context.startActivity(new Intent(context, LoginActivity.class));
            NotifyMessageManager.getInstance().setOnHandleMessageListener(new NotifyMessageManager.NotifyMessageListener() {
                @Override
                public void onHandleMessage(String msg) {
                    if (isJump && "proceed".equals(msg)) {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
            });
            return;
        }
        joinPoint.proceed();
    }

}
