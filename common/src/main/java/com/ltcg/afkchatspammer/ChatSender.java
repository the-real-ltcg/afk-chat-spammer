package com.ltcg.afkchatspammer;

@FunctionalInterface
public interface ChatSender {
    void send(String message);
}
