package com.naveen.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;

import java.io.File;

@Configuration
@EnableIntegration
public class IntegrationConfig {

    private final Transformer transformer;

    public IntegrationConfig(Transformer transformer) {
        this.transformer = transformer;
    }

    @Bean
    public IntegrationFlow integrationFlow() {
        return IntegrationFlows.from(fileReader(),
                spec -> spec.poller(Pollers.fixedDelay(500)))
                .transform(transformer, "transform")
                .handle(fileWriter())
                .get();
    }

    @Bean
    public FileWritingMessageHandler fileWriter() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(
                new File("destination")
        );
        handler.setExpectReply(false);
        return handler;
    }

    @Bean
    public FileReadingMessageSource fileReader() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("source"));
        return source;
    }

}
