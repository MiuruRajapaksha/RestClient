package com.example.demo;


import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    static Logger logger = LoggerFactory.getLogger(RestClient.class);

    public static void main(String[] args) throws URISyntaxException {
//          testGetUserListSuccess(); //to get all Users
        getRequestTest();   //to get all Users
//        postRequestTest(); //to create a test User
//        putRequestTest(16); //to update an existing user
//        deleteRequestTest(16); //to delete an existing user
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
        logger.info("All Users [{}]",result);
    }

    public static void postRequestTest() {

        User newUser = new User("Test", "test@gmail.com");
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> requestBody = new HttpEntity<>(newUser, headers);
        User user = restTemplate.postForObject(URL_USERS, requestBody, User.class);
        if (user != null) {
            logger.info("User created successfully [{}]", user);
        } else {
            logger.error("User not created");
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
        User user = restTemplate.getForObject(URL_USERS_ID, User.class);
        if(user!=null){
            logger.info("User updated successfully [{}]",user.getName());
        }
        else{
            logger.error("User update failed");
        }
    }

    public static void deleteRequestTest(long id) {
        String URL_USERS_ID = "http://localhost:8080/api/users/" + id;
        restTemplate.delete(URL_USERS_ID);
        logger.info("User deleted successfully [{}]",id);
    }
}
