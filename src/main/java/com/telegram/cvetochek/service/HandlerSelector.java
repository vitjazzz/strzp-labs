package com.telegram.cvetochek.service;

public interface HandlerSelector {
    CommandHandler select(String command);
}
