package com.example.xyb.aopdemo;

public class NotifyMessageManager {
    private static NotifyMessageManager instance;

    public static NotifyMessageManager getInstance() {
        if (instance == null) {
            synchronized (NotifyMessageManager.class) {
                if (instance == null) {
                    instance = new NotifyMessageManager();
                }
            }
        }
        return instance;
    }

    private NotifyMessageListener listener;

    public void setOnHandleMessageListener(NotifyMessageListener listener) {
        this.listener = listener;
    }

    public void postMessage(String msg) {
        listener.onHandleMessage(msg);
    }

    public interface NotifyMessageListener {
        void onHandleMessage(String msg);

    }
}
