package com.api.testrunner;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {

    private String name;
    private String method;
    private String url;
    private RequestData request;
    private ExpectedResponse response;
}

