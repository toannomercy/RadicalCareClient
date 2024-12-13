package org.example.radicalmotor.Daos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String chassisNumber;
    private String vehicleName;
    private Double price;
    private int quantity;
}
