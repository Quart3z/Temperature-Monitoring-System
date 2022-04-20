package com.example.backend.spring.entity;

import com.example.backend.spring.repository.DeviceRepository;
import com.example.backend.spring.repository.EntryRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Entity
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

    @JsonIgnore
    @Transient
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    /**
     * =================
     * Class constructor
     * =================
     */
    public Entry() {
    }

    public Entry(Device device, Long timestamp, float temperature) {
        this.device = device;
        this.timestamp = timestamp;
        this.temperature = temperature;
    }

    public Entry(ArrayList<String> entry, Device device) {
        this.device = device;
        this.timestamp = Long.parseLong(entry.get(2));
        this.temperature = Float.parseFloat(entry.get(3));
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

    public Long getDeviceId(){
        return device.getId();
    }

    public void setDevice(Device device) {
        this.device = device;
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

}
