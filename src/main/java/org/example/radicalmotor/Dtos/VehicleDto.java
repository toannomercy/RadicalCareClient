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
    private Long costId;
    private String price;
    private Long categoryId;
    private Long supplierId;
    private String description;
    private List<String> warrantyHistory;
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

}
