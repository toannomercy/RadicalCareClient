package org.example.radicalmotor.Dtos;

import lombok.Data;

import java.util.List;

@Data
public class FilterGetVm {
    private String chassisNumber;
    private String vehicleName;
    private String segment;
    private String categoryName;
    private String color;
    private boolean sold;
    private String price;
    private List<String> imageUrls;
}