package org.example.radicalmotor.Controllers;

import org.example.radicalmotor.Dtos.VehicleDto;
import org.example.radicalmotor.Services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/vehicles")
    public String getAllVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Model model
    ) {
        // Lấy tất cả dữ liệu từ backend
        List<VehicleDto> vehicles = vehicleService.getAllVehiclesWithoutPagination();

        // Phân trang dữ liệu ở client
        int start = page * size;
        int end = Math.min(start + size, vehicles.size());
        List<VehicleDto> pagedVehicles = vehicles.subList(start, end);

        model.addAttribute("vehicles", pagedVehicles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) vehicles.size() / size));

        return "vehicle/index"; // Tên file view trong templates/vehicle/index.html
    }
}
