package com.api.testrunner;

import com.api.testrunner.model.TestCase;
import com.api.testrunner.service.ApiExecuterService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ApiTestRunner implements CommandLineRunner {

    @Autowired
    ApiExecuterService service;

    @Autowired
    private ApplicationContext context;

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

        File file;
        try {
            file = new File(caseFilePath);
            if(!file.exists()) throw new FileNotFoundException("\nUnable to fetch the testcase file. Ensure that the file exists\n");
            if(!FilenameUtils.getExtension(caseFilePath).equals("yaml")) throw new IllegalArgumentException("\nAttached file is not a .yaml file\n");

        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            SpringApplication.exit(context, () -> 0);
            return;
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            SpringApplication.exit(context, () -> 0);
            return;
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            SpringApplication.exit(context, () -> 0);
            return;
        }


       // InputStream in = getClass().getClassLoader().getResourceAsStream("testcases.yaml");
        Map<String, List<TestCase>> data = mapper.readValue(file, typeRef);

        List<TestCase> tests = data.get("tests");

        for(TestCase test : tests) service.runTest(test);

        System.exit(0);

    }
}
