package com.api.testrunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ApiTestRunner implements CommandLineRunner {

    @Autowired
    ApiExecuterService service;

    public static void main(String[] args) {
        SpringApplication.run(ApiTestRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TypeReference<Map<String, List<TestCase>>> typeRef = new TypeReference<>() {};

        InputStream in = getClass().getClassLoader().getResourceAsStream("testcases.yaml");
        Map<String, List<TestCase>> data = mapper.readValue(in, typeRef);

        List<TestCase> tests = data.get("tests");

        for(TestCase test : tests) service.runTest(test);

        System.exit(0);

    }
}
