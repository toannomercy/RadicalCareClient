package org.example.radicalmotor.Dtos;

import lombok.Data;
import java.util.List;

@Data
public class SearchVehicleGetVm {
    private String chassisNumber;
    private String vehicleName;
    private String version;
    private String segment;
    private String color;
    private List<String> imageUrls;
    private Double cost;
    private String category;
}
