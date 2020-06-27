package com.telegram.cvetochek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class HandlerSelectorImpl implements HandlerSelector {

    @Qualifier("defaultHandler")
    @Autowired
    private CommandHandler defaultHandler;

    @Qualifier("subscriptionHandler")
    @Autowired
    private CommandHandler subscriptionHandler;

    @Override
    public CommandHandler select(String command) {
        switch (command) {
            case "/start":
                return subscriptionHandler;
            default:
                return defaultHandler;
        }
    }
}
