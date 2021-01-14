package com.example.demo;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@SpringBootApplication
public class RestClient {
    static final String URL_USERS = "http://localhost:8080/api/users";
    static RestTemplate restTemplate = new RestTemplate();
    static HttpHeaders headers = new HttpHeaders();
    static Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws URISyntaxException {
//          testGetUserListSuccess();
//        getRequestTest();
        postRequestTest();
//        putRequestTest(16);
//        deleteRequestTest(16);
    }

    @Test
    public static void testGetUserListSuccess() throws URISyntaxException
    {
        URI uri = new URI(URL_USERS);
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("id"));
    }

    public static void getRequestTest() {
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);// Request to return JSON format
        HttpEntity<String> entity = new HttpEntity<String>(headers);// HttpEntity<String>: To get result as String.
        ResponseEntity<String> response = restTemplate.exchange(URL_USERS, HttpMethod.GET, entity, String.class);// Send request with GET method, and Headers.
        String result = response.getBody();
        logger.log(Level.INFO,result);
    }

    public static void postRequestTest() {

        User newUser = new User("Test", "test@gmail.com");
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Data attached to the request.
        HttpEntity<User> requestBody = new HttpEntity<>(newUser, headers);
        // Send request with POST method.
        User e = restTemplate.postForObject(URL_USERS, requestBody, User.class);
        if (e != null) {
            logger.log(Level.INFO,"User created: " + e.getId());
        } else {
            logger.log(Level.ERROR,"Something error!");
        }
    }

    public static void putRequestTest(int id) {
        String URL_USERS_ID = "http://localhost:8080/api/users/" + id;
        User updateInfo = new User("Miuru", "test@mail");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        // Data attached to the request.
        HttpEntity<User> requestBody = new HttpEntity<>(updateInfo, headers);
        // Send request with PUT method.
        restTemplate.put(URL_USERS_ID, requestBody, new Object[]{});
        User e = restTemplate.getForObject(URL_USERS_ID, User.class);
        logger.log(Level.INFO,"(Client side) Employee after update: ");
        logger.log(Level.INFO,"Employee: " + e.getName());
    }

    public static void deleteRequestTest(long id) {
        String URL_USERS_ID = "http://localhost:8080/api/users/" + id;
        restTemplate.delete(URL_USERS_ID);
    }
}
