package org.teamnescafe;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bot {
    public static void main(String[] args) throws ClientException, ApiException, InterruptedException {
        int groupId = 11111111;
        String accessToken = "token";
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vkApiClient = new VkApiClient(transportClient);

        Random random = new Random();

        Keyboard keyboard = new Keyboard();
        List<List<KeyboardButton>> allKeys = new ArrayList<>();
        List<KeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Привет").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        buttonList.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Ботяра, кто я?").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        allKeys.add(buttonList);
        keyboard.setButtons(allKeys);

        GroupActor actor = new GroupActor(groupId, accessToken);
        Integer ts = vkApiClient.messages().getLongPollServer(actor).execute().getTs();
        while (true) {
            MessagesGetLongPollHistoryQuery historyQuery = vkApiClient.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if (!messages.isEmpty()) {
                messages.forEach(message -> {
                    System.out.println(message.toString());
                    try {
                        if (message.getText().equals("Начать")) {
                            vkApiClient.messages().send(actor).message("Ээээ, привет. Если нужно расписание, то введи фамилию человека, которого ты собираешься сталкерить, таким образом:\nРасписание: Имя").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
                        } else if (message.getText().equals("Привет")) {
                            vkApiClient.messages().send(actor).message("Дарова, шизик.").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        } else if (message.getText().equals("Ботяра, кто я?")) {
                            vkApiClient.messages().send(actor).message("Ты шизик, чел").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        } else if (message.getText().matches("^[А-Я]{1}[а-я]{9}[:]{1} [А-Я]{1}[а-я]{1,8}$")) {
                            ExcelInitiator excelInitiator = new ExcelInitiator();
                            vkApiClient.messages().send(actor).message(excelInitiator.find(message.getText().toUpperCase())).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        } else {
                            vkApiClient.messages().send(actor).message("Боже, чееел, я обычный бот, не воспринимающий большинство сообщений. Кнопки и список сообщений для кого созданы?").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                    } catch (ApiException | ClientException exception) {
                        exception.printStackTrace();
                    }
                });
            }
            ts = vkApiClient.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }
    }
}

