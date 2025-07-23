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
        RestTemplate template =  new RestTemplate();

        template.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }
            @Override
            public void handleError(ClientHttpResponse response) {

            }
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(test.getRequest().getBody(), headers);

        try {
            ResponseEntity<String> response =  template.exchange(test.getUrl(), HttpMethod.valueOf(test.getMethod()),
                    entity, String.class);

//            System.out.println(response);

            if(response.getStatusCodeValue() != test.getResponse().getStatus()) {
                System.out.printf("%s: expected %d but got %d%n",
                        test.getName(), test.getResponse().getStatus(), response.getStatusCodeValue());

            }
            else {

                System.out.println("\n\n"+test.getName());
                String actualBody = response.getBody();

//                System.out.println("\n---------------------------------------------------\n" +test.getName());
                System.out.println("actualbody: " +actualBody);
                System.out.println("expected response: "+test.getResponse().getMessage());

                if(test.getResponse().getBody() != null || test.getResponse().getMessage()!=null) {
                    Map<String, Object> actualMap = mapper.readValue(actualBody, Map.class);
                    Map<String, Object> expectedMap = test.getResponse().getBody();

                    System.out.println(actualBody.toString()+" - "+ expectedMap.toString());
                    if(!expectedMap.equals(actualMap)) {
                        System.out.printf("\nTest Name: %s \nResult: Assertion Failed - Body Mismatch\nExpected: %s \nActual: %s", test.getName(), expectedMap, actualMap);
                        return;
                    }
                }

                System.out.printf("\nTest Name: %s \nResult: Passed \nActual status code: %d\n", test.getName(), test.getResponse().getStatus());
            }
        }

        catch (Exception e) {
            System.out.printf("\n<-----Catch block---->%n%s: %d%n %s %s", test.getName(), test.getResponse().getStatus(), test.getResponse().getBody(), e.getMessage());

        }
    }
}
