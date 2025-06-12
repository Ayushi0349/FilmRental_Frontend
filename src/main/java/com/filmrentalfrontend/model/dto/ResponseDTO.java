
package com.filmrentalfrontend.model.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseDTO {
    private Map<String, String> response = new HashMap<>();

    @JsonAnySetter
    public void setResponse(String key, String value) {
        response.put(key, value);
    }

    public String getMessage() {
        return response.get("message") != null ? response.get("message") :
                response.get("value") != null ? response.get("value") :
                        response.get("error") != null ? response.get("error") :
                                response.values().stream().findFirst().orElse("Unknown response");
    }
}
