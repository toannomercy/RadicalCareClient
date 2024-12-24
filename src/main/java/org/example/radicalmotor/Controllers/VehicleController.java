package org.example.radicalmotor.Controllers;

import org.example.radicalmotor.Dtos.ApiResponse;
import org.example.radicalmotor.Dtos.FilterGetVm;
import org.example.radicalmotor.Dtos.SearchVehicleGetVm;
import org.example.radicalmotor.Dtos.VehicleDto;
import org.example.radicalmotor.Services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller

@RequestMapping("/vehicles")

public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping()
    public String getAllVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String segment,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Double minCost,
            @RequestParam(required = false) Double maxCost,
            Model model
    ) {
        // Lấy tất cả dữ liệu từ backend
        List<VehicleDto> vehicles = vehicleService.getAllVehiclesWithoutPagination();

        // Lọc theo từ khóa tìm kiếm
        if (keyword != null && !keyword.trim().isEmpty()) {
            vehicles = vehicles.stream()
                    .filter(vehicle -> vehicle.getVehicleName().toLowerCase().contains(keyword.toLowerCase())
                            || vehicle.getColor().toLowerCase().contains(keyword.toLowerCase())
                            || vehicle.getSegment().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Lọc theo segment
        if (segment != null && !segment.trim().isEmpty()) {
            vehicles = vehicles.stream()
                    .filter(vehicle -> vehicle.getSegment().equalsIgnoreCase(segment))
                    .collect(Collectors.toList());
        }

        // Lọc theo color
        if (color != null && !color.trim().isEmpty()) {
            vehicles = vehicles.stream()
                    .filter(vehicle -> vehicle.getColor().equalsIgnoreCase(color))
                    .collect(Collectors.toList());
        }
        // Lọc theo giá tối thiểu
        if (minCost != null) {
            vehicles = vehicles.stream()
                    .filter(vehicle -> vehicle.getPriceAsDouble() >= minCost)
                    .collect(Collectors.toList());
        }
        // Lọc theo giá tối đa
        if (maxCost != null) {
            vehicles = vehicles.stream()
                    .filter(vehicle -> vehicle.getPriceAsDouble() <= maxCost)
                    .collect(Collectors.toList());
        }

        // Phân trang dữ liệu
        int start = page * size;
        int end = Math.min(start + size, vehicles.size());
        List<VehicleDto> pagedVehicles = vehicles.subList(start, end);

        // Đưa dữ liệu vào Model để render trên View
        model.addAttribute("vehicles", pagedVehicles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) vehicles.size() / size));
        model.addAttribute("keyword", keyword);
        model.addAttribute("segment", segment);
        model.addAttribute("color", color);
        model.addAttribute("minCost", minCost);
        model.addAttribute("maxCost", maxCost);

        return "vehicle/index";
    }


    //    @GetMapping("/vehicles")
//    public String getAllVehicles(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "9") int size,
//            Model model
//    ) {
//        // Lấy tất cả dữ liệu từ backend
//        List<VehicleDto> vehicles = vehicleService.getAllVehiclesWithoutPagination();
//
//        // Phân trang dữ liệu ở client
//        int start = page * size;
//        int end = Math.min(start + size, vehicles.size());
//        List<VehicleDto> pagedVehicles = vehicles.subList(start, end);
//
//        model.addAttribute("vehicles", pagedVehicles);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", (int) Math.ceil((double) vehicles.size() / size));
//
//        return "vehicle/index"; // Tên file view trong templates/vehicle/index.html

    @GetMapping("/detail/{id}")
    public String getVehicleDetail(@PathVariable("id") String chassisNumber, Model model) {
        try {
            VehicleDto vehicle = vehicleService.getVehicleDetail(chassisNumber);
            model.addAttribute("vehicle", vehicle);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không tìm thấy thông tin cho xe có mã: " + chassisNumber);
        }
        return "vehicle/detail";
    }

//    @GetMapping()
//    public String getFilteredAndSearchedVehicles(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) Double minPrice,
//            @RequestParam(required = false) Double maxPrice,
//            @RequestParam(required = false) String segment,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "9") int size,
//            Model model) {
//
//        if (keyword != null && !keyword.isEmpty()) {
//            List<SearchVehicleGetVm> searchResults = vehicleService.searchVehicles(keyword);
//            model.addAttribute("vehicles", searchResults);
//        } else {
//            List<FilterGetVm> filteredVehicles = vehicleService.filterVehicles(minPrice, maxPrice, segment, page, size);
//            model.addAttribute("vehicles", filteredVehicles);
//        }
//
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("minPrice", minPrice);
//        model.addAttribute("maxPrice", maxPrice);
//        model.addAttribute("segment", segment);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("pageSize", size);
//
//        return "vehicle/index";
//    }
}




