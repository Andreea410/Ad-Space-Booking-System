package com.bookingsystem.controllers;

import com.bookingsystem.model.AdSpace;
import com.bookingsystem.model.AdSpaceType;
import com.bookingsystem.service.AdSpaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ad-spaces")
public class AdSpaceController {

    private final AdSpaceService adSpaceService;

    public AdSpaceController(AdSpaceService adSpaceService) {
        this.adSpaceService = adSpaceService;
    }

    /**
     * GET /api/v1/ad-spaces
     *
     * Lists all AVAILABLE ad spaces.
     * Supports optional filtering by type and city via query parameters.
     *
     * Examples:
     *  - /api/v1/ad-spaces
     *  - /api/v1/ad-spaces?city=Bucharest
     *  - /api/v1/ad-spaces?type=BILLBOARD
     *  - /api/v1/ad-spaces?type=BILLBOARD&city=Bucharest
     */
    @GetMapping
    public List<AdSpace> getAdSpaces(
            @RequestParam(name = "type", required = false) String typeParam,
            @RequestParam(name = "city", required = false) String city
    ) {
        AdSpaceType type = null;
        if (typeParam != null && !typeParam.isBlank()) {
            try {
                type = AdSpaceType.valueOf(typeParam.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Invalid 'type' parameter. Allowed values: " +
                                java.util.Arrays.toString(AdSpaceType.values())
                );
            }
        }

        return adSpaceService.searchAdSpaces(city, type);
    }

    /**
     * GET /api/v1/ad-spaces/{id}
     *
     * Returns details for a single ad space.
     * - 200 OK with ad space data when found
     * - 404 Not Found when the id does not exist (handled by GlobalExceptionHandler)
     */
    @GetMapping("/{id}")
    public AdSpace getAdSpaceById(@PathVariable Long id) {
        return adSpaceService.getAdSpaceById(id);
    }
}
