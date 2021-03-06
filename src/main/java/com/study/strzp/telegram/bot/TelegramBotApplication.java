package com.study.strzp.telegram.bot;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.Proxy;

@SpringBootApplication
public class TelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
    }

    @Value("${proxy.host:46.255.15.51}")
    String proxyHost;

    @Value("${proxy.port:33907}")
    int proxyPort;

    @Bean
    @Qualifier("proxyRestTemplate")
    RestTemplate proxyRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        requestFactory.setProxy(proxy);
        return new RestTemplate(requestFactory);
    }

    @Bean
    @Qualifier("restTemplate")
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    TelegramLongPollingBot telegramBot(){
        return new MyTestBot();
    }

    @PostConstruct
    public void start() {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(telegramBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
