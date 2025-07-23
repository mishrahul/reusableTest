package com.api.testrunner;


import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.Map;

@Getter
@Setter
public class ExpectedResponse {
    private int status;
    private Object body;
    //private String message;
}
