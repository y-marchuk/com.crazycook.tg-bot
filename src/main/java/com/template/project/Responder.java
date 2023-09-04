package com.template.project;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.template.project.Buttons.CALLBACK_DATA_L;
import static com.template.project.Buttons.CALLBACK_DATA_M;
import static com.template.project.Buttons.CALLBACK_DATA_MENU;
import static com.template.project.Buttons.CALLBACK_DATA_ORDER;
import static com.template.project.Buttons.CALLBACK_DATA_PRICE;
import static com.template.project.Buttons.CALLBACK_DATA_S;
import static com.template.project.Buttons.CALLBACK_DATA_START;
import static com.template.project.Buttons.defaultRespond;
import static com.template.project.Buttons.respondToMenuButton;
import static com.template.project.Buttons.respondToOrderButton;
import static com.template.project.Buttons.respondToPriceButton;
import static com.template.project.Buttons.respondToSMLButton;
import static com.template.project.Buttons.respondToStartButton;

public class Responder extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SendMessage sendMessage = new SendMessage();
                List<List <InlineKeyboardButton>> buttons = new ArrayList<>();

                String chatId = respondToText(sendMessage, buttons, update);

                if(chatId == null){
                    chatId = respondToButton(sendMessage, buttons, update);
                }

                if(chatId != null) {
                    sendMessage.setChatId(chatId);
                    try {
                        sendApiMethod(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("New update received. " + update);
            }
        }).start();
    }

    private String respondToButton(SendMessage sendMessage, List<List<InlineKeyboardButton>> buttons, Update update) {
        if(update.hasCallbackQuery() && !update.getCallbackQuery().getData().isEmpty()){
            String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
            String callbackData =  update.getCallbackQuery().getData();

            switch (callbackData){
                case CALLBACK_DATA_START:
                    respondToStartButton(sendMessage, buttons);
                    break;
                case CALLBACK_DATA_PRICE:
                    respondToPriceButton(sendMessage, buttons);
                    break;
                case CALLBACK_DATA_ORDER:
                    respondToOrderButton(sendMessage, buttons);
                    break;
                case CALLBACK_DATA_MENU:
                    respondToMenuButton(sendMessage, buttons);
                    break;
                case CALLBACK_DATA_S, CALLBACK_DATA_M, CALLBACK_DATA_L:
                    respondToSMLButton(sendMessage, buttons);
                    break;
                default:
                    System.out.println("User callbackData = "+ callbackData);
            }
            return chatId;
        }
        return null;
    }

    private String respondToText(SendMessage sendMessage, List<List<InlineKeyboardButton>> buttons, Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String username = update.getMessage().getFrom().getUserName();
            String chatId = String.valueOf(update.getMessage().getChatId());

            String userMessage = update.getMessage().getText().toLowerCase(Locale.ROOT);
            switch (userMessage){
                case "/start", "/Start":
                    sendMessage.setText("Привіт, раді тебе вітати в нашому чат-боті для замовлення макарон.");
                    sendMessage.setChatId(chatId);
                    try {
                        sendApiMethod(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    respondToStartButton(sendMessage, buttons);
                    break;
                case "hello", "hi":
                    sendMessage.setText("Hi, " + username + "! You are awesome");
                    break;
                case "contact":
                    sendMessage.setText("Please share your contact");
                    KeyboardRow keyboardRaw = new KeyboardRow();
                    KeyboardButton keyboardButton = new KeyboardButton();
                    keyboardButton.setText("Ok, here is my phone number");
                    keyboardButton.setRequestContact(true);
                    keyboardRaw.add(keyboardButton);
                    List<KeyboardRow> keyboardRowList = List.of(keyboardRaw);
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setKeyboard(keyboardRowList);
                    replyKeyboardMarkup.setOneTimeKeyboard(true);
                    replyKeyboardMarkup.setResizeKeyboard(true);

                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
                    break;
                case CALLBACK_DATA_PRICE:
                    respondToPriceButton(sendMessage, buttons);
                    break;
                default:
                    defaultRespond(sendMessage, buttons);
                    System.out.println("User Message = "+ userMessage);
            }
            if(update.getMessage().hasContact()){
                sendMessage.setText("Thanks for your contact.");
                String phoneNumber = update.getMessage().getContact().getPhoneNumber().trim();
            }
            return chatId;
        }
        return null;
    }

    @Override
    public String getBotToken(){
        return AppProperty.BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return AppProperty.BOT_USERNAME;
    }
}
