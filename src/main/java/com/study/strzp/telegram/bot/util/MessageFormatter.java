package com.study.strzp.telegram.bot.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;

public class MessageFormatter {
    public static InlineKeyboardMarkup REPLY_BUTTONS = new InlineKeyboardMarkup()
            .setKeyboard(
                    Arrays.asList(
                            Arrays.asList(
                                    new InlineKeyboardButton("Курс валют")
                                            .setCallbackData("/currency"),
                                    new InlineKeyboardButton("Банкомати")
                                            .setCallbackData("/atm")
                            )
                    )
            );

    public static void addButtons(SendMessage sendMessage) {
        sendMessage.setReplyMarkup(REPLY_BUTTONS);
    }
}
