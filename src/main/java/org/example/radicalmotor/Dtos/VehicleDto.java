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
    /**
     * Chuyển price từ String sang Double.
     *
     * @return Giá trị Double hoặc 0.0 nếu chuyển đổi thất bại
     */
    public Double getPriceAsDouble() {
        try {
            return price != null && !price.isEmpty() ? Double.parseDouble(price) : 0.0;
        } catch (NumberFormatException e) {
            System.err.println("Invalid price format: " + price);
            return 0.0;
        }
    }


    @Override
    public String toString() {
        return "VehicleDto{" +
                "chassisNumber='" + chassisNumber + '\'' +
                ", vehicleName='" + vehicleName + '\'' +
                ", price='" + price + '\'' +
                ", otherFields... }";
    }

}

