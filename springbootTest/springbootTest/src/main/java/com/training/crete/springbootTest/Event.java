package com.training.crete.springbootTest;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Document
public class Event {
    @Id
    private String id;

    private String name;
    private Tag tag;
    private String date;
    private Address address;
    private Date createdAt;
    private List<Double> coordinates;
    private boolean isVisible;
    private BigDecimal priority;

    public Event(String name, Tag tag, String date, Address address, Date createdAt, List<Double> coordinates, boolean isVisible, BigDecimal priority) {
        this.name = name;
        this.tag = tag;
        this.date = date;
        this.address = address;
        this.createdAt = createdAt;
        this.coordinates = coordinates;
        this.isVisible = isVisible;
        this.priority = priority;
    }
}
