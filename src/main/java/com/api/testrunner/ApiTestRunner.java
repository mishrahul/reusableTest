package com.api.testrunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Arrays;
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
        System.out.println(Arrays.toString(args));
        if (args.length == 0) {
            throw new IllegalArgumentException("Insufficient argument");
        }

        String caseFilePath = args[0];
        System.out.println(caseFilePath);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TypeReference<Map<String, List<TestCase>>> typeRef = new TypeReference<>() {};


        File file = new File(caseFilePath);

       // InputStream in = getClass().getClassLoader().getResourceAsStream("testcases.yaml");
        Map<String, List<TestCase>> data = mapper.readValue(file, typeRef);

        List<TestCase> tests = data.get("tests");

        for(TestCase test : tests) service.runTest(test);

        System.exit(0);

    }
}
