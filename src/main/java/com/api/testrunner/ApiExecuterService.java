package com.api.testrunner;

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

            if (response.getStatusCodeValue() != test.getResponse().getStatus()) {
                System.out.printf("%s: expected %d but got %d%n",
                        test.getName(), test.getResponse().getStatus(), response.getStatusCodeValue());
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
                                test.getName(), expectedMap, actualMap);
                        return;
                    }
                }
                else if (expected instanceof String) {
                    if (!actualBody.trim().equals(expected.toString().trim())) {
                        System.out.printf(
                                "\nTest Name: %s \nResult: Assertion Failed - Message Mismatch\nExpected: \"%s\"\nActual: \"%s\"\n",
                                test.getName(), expected, actualBody);
                        return;
                    }
                }
            }

            System.out.printf(
                    "\nTest Name: %s \nResult: Passed \nActual status code: %d\n",
                    test.getName(), test.getResponse().getStatus());

        } catch (Exception e) {
            System.out.printf("\n%s: EXCEPTION: %s\n", test.getName(), e.getMessage());
        }
    }
}
