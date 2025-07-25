package com.api.testrunner;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpectedResponse {
    private int status;
    private Object body;
}
