package com.api.testrunner.model;


import com.api.testrunner.request.RequestData;
import com.api.testrunner.response.ExpectedResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


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

