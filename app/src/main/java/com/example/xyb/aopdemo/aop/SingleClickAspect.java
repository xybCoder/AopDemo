package com.example.xyb.aopdemo.aop;
import android.view.View;

import com.example.xyb.aopdemo.R;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SingleClickAspect {


    @Pointcut("execution(@com.example.xyb.aopdemo.annotation.SingleClick * *(..))")//方法切入点
    public void executionAspectJ() {
    }

    @Pointcut("execution(@com.example.xyb.aopdemo.annotation.SingleClick *.new(..))")//构造器切入点
    public void constructorAspectJ() {
    }

    @Around("executionAspectJ()||constructorAspectJ")//在连接点进行方法替换
    public void aroundAspectJ(ProceedingJoinPoint joinPoint) throws Throwable {

        View view = null;
        Object[] args = joinPoint.getArgs();
        if(args!=null){
            for(Object obj:args){
                if(obj instanceof View){
                    view = (View)obj;
                    break;
                }
            }
        }
        if(view==null){
            return;
        }
        long lastTime = 0;
        Object object = (Object) view.getTag(R.id.click_throttle);
        if(object instanceof Long){
            lastTime = (Long)object;
        }
        long currentTime = System.currentTimeMillis();
        if(currentTime-lastTime>1000){
            joinPoint.proceed();
            view.setTag(R.id.click_throttle,currentTime);
        }else{
            if(lastTime==0){
                view.setTag(R.id.click_throttle,currentTime);
                joinPoint.proceed();
            }
        }

        return;
    }
}
