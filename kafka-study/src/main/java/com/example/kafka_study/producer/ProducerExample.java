package com.example.kafka_study.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerExample {
	private static final Logger logger = LoggerFactory.getLogger(ProducerExample.class);
	private final static String TOPIC_NAME = "partition-test";
	//	private final static String BOOTSTRAP_SERVER = "172.16.124.107:9092";
	private final static String BOOTSTRAP_SERVER = "localhost:9092";

	public static void main(String[] args) {
		비동기_전송();
	}
	public static void 동기_전송() {
		Properties configs = new Properties();
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

		try {
			for (int i = 0; i < 100; i++) {
				ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME,  "test meesage" + i);
				RecordMetadata metadata = producer.send(record).get();
				logger.debug("topic: {}, partition: {}, offset: {}, key: {}, message: {}", metadata.topic(), metadata.partition(), metadata.offset(), record.key(), record.value());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.close();
		}
	}

	public static void 비동기_전송() {
			Properties configs = new Properties();
			configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
			configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
			configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

			KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

			try {
				for (int i = 1; i <= 100; i++) {
					ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, String.valueOf(i),"meesage" + i);
					producer.send(record, new TestProducerCallback(record));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				producer.close();
			}
	}
	public static void 정확히_한번_전송() {
		Properties configs = new Properties();
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		configs.put(ProducerConfig.ACKS_CONFIG, "all");
		configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
		configs.put(ProducerConfig.RETRIES_CONFIG, "5");
		configs.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "peter-transaction-01"); // 프로세스마다 고유한 아이디로 설정해야함

		KafkaProducer<String, String> producer = new KafkaProducer<>(configs);
		producer.initTransactions(); // 트랜잭션 초기화
		producer.beginTransaction(); // 트랜잭션 중단
		try {
			for(int i = 0;i<1;i++) {
				ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "test meesage" + i);
				producer.send(record);
				producer.flush();
				logger.error("Message send successfully");
			}
		} catch(Exception e) {
			producer.abortTransaction(); // 트랜잭션 중단
			e.printStackTrace();
		} finally {
			producer.commitTransaction();
			producer.close();
		}
	}
	public static class TestProducerCallback implements Callback {
		private ProducerRecord<String, String> record;

		public TestProducerCallback(ProducerRecord<String, String> record) {
			this.record = record;
		}

		@Override
		public void onCompletion(RecordMetadata metadata, Exception e) {
			if(e != null) {
				e.printStackTrace();
			} else {
				logger.debug("topic: {}, partition: {}, offset: {}, key: {}, message: {}",metadata.topic(), metadata.partition(), metadata.offset(), record.key(), record.value());
			}
		}
	}
}
