package org.example.radicalmotor.Daos;

import lombok.Data;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;
@Data
public class Cart {
    private List<Item> cartItems = new ArrayList<>();
    public void addItems(Item item) {
        boolean isExist = cartItems.stream()
                .filter(i -> Objects.equals(i.getChassisNumber(),
                        item.getChassisNumber()))
                .findFirst()
                .map(i -> {
                    i.setQuantity(i.getQuantity() +
                            item.getQuantity());
                    return true;
                })
                .orElse(false);
        if (!isExist) {
            cartItems.add(item);
        }
    }
    public void removeItems(String chassisNumber) {
        cartItems.removeIf(item -> Objects.equals(item.getChassisNumber(),
                chassisNumber));
    }
}
