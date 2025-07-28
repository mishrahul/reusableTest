package com.api.testrunner.request;


import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Setter
@Getter
public class RequestData {
    private Map<String, String> headers;
    private Object body;
}
