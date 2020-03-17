
package com.damaru.scs.dyn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@SpringBootApplication
public class BadApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(BadApplication.class);
	private final EmitterProcessor<Message<String>> emitterProcessor = EmitterProcessor.create();
	private static final String SPRING_DESTINATION_HEADER = "spring.cloud.stream.sendto.destination";


	public static void main(String[] args) {
		SpringApplication.run(BadApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
		int i = 0;
		while (true) {
			String topic = "topic/" + i++;
			Message<String> message = MessageBuilder.withPayload(topic).setHeader(SPRING_DESTINATION_HEADER, topic).build();
			emitterProcessor.onNext(message);
			Thread.sleep(1);
		}
	}

	@Bean
	public Supplier<Flux<Message<String>>> supplier() {
		return () -> emitterProcessor;
	}

}
