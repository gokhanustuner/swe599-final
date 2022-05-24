package com.swe599final.mdm;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.swe599final.mdm.domain.repository.UserRepository;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.swe599final.mdm.domain.service.AndroidManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@Controller
public class MdmApplication {
	private static final Log LOGGER = LogFactory.getLog(MdmApplication.class);
	private static final Random rand = new Random(2020);

	public static void main(String[] args) {
		SpringApplication.run(MdmApplication.class, args);
	}

	/**

	@Bean
	public MessageChannel inputMessageChannel() {
		return new DirectChannel();
	}

	@Bean
	public PubSubInboundChannelAdapter inboundChannelAdapter(
		@Qualifier("inputMessageChannel") MessageChannel messageChannel,
		PubSubTemplate pubSubTemplate
	) {
		PubSubInboundChannelAdapter adapter =
				new PubSubInboundChannelAdapter(pubSubTemplate, "projects/swe578/subscriptions/device_enrollment-sub");
		adapter.setOutputChannel(messageChannel);
		adapter.setAckMode(AckMode.MANUAL);
		adapter.setPayloadType(String.class);
		return adapter;
	}

	@ServiceActivator(inputChannel = "inputMessageChannel")
	public MessageHandler messageReceiver(
	) {
		return message -> {
			LOGGER.info("Message arrived! Payload: " + new String((byte[]) message.getPayload()));
			BasicAcknowledgeablePubsubMessage originalMessage =
					message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);
			originalMessage.ack();
		};
	}

	@Bean
	@ServiceActivator(inputChannel = "inputMessageChannel")
	public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
		return new PubSubMessageHandler(pubsubTemplate, "projects/swe578/topics/device_enrollment");
	}

	@Bean
	public Consumer<Message<String>> receiveMessageFromTopicTwo() {
		return message -> {
			LOGGER.info(
				"Message arrived via an input binder from topic-two! Payload: " + message.getPayload());
		};
	}

	@Bean
	public Supplier<Flux<Message<String>>> sendMessageToTopicOne() {
		return () ->
			Flux.<Message<String>>generate(
				sink -> {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// Stop sleep earlier.
					}

					Message<String> message = MessageBuilder.withPayload("message-" + rand.nextInt(1000)).build();
					LOGGER.info(
						"Sending a message via the output binder to topic-one! Payload: " + message.getPayload()
					);
					sink.next(message);
				}
			).subscribeOn(Schedulers.boundedElastic());
	}
	*/
}
