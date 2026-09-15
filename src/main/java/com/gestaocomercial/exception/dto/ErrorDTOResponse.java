package com.gestaocomercial.exception.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ErrorDTOResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;






}
