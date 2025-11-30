package com.bookingsystem.controllers;

import com.bookingsystem.model.AdSpace;
import com.bookingsystem.model.AdSpaceType;
import com.bookingsystem.service.AdSpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
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
     * Supports optional sorting by sortBy and sortOrder query parameters.
     *
     * Examples:
     *  - /api/v1/ad-spaces
     *  - /api/v1/ad-spaces?city=Bucharest
     *  - /api/v1/ad-spaces?type=BILLBOARD
     *  - /api/v1/ad-spaces?type=BILLBOARD&city=Bucharest
     *  - /api/v1/ad-spaces?sortBy=pricePerDay&sortOrder=asc
     */
    @GetMapping
    public List<AdSpace> getAdSpaces(
            @RequestParam(name = "type", required = false) String typeParam,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "asc") String sortOrder
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

        List<AdSpace> adSpaces = adSpaceService.searchAdSpaces(city, type);

        if (sortBy != null && !sortBy.isBlank()) {
            Comparator<AdSpace> comparator = getComparator(sortBy);
            if ("desc".equalsIgnoreCase(sortOrder)) {
                comparator = comparator.reversed();
            }
            adSpaces.sort(comparator);
        }

        return adSpaces;
    }

    private Comparator<AdSpace> getComparator(String sortBy) {
        String normalized = sortBy.toLowerCase().replaceAll("_", "");
        return switch (normalized) {
            case "name" -> Comparator.comparing(AdSpace::getName, String.CASE_INSENSITIVE_ORDER);
            case "city" -> Comparator.comparing(AdSpace::getCity, String.CASE_INSENSITIVE_ORDER);
            case "priceperday", "price" -> Comparator.comparing(AdSpace::getPricePerDay);
            case "type" -> Comparator.comparing(adSpace -> adSpace.getType().name());
            default -> throw new IllegalArgumentException(
                    "Invalid 'sortBy' parameter. Allowed values: name, city, pricePerDay, type"
            );
        };
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

    /**
     * PATCH /api/v1/ad-spaces/{id}
     *
     * Updates an ad space's name.
     * - 200 OK with updated ad space
     * - 404 Not Found if ad space doesn't exist
     */
    @PatchMapping("/{id}")
    public AdSpace updateAdSpace(
            @PathVariable Long id,
            @RequestBody UpdateAdSpaceRequest request
    ) {
        return adSpaceService.updateAdSpace(
                id,
                request.name(),
                null,  // type not updated from frontend
                null,  // city not updated from frontend
                null,  // address not updated from frontend
                null   // price not updated from frontend
        );
    }

    /**
     * DELETE /api/v1/ad-spaces/{id}
     *
     * Deletes an ad space.
     * - 204 No Content on success
     * - 404 Not Found if ad space doesn't exist
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdSpace(@PathVariable Long id) {
        adSpaceService.deleteAdSpace(id);
    }

    /**
     * PATCH /api/v1/ad-spaces/{id}/book
     *
     * Marks an ad space as booked.
     * - 200 OK with updated ad space
     * - 404 Not Found if ad space doesn't exist
     */
    @PatchMapping("/{id}/book")
    public AdSpace bookAdSpace(@PathVariable Long id) {
        AdSpace adSpace = adSpaceService.getAdSpaceById(id);
        adSpace.markBooked();
        return adSpaceService.updateAdSpace(id, null, null, null, null, null);
    }

    public record UpdateAdSpaceRequest(String name) {}
}
