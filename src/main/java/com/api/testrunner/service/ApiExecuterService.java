package com.api.testrunner.service;

import com.api.testrunner.model.TestCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class ApiExecuterService {

    ObjectMapper mapper = new ObjectMapper();

//    public void rnTest(TestCase test) {
//        RestTemplate template = new RestTemplate();
//        template.setErrorHandler(new DefaultResponseErrorHandler() {
//            @Override
//            public boolean hasError(ClientHttpResponse response) { return false; }
//
//            @Override
//            public void handleError(ClientHttpResponse response) {}
//        });
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Object> entity = new HttpEntity<>(test.getRequest().getBody(), headers);
//
//        try {
//            ResponseEntity<String> response = template.exchange(
//                    test.getUrl(),
//                    HttpMethod.valueOf(test.getMethod()),
//                    entity,
//                    String.class
//            );
//
//            //System.out.println("\n" + test.getName() + " - " + response.getBody());
//
//            if (response.getStatusCodeValue() != test.getResponse().getStatus()) {
//
//                System.out.println(response.getStatusCodeValue() != test.getResponse().getStatus());
//                System.out.println(response.getStatusCodeValue() +"\n"+ test.getResponse().getStatus());
//                System.out.printf("\nTest Name: %s \nResponse Status Mismatch: Expected %d but got %d \nResponse Body: %s",
//                        test.getName(), test.getResponse().getStatus(), response.getStatusCodeValue(), response.getBody());
//                return;
//            }
//
//            String actualBody = response.getBody();
//            Object expected = test.getResponse().getBody();
//
//            if (expected != null) {
//                Object actualParsed = mapper.readValue(actualBody, Object.class);
//
//                if (!expected.equals(actualParsed)) {
//                    System.out.printf(
//                            "\nTest Name: %s \nResult: Assertion Failed - Body Mismatch\nExpected: %s\nActual: %s\n",
//                            test.getName(), expected, actualParsed);
//                    return;
//                }
//            }
//
//            System.out.printf(
//                    "\nTest Name: %s \nResult: Passed \nActual status code: %d\n",
//                    test.getName(), test.getResponse().getStatus());
//
//        } catch (Exception e) {
//            System.out.printf("\n%s: EXCEPTION: %s\n", test.getName(), e.getMessage());
//        }
//    }


    public void runTest(TestCase test) {
        RestTemplate template = new RestTemplate();


        template.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) { return false; }

            @Override
            public void handleError(ClientHttpResponse response) {

            }
        });
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(test.getRequest().getBody(), headers);

        try {
            ResponseEntity<String> response = template.exchange(
                    test.getUrl(),
                    HttpMethod.valueOf(test.getMethod()),
                    entity,
                    String.class
            );


           // System.out.println("\n" +test.getName() +" - " +response.getBody());
            if (response.getStatusCodeValue()!=test.getResponse().getStatus()) {
                System.out.printf("\n%s: expected %d but got %d",
                        test.getName(), test.getResponse().getStatus(), response.getStatusCodeValue()
                );
                return;
            }

            //System.out.println("\n" + test.getName());
            String actualBody = response.getBody();

            Object expected = test.getResponse().getBody();
            if (expected != null) {
                if (expected instanceof Map) {
                    Map<String, Object> actualMap = mapper.readValue(actualBody, Map.class);
                    Map<String, Object> expectedMap = (Map<String, Object>) expected;

                    if (!expectedMap.equals(actualMap)) {
                        System.out.printf(
                                "\nTest Name: %s \nResult: Assertion Failed - Body Mismatch\nExpected: %s\nActual: %s\n",
                                test.getName(), expectedMap, actualMap
                        );
                        return;

                    }
                }
                else if (expected instanceof String) {
                    if (!actualBody.trim().equals(expected.toString().trim())) {
                        System.out.printf(
                                "\nTest Name: %s \nResult: Assertion Failed - Message Mismatch\nExpected: \"%s\"\nActual: \"%s\"\n",
                                test.getName(), expected, actualBody
                        );
                        return;
                    }
                }

            }

            System.out.printf(
                    "\nTest Name: %s \nResult: Passed \nActual status code: %d\n",
                    test.getName(), test.getResponse().getStatus());

        }
        catch (JsonProcessingException e) {
            System.out.println("\n"+e.getMessage()+"\n");
        }

        catch (Exception e) {
            System.out.printf("\n%s: EXCEPTION: %s\n", test.getName(), e.getMessage());
        }
    }
}
