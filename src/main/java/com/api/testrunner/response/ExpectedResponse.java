package com.api.testrunner.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpectedResponse {
    private int status;
    private Object body;
}
