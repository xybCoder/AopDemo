package com.example.xyb.aopdemo.aop;

import android.content.Context;
import android.util.Log;

import com.example.admin.aopdemo.annotation.CheckLogin;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


@Aspect
public class CheckLoginAspect {

    private static final String TAG = "CheckLoginAspect";

    @Pointcut("execution(@com.example.admin.aopdemo.annotation.CheckLogin * *(..))")
    public void executionAspectJ() {

    }

    @Around("executionAspectJ()")//在连接点进行方法替换
    public Object aroundAspectJ(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Log.i(TAG, "aroundAspectJ(ProceedingJoinPoint joinPoint)");
        CheckLogin aspectJAnnotation = methodSignature.getMethod().getAnnotation(CheckLogin.class);
        boolean value = aspectJAnnotation.value();
        Context context = (Context) joinPoint.getThis();
        Object o = null;
        if (value) {
            o = joinPoint.proceed();
            Log.d(TAG, "继续往下走");
        } else {
            Log.i(TAG, "停止继续往下走");
        }
        return o;
    }

}
