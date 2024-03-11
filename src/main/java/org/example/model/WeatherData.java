package org.example.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Weather weather;
    @OneToOne(cascade = CascadeType.ALL)
    private Temperature temperature;
    private double visibility;
    @OneToOne(cascade = CascadeType.ALL)
    private Wind wind;
    private long datetime;
    @OneToOne(cascade = CascadeType.ALL)
    private Sys sys;
    private long timezone;
    private String name;


    @JsonGetter("datetime")
    public long getDatetime() {
        return datetime;
    }

    @JsonSetter("dt")
    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    @JsonGetter("temperature")
    public Temperature getTemperature() {
        return temperature;
    }

    @JsonSetter("main")
    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }


}
