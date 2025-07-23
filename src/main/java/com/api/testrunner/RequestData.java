package com.api.testrunner;


import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Setter
@Getter
public class RequestData {
    private Map<String, String> headers;
    private Object body;
}
