package com.pt.learn.tddexamples.config;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

@Component
@Getter
public class PublishApiConfig implements InitializingBean {

    @Value("${publish.msg.api.endpoint.url}")
    private String publishApiUrl;

    @Value("${token.api.client.id}")
    private String tokenClientId;

    @Value("${token.api.access.key.secret}")
    private String tokenAccessSecret;

    @Value("${token.api.endpoint.url}")
    private String tokenUrl;

    @Value("${token.grant_type}")
    private String grantType;

    @Override
    public void afterPropertiesSet() {
        System.out.println("###################Starting#################");
        Arrays.stream(PublishApiConfig.class.getDeclaredFields()).forEach(this::accept);
        System.out.println("###################End#################");
    }

    private void accept(Field value) {
        try {
            System.out.println(value.get(this).toString());
            if ("java.lang.String".equals(value.getAnnotatedType().toString())) {
                String fieldValue = value.get(this).toString();
                if (fieldValue == null || fieldValue.trim().length() == 0) {
                    throw new RuntimeException("Expected value field is empty"
                            + value.getName());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
