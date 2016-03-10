package com.rookiefly.test.commons.token;

import org.isomorphism.util.TokenBucket;
import org.isomorphism.util.TokenBuckets;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by rookiefly on 2015/9/29.
 */
public class TokenBucketTest {

	@Test
	public void test1() {
		// Create a token bucket with a capacity of 1 token that refills at a fixed interval of 1 token/sec.
		TokenBucket bucket = TokenBuckets.builder().withCapacity(1)
		        .withFixedIntervalRefillStrategy(1, 1, TimeUnit.SECONDS).build();

		while (true) {
			// Consume a token from the token bucket. If a token is not available this method will block until
			// the refill strategy adds one to the bucket.
			bucket.consume(1);

			poll();
		}
	}

	private void poll() {
		System.out.println("poll.....");
	}

	public void test2() {
		// Create a token bucket with a capacity of 40 kb tokens that refills at a fixed interval of 20 kb tokens per
		// second
		TokenBucket bucket = TokenBuckets.builder().withCapacity(40960)
		        .withFixedIntervalRefillStrategy(20480, 1, TimeUnit.SECONDS).build();

		while (true) {
			String response = prepareResponse();

			// Consume tokens from the bucket commensurate with the size of the response
			bucket.consume(response.length());

			send(response);
		}
	}

	private void send(String response) {
		System.out.println("send response");
	}

	private String prepareResponse() {
		return null;
	}

}
