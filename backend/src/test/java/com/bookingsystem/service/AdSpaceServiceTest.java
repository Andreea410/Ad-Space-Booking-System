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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;

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
        THEN it delegates to findByStatus(AVAILABLE) with default sort
    """)
    void searchAdSpaces_noFilters_usesStatusOnly() {
        // GIVEN
        when(adSpaceRepository.findByStatus(eq(AdSpaceStatus.AVAILABLE), any(Sort.class)))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceService.searchAdSpaces(null, null, null);

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceRepository).findByStatus(eq(AdSpaceStatus.AVAILABLE), any(Sort.class));
    }

    @Test
    @DisplayName("""
        GIVEN only city filter
        WHEN searchAdSpaces is called
        THEN it delegates to findByStatusAndCityContainingIgnoreCase with sort
    """)
    void searchAdSpaces_onlyCity_usesStatusAndCity() {
        // GIVEN
        when(adSpaceRepository.findByStatusAndCityContainingIgnoreCase(
                eq(AdSpaceStatus.AVAILABLE), eq("Bucharest"), any(Sort.class)))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceService.searchAdSpaces("Bucharest", null, null);

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceRepository).findByStatusAndCityContainingIgnoreCase(
                eq(AdSpaceStatus.AVAILABLE), eq("Bucharest"), any(Sort.class));
    }

    @Test
    @DisplayName("""
        GIVEN only type filter
        WHEN searchAdSpaces is called
        THEN it delegates to findByStatusAndType with sort
    """)
    void searchAdSpaces_onlyType_usesStatusAndType() {
        // GIVEN
        when(adSpaceRepository.findByStatusAndType(
                eq(AdSpaceStatus.AVAILABLE), eq(AdSpaceType.BILLBOARD), any(Sort.class)))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceService.searchAdSpaces(null, AdSpaceType.BILLBOARD, null);

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceRepository).findByStatusAndType(
                eq(AdSpaceStatus.AVAILABLE), eq(AdSpaceType.BILLBOARD), any(Sort.class));
    }

    @Test
    @DisplayName("""
        GIVEN both city and type filters
        WHEN searchAdSpaces is called
        THEN it delegates to findByStatusAndTypeAndCityContainingIgnoreCase with sort
    """)
    void searchAdSpaces_cityAndType_usesStatusTypeAndCity() {
        // GIVEN
        when(adSpaceRepository.findByStatusAndTypeAndCityContainingIgnoreCase(
                eq(AdSpaceStatus.AVAILABLE),
                eq(AdSpaceType.BILLBOARD),
                eq("Bucharest"),
                any(Sort.class)
        )).thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceService.searchAdSpaces("Bucharest", AdSpaceType.BILLBOARD, null);

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceRepository).findByStatusAndTypeAndCityContainingIgnoreCase(
                eq(AdSpaceStatus.AVAILABLE),
                eq(AdSpaceType.BILLBOARD),
                eq("Bucharest"),
                any(Sort.class)
        );
    }

    @Test
    @DisplayName("""
        GIVEN an existing ad space and a new name
        WHEN updateAdSpace is called with only name
        THEN the name is updated and the entity is saved
    """)
    void updateAdSpace_withName_updatesSuccessfully() {
        // GIVEN
        AdSpace existingAdSpace = sampleAdSpace();
        String newName = "Updated Billboard Name";
        
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(existingAdSpace));
        when(adSpaceRepository.save(any(AdSpace.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        AdSpace result = adSpaceService.updateAdSpace(1L, newName, null, null, null, null);

        // THEN
        assertEquals(newName, result.getName());
        assertEquals("Bucharest", result.getCity()); // Other fields unchanged
        verify(adSpaceRepository).findById(1L);
        verify(adSpaceRepository).save(existingAdSpace);
    }

    @Test
    @DisplayName("""
        GIVEN an existing ad space and multiple update fields
        WHEN updateAdSpace is called
        THEN all provided fields are updated
    """)
    void updateAdSpace_withMultipleFields_updatesAll() {
        // GIVEN
        AdSpace existingAdSpace = sampleAdSpace();
        String newName = "New Name";
        String newCity = "Cluj";
        String newAddress = "New Address";
        BigDecimal newPrice = new BigDecimal("250.00");
        AdSpaceType newType = AdSpaceType.BUS_STOP;
        
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(existingAdSpace));
        when(adSpaceRepository.save(any(AdSpace.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        AdSpace result = adSpaceService.updateAdSpace(1L, newName, newType, newCity, newAddress, newPrice);

        // THEN
        assertEquals(newName, result.getName());
        assertEquals(newCity, result.getCity());
        assertEquals(newAddress, result.getAddress());
        assertEquals(newPrice, result.getPricePerDay());
        assertEquals(newType, result.getType());
        verify(adSpaceRepository).save(existingAdSpace);
    }

    @Test
    @DisplayName("""
        GIVEN a non-existent ad space id
        WHEN updateAdSpace is called
        THEN AdSpaceNotFoundException is thrown
    """)
    void updateAdSpace_notFound_throwsException() {
        // GIVEN
        when(adSpaceRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(AdSpaceNotFoundException.class, 
                () -> adSpaceService.updateAdSpace(99L, "New Name", null, null, null, null));
        verify(adSpaceRepository, never()).save(any());
    }

    @Test
    @DisplayName("""
        GIVEN an existing ad space with no dependencies
        WHEN deleteAdSpace is called
        THEN the repository deletes the entity
    """)
    void deleteAdSpace_noDependencies_deletesSuccessfully() {
        // GIVEN
        AdSpace adSpace = sampleAdSpace();
        
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(adSpace));
        doNothing().when(adSpaceRepository).delete(adSpace);

        // WHEN
        adSpaceService.deleteAdSpace(1L);

        // THEN
        verify(adSpaceRepository).findById(1L);
        verify(adSpaceRepository).delete(adSpace);
    }

    @Test
    @DisplayName("""
        GIVEN a non-existent ad space id
        WHEN deleteAdSpace is called
        THEN AdSpaceNotFoundException is thrown
    """)
    void deleteAdSpace_notFound_throwsException() {
        // GIVEN
        when(adSpaceRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(AdSpaceNotFoundException.class, 
                () -> adSpaceService.deleteAdSpace(99L));
        verify(adSpaceRepository, never()).delete(any());
    }

    @Test
    @DisplayName("""
        GIVEN an ad space with booking dependencies
        WHEN deleteAdSpace is called
        THEN DataIntegrityViolationException is propagated from repository
    """)
    void deleteAdSpace_withDependencies_throwsDataIntegrityViolation() {
        // GIVEN
        AdSpace adSpace = sampleAdSpace();
        
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(adSpace));
        doThrow(new DataIntegrityViolationException("Foreign key constraint"))
                .when(adSpaceRepository).delete(adSpace);

        // WHEN / THEN
        assertThrows(DataIntegrityViolationException.class, 
                () -> adSpaceService.deleteAdSpace(1L));
    }
}
