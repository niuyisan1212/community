package com.example.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Author INSLYAB
 * @Date 2022/6/27 10:00
 */
@Configuration
public class KaptchaConfig {

    @Value("${kaptcha.image.width}")
    private String imageWidth;

    @Value("${kaptcha.image.height}")
    private String imageHeight;

    @Value("${kaptcha.textproducer.font.size}")
    private String textproducerFontSize;

    @Value("${kaptcha.textproducer.font.color}")
    private String textproducerFontColor;

    @Value("${kaptcha.textproducer.char.string}")
    private String textproducerCharString;

    @Value("${kaptcha.textproducer.char.length}")
    private String textproducerCharLength;

    @Value("${kaptcha.noise.impl}")
    private String noiseImpl;

    @Bean
    public Producer kaptchaProducer(){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width",imageWidth);
        properties.setProperty("kaptcha.image.height",imageHeight);
        properties.setProperty("kaptcha.textproducer.font.size",textproducerFontSize);
        properties.setProperty("kaptcha.textproducer.font.color",textproducerFontColor);
        properties.setProperty("kaptcha.textproducer.char.string",textproducerCharString);
        properties.setProperty("kaptcha.textproducer.char.length",textproducerCharLength);
        properties.setProperty("kaptcha.noise.impl",noiseImpl);

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
