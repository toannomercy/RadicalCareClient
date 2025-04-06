package org.example.radicalmotor.Services;

import lombok.extern.slf4j.Slf4j;
import org.example.radicalmotor.Dtos.ApiResponse;
import org.example.radicalmotor.Dtos.FilterGetVm;
import org.example.radicalmotor.Dtos.SearchVehicleGetVm;
import org.example.radicalmotor.Dtos.VehicleDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
@Slf4j
@Service
public class VehicleService {

    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    public VehicleService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<VehicleDto> getAllVehiclesWithoutPagination() {
        String url = apiBaseUrl + "/api/v1/vehicles/all";

        // Sử dụng ResponseEntity với ApiResponse<VehicleDto[]>
        ResponseEntity<ApiResponse<VehicleDto[]>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<VehicleDto[]>>() {
                }
        );

        // Lấy danh sách xe từ ApiResponse
        VehicleDto[] vehiclesArray = response.getBody().getData();
        return Arrays.asList(vehiclesArray);
    }

    public VehicleDto getVehicleDetail(String chassisNumber) {
        String url = apiBaseUrl + "/api/v1/vehicle/" + chassisNumber;
        log.info("Calling API to get vehicle detail from URL: {}", url);

        ResponseEntity<ApiResponse<VehicleDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<VehicleDto>>() {
                }
        );

        // Log dữ liệu response từ BE
        ApiResponse<VehicleDto> apiResponse = response.getBody();
        log.info("Response from BE: {}", apiResponse);

        // Lấy dữ liệu từ response và log giá trị của price
        VehicleDto vehicle = Objects.requireNonNull(apiResponse).getData();
        log.info("Vehicle detail received: {}", vehicle);

        return vehicle;
    }

    /*
    public List<FilterGetVm> filterVehicles(Double minPrice, Double maxPrice, String segment, int page, int size) {
        String url = apiBaseUrl + "/api/v1/filter?"
                + (minPrice != null ? "minCost=" + minPrice + "&" : "")
                + (maxPrice != null ? "maxCost=" + maxPrice + "&" : "")
                + (segment != null ? "segment=" + segment + "&" : "")
                + "page=" + page + "&size=" + size;

        ResponseEntity<ApiResponse<List<FilterGetVm>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody().getData();
    }

    public List<SearchVehicleGetVm> searchVehicles(String keyword) {
        String url = apiBaseUrl + "/api/v1/search?keyword=" + keyword;

        ResponseEntity<ApiResponse<List<SearchVehicleGetVm>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody().getData();
    }
    */
}
