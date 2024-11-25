package com.example.personalfinance.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a generic response object for API responses.
 * It can include a message, data, or both to provide flexibility in handling various response scenarios.
 */

@Data
@AllArgsConstructor
public class BaseResponse {
    private Object message; // Represents a message, typically for providing feedback or status
    private Object data;    // Represents the response data, which can be of any type

    /**
     * Constructs a BaseResponse with only a message.
     *
     * @param message the message to include in the response
     */
    public BaseResponse(Object message) {
        this.message = message;
    }
}
