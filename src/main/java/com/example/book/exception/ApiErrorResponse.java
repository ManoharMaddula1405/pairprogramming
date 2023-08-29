package com.example.book.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse {
    private Integer status;
    private String msg;
    private Long timeStamp;
}
