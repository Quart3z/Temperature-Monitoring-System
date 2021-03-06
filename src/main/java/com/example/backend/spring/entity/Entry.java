package com.example.backend.spring.entity;

import com.example.backend.spring.repository.DeviceRepository;
import com.example.backend.spring.repository.EntryRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class Entry {

    /**
     * ====================
     * Attributes & columns
     * ====================
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many-to-one relationship with Device table
     * foreign key - uuid
     */
    @ManyToOne(targetEntity = Device.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "device_id")
    private Device device;

    @Column(nullable = false)
    private Long timestamp;

    @Column(nullable = false)
    private float temperature;

    @Column(nullable = false)
    private float humidity;

    @Column(nullable = false)
    private float precipitation;

    @Column(nullable = false)
    private float windSpeed;

    /**
     * =================
     * Class constructor
     * =================
     */
    public Entry() {
    }

    public Entry(Device device, Long timestamp, float temperature, float humidity, float precipitation, float windSpeed) {
        this.device = device;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
        this.windSpeed = windSpeed;
    }

    public Entry(ArrayList<String> entry, Device device) {
        this.device = device;
        this.timestamp = Long.parseLong(entry.get(2));
        this.temperature = Float.parseFloat(entry.get(3));
        this.humidity = 0;
        this.precipitation = 0;
        this.windSpeed = 0;
    }

    /**
     * =======================
     * Accessors and mutators
     * =======================
     */
    public Long getId() {
        return id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Long getDeviceId() {
        return device.getId();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(float precipitation) {
        this.precipitation = precipitation;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String[] returnAsStringArray() {

        return new String[]{
                String.valueOf(temperature),
        };

    }

}
