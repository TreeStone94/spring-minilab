package com.example.orchestration.config;

import com.example.orchestration.dto.OrderReply;
import com.example.orchestration.dto.PaymentReply;
import com.example.orchestration.dto.StockReply;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    // 공통 Consumer 속성을 설정합니다. (Deserializer 관련 설정은 제외)
    private Map<String, Object> consumerProps(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return props;
    }

    // 특정 타입을 위한 JsonDeserializer를 생성하고 설정합니다.
    private <T> JsonDeserializer<T> createJsonDeserializer(Class<T> targetType) {
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(targetType);
        deserializer.setUseTypeHeaders(false); // 타입 헤더는 사용하지 않습니다.
        deserializer.addTrustedPackages("*");   // 모든 패키지를 신뢰하도록 설정합니다.
        return deserializer;
    }

    // OrderReply를 위한 전용 팩토리
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderReply> orderReplyListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderReply> factory = new ConcurrentKafkaListenerContainerFactory<>();
        Map<String, Object> props = consumerProps("order-reply-group");
        // Deserializer 인스턴스를 직접 생성하여 ConsumerFactory에 전달합니다.
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), createJsonDeserializer(OrderReply.class)));
        return factory;
    }

    // PaymentReply를 위한 전용 팩토리
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentReply> paymentReplyListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentReply> factory = new ConcurrentKafkaListenerContainerFactory<>();
        Map<String, Object> props = consumerProps("payment-reply-group");
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), createJsonDeserializer(PaymentReply.class)));
        return factory;
    }

    // StockReply를 위한 전용 팩토리
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StockReply> stockReplyListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StockReply> factory = new ConcurrentKafkaListenerContainerFactory<>();
        Map<String, Object> props = consumerProps("stock-reply-group");
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), createJsonDeserializer(StockReply.class)));
        return factory;
    }
}
