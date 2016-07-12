package com.rookiefly.test.commons.http;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by rookiefly on 2016/2/1.
 */
public class HttpTest {

    @Test
    public void testAsync() {

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Future<Response> f = asyncHttpClient.prepareGet("http://www.ning.com/").execute();
        try {
            Response r = f.get();
            System.out.println(r.getStatusCode());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsync02() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Future<Integer> f = asyncHttpClient.prepareGet("http://www.ning.com/").execute(
                new AsyncCompletionHandler<Integer>() {

                    @Override
                    public Integer onCompleted(Response response) throws Exception {
                        // Do something with the Response
                        return response.getStatusCode();
                    }

                    @Override
                    public void onThrowable(Throwable t) {
                        // Something wrong happened.
                    }
                });

        try {
            System.out.println("Response Code: " + f.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsync03() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.prepareGet("http://www.ning.com/").execute(new AsyncCompletionHandler<Response>() {

            @Override
            public Response onCompleted(Response response) throws Exception {
                // Do something with the Response
                System.out.println("Response Code: " + response.getStatusCode());
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                // Something wrong happened.
            }
        });
    }

    public static void main(String[] args) {
        final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.prepareGet("http://www.ning.com/").execute(new AsyncCompletionHandler<Response>() {

            @Override
            public Response onCompleted(Response response) throws Exception {
                asyncHttpClient.closeAsynchronously();
                // Do something with the Response
                System.out.println("Response Code: " + response.getStatusCode());
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                // Something wrong happened.
            }
        });
    }
}
