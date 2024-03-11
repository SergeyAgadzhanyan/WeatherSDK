package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Geo wit lat and long for weather api
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Geo {
    private String lat;
    private String lon;
}
