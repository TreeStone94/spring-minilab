package com.example.kafka_study.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerExample2 {
	private static final Logger logger = LoggerFactory.getLogger(ConsumerExample2.class);
	private final static String TOPIC_NAME = "tps-test";
	//	private final static String BOOTSTRAP_SERVER = "172.16.124.107:9092";
	private final static String BOOTSTRAP_SERVER = "localhost:9092";
	private final static String GROUP_ID = "tps-test-group";
	private static final int MESSAGE_COUNT = 1000;


	public static void main(String[] args) {
		처리량_설정();
	}

	public static void 오토_커밋() {
		Properties configs = new Properties();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // 컨슈머 오프셋을 찾지 못하는 경우 latest로 초기화하며 가장 최근부터 메시지 가져옴
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(configs);
		consumer.subscribe(Arrays.asList(TOPIC_NAME));

		try {
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
				for (ConsumerRecord<String, String> record : records) {
					logger.error("Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}", record.topic(), record.partition(), record.offset(), record.key(), record.value());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}

	public static void 동기_커밋() {
		Properties configs = new Properties();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // 컨슈머 오프셋을 찾지 못하는 경우 latest로 초기화하며 가장 최근부터 메시지 가져옴
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(configs);
		consumer.subscribe(Arrays.asList(TOPIC_NAME));

		try {
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
				for (ConsumerRecord<String, String> record : records) {
					logger.error("Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}", record.topic(), record.partition(), record.offset(), record.key(), record.value());
				}
				consumer.commitSync(); // 동기
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}

	public static void 비동기_커밋() {
		Properties configs = new Properties();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // 컨슈머 오프셋을 찾지 못하는 경우 latest로 초기화하며 가장 최근부터 메시지 가져옴
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(configs);
		consumer.subscribe(Arrays.asList(TOPIC_NAME));

		try {
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
				for (ConsumerRecord<String, String> record : records) {
					logger.error("Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}", record.topic(), record.partition(), record.offset(), record.key(), record.value());
				}
				consumer.commitAsync(); // 비동기
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}


	public static void 처리량_설정() {
		Properties configs = new Properties();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		configs.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "1000");
		configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
		configs.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "10485760");


		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(configs);
		consumer.subscribe(Arrays.asList(TOPIC_NAME));

		AtomicInteger count = new AtomicInteger(0);
        long[] startTime = new long[1];


		try {
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
				int recordCount = records.count();
				if (recordCount == 0) continue;

				if (count.get() == 0) {
					startTime[0] = System.nanoTime(); // 첫 수신 시간 기록
				}

				for (ConsumerRecord<String, String> record : records) {
					// 처리 시뮬레이션
					Thread.sleep(10);
				}

				int total = count.addAndGet(recordCount);
				System.out.printf("[x] Polled %d, Total: %d%n", recordCount, total);

				if (total >= MESSAGE_COUNT) {
					long endTime = System.nanoTime();
					long elapsedMs = (endTime - startTime[0]) / 1_000_000;
					System.out.printf("[✓] Received %d messages in %d ms%n", total, elapsedMs);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}
}
