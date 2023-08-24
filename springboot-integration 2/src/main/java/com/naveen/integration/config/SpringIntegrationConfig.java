package com.naveen.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;

import java.io.File;

@Configuration
@EnableIntegration
public class SpringIntegrationConfig {

    @Bean
    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "500"))
    public FileReadingMessageSource fileReadingMessageSource() {
      //  CompositeFileListFilter<File> filter=new CompositeFileListFilter<>();
       // filter.addFilter(new SimplePatternFileListFilter("*.png"));
        FileReadingMessageSource readder = new FileReadingMessageSource();
        readder.setDirectory(new File("source"));
       // readder.setFilter(filter);
        return readder;
    }

    @Bean
    @ServiceActivator(inputChannel = "fileInputChannel")
    public FileWritingMessageHandler fileWritingMessageHandler() {
        FileWritingMessageHandler writer =
                new FileWritingMessageHandler(new File("destination"));
        writer.setAutoCreateDirectory(true);
        writer.setExpectReply(false);
        return writer;
    }
}
