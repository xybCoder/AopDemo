package com.example.xyb.aopdemo.aop;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.example.xyb.aopdemo.LoginActivity;
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
    public Object aroundAspectJ(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Log.i(TAG, "aroundAspectJ(ProceedingJoinPoint joinPoint)");
        CheckLogin aspectJAnnotation = methodSignature.getMethod().getAnnotation(CheckLogin.class);
        boolean isJump = aspectJAnnotation.isJump();
        String token = aspectJAnnotation.token();
        Context context = (Context) joinPoint.getThis();
        Object o = null;
        if (TextUtils.isEmpty(token)) {//未登录
            if (joinPoint.getThis() instanceof AppCompatActivity) {
                context.startActivity(new Intent(context, LoginActivity.class));
            } else if (joinPoint.getThis() instanceof Fragment) {
                context = ((Fragment) joinPoint.getThis()).getActivity();
                context.startActivity(new Intent(context, LoginActivity.class));
            } else {
                return null;
            }

            if (isJump) {
                //是否跳转
                o = joinPoint.proceed();
            }

        } else {//登录
            joinPoint.proceed();
        }

        return o;
    }

}
