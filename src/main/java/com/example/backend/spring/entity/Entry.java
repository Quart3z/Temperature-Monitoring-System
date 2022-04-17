package com.example.backend.spring.entity;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, unique = true)
    private Long timestamp;

    @Column(nullable = false, unique = true)
    private float temperature;

    public Entry(String uuid, Long userId, Long timestamp, float temperature ){
        this.uuid = uuid;
        this.userId = userId;
        this.timestamp = timestamp;
        this.temperature = temperature;
    }

    public Entry(ArrayList<String> entry){

        System.out.println(entry.get(0));
        this.uuid = entry.get(0);
        System.out.println(entry.get(1));
        this.userId = Long.parseLong(entry.get(1));
        System.out.println(entry.get(2));
        this.timestamp = Long.parseLong(entry.get(2));
        System.out.println(entry.get(3));
        this.temperature = Float.parseFloat(entry.get(3));

    }

    public Long getId(){
        return  id;
    }

    public String getUuid(){
        return uuid;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public Long getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(Long timestamp){
        this.timestamp = timestamp;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature){
        this.temperature = temperature;
    }
}
