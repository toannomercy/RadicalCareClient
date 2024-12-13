package org.example.radicalmotor.Dtos;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private T data;
    private String message;
    private int status;
}

