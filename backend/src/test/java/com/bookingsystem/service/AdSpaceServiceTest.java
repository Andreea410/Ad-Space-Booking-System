package com.bookingsystem.service;

import com.bookingsystem.exception.AdSpaceNotFoundException;
import com.bookingsystem.model.AdSpace;
import com.bookingsystem.model.AdSpaceStatus;
import com.bookingsystem.model.AdSpaceType;
import com.bookingsystem.repository.AdSpaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdSpaceServiceTest {

    @Mock
    private AdSpaceRepository adSpaceRepository;

    @InjectMocks
    private AdSpaceService adSpaceService;

    private AdSpace sampleAdSpace() {
        return new AdSpace(
                "Sample Billboard",
                AdSpaceType.BILLBOARD,
                "Bucharest",
                "Piata Unirii",
                new BigDecimal("150.00"),
                AdSpaceStatus.AVAILABLE
        );
    }

    @Test
    @DisplayName("""
        GIVEN valid ad space details
        WHEN createAdSpace is called
        THEN a new AVAILABLE ad space is created and persisted via the repository
    """)
    void createAdSpace_createsAndSavesEntity() {
        // GIVEN
        AdSpace persisted = sampleAdSpace();
        when(adSpaceRepository.save(any(AdSpace.class))).thenReturn(persisted);

        // WHEN
        AdSpace result = adSpaceService.createAdSpace(
                "Sample Billboard",
                AdSpaceType.BILLBOARD,
                "Bucharest",
                "Piata Unirii",
                new BigDecimal("150.00")
        );

        // THEN
        assertNotNull(result);
        assertEquals("Sample Billboard", result.getName());
        assertEquals(AdSpaceStatus.AVAILABLE, result.getStatus());
        verify(adSpaceRepository).save(any(AdSpace.class));
    }

    @Test
    @DisplayName("""
        GIVEN an existing ad space id
        WHEN getAdSpaceById is called
        THEN the ad space is returned from the repository
    """)
    void getAdSpaceById_returnsEntity() {
        // GIVEN
        AdSpace adSpace = sampleAdSpace();
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(adSpace));

        // WHEN
        AdSpace result = adSpaceService.getAdSpaceById(1L);

        // THEN
        assertSame(adSpace, result);
        verify(adSpaceRepository).findById(1L);
    }

    @Test
    @DisplayName("""
        GIVEN a non-existent ad space id
        WHEN getAdSpaceById is called
        THEN AdSpaceNotFoundException is thrown
    """)
    void getAdSpaceById_throwsWhenNotFound() {
        // GIVEN
        when(adSpaceRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(AdSpaceNotFoundException.class, () -> adSpaceService.getAdSpaceById(99L));
    }

    @Test
    @DisplayName("""
        GIVEN no filters
        WHEN searchAdSpaces is called
        THEN it delegates to findByStatus(AVAILABLE)
    """)
    void searchAdSpaces_noFilters_usesStatusOnly() {
        // GIVEN
        when(adSpaceRepository.findByStatus(AdSpaceStatus.AVAILABLE))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceService.searchAdSpaces(null, null);

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceRepository).findByStatus(AdSpaceStatus.AVAILABLE);
    }

    @Test
    @DisplayName("""
        GIVEN only city filter
        WHEN searchAdSpaces is called
        THEN it delegates to findByStatusAndCity(AVAILABLE, city)
    """)
    void searchAdSpaces_onlyCity_usesStatusAndCity() {
        // GIVEN
        when(adSpaceRepository.findByStatusAndCity(AdSpaceStatus.AVAILABLE, "Bucharest"))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceService.searchAdSpaces("Bucharest", null);

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceRepository).findByStatusAndCity(AdSpaceStatus.AVAILABLE, "Bucharest");
    }

    @Test
    @DisplayName("""
        GIVEN only type filter
        WHEN searchAdSpaces is called
        THEN it delegates to findByStatusAndType(AVAILABLE, type)
    """)
    void searchAdSpaces_onlyType_usesStatusAndType() {
        // GIVEN
        when(adSpaceRepository.findByStatusAndType(AdSpaceStatus.AVAILABLE, AdSpaceType.BILLBOARD))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceService.searchAdSpaces(null, AdSpaceType.BILLBOARD);

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceRepository).findByStatusAndType(AdSpaceStatus.AVAILABLE, AdSpaceType.BILLBOARD);
    }

    @Test
    @DisplayName("""
        GIVEN both city and type filters
        WHEN searchAdSpaces is called
        THEN it delegates to findByStatusAndTypeAndCity(AVAILABLE, type, city)
    """)
    void searchAdSpaces_cityAndType_usesStatusTypeAndCity() {
        // GIVEN
        when(adSpaceRepository.findByStatusAndTypeAndCity(
                AdSpaceStatus.AVAILABLE,
                AdSpaceType.BILLBOARD,
                "Bucharest"
        )).thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceService.searchAdSpaces("Bucharest", AdSpaceType.BILLBOARD);

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceRepository).findByStatusAndTypeAndCity(
                AdSpaceStatus.AVAILABLE,
                AdSpaceType.BILLBOARD,
                "Bucharest"
        );
    }
}
