package org.example.radicalmotor.Dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class VehicleDto {
    private String chassisNumber;
    private String vehicleName;
    private LocalDate importDate;
    private String version;
    private String color;
    private String segment;
    private boolean isDeleted;
    private boolean sold;
    private String description;
    private String price;
    private List<String> imageUrls;

    @Override
    public String toString() {
        return "VehicleDto{" +
                "chassisNumber='" + chassisNumber + '\'' +
                ", vehicleName='" + vehicleName + '\'' +
                ", price='" + price + '\'' +
                ", otherFields... }";
    }

}

