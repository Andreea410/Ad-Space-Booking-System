package com.bookingsystem.controllers;

import com.bookingsystem.exception.AdSpaceNotFoundException;
import com.bookingsystem.model.AdSpace;
import com.bookingsystem.model.AdSpaceStatus;
import com.bookingsystem.model.AdSpaceType;
import com.bookingsystem.service.AdSpaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static com.bookingsystem.controllers.AdSpaceController.UpdateAdSpaceRequest;

@ExtendWith(MockitoExtension.class)
class AdSpaceControllerTest {

    @Mock
    private AdSpaceService adSpaceService;

    @InjectMocks
    private AdSpaceController adSpaceController;

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
        GIVEN a valid request with no search filtering
        WHEN getAdSpaces is called
        THEN all available spaces are returned with default sort
    """)
    void getAdSpaces_returnsList() {
        // GIVEN
        when(adSpaceService.searchAdSpaces(eq(null), eq(null), any(Sort.class)))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceController.getAdSpaces(null, null, null, null);

        // THEN
        assertEquals(1, result.size());
        assertEquals("Sample Billboard", result.get(0).getName());
        assertEquals(AdSpaceType.BILLBOARD, result.get(0).getType());
        assertEquals("Bucharest", result.get(0).getCity());
        verify(adSpaceService).searchAdSpaces(eq(null), eq(null), any(Sort.class));
    }

    @Test
    @DisplayName("""
        GIVEN a request with city and type filters
        WHEN getAdSpaces is called
        THEN type is parsed case-insensitively and passed to the service with sort
    """)
    void getAdSpaces_filtersByCityAndType() {
        // GIVEN
        when(adSpaceService.searchAdSpaces(eq("Bucharest"), eq(AdSpaceType.BILLBOARD), any(Sort.class)))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceController.getAdSpaces("billboard", "Bucharest", null, null);

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceService).searchAdSpaces(eq("Bucharest"), eq(AdSpaceType.BILLBOARD), any(Sort.class));
    }

    @Test
    @DisplayName("""
        GIVEN an invalid type query parameter
        WHEN getAdSpaces is called
        THEN an IllegalArgumentException is thrown with a helpful message
    """)
    void getAdSpaces_invalidType_throwsIllegalArgumentException() {
        // GIVEN

        // WHEN / THEN
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> adSpaceController.getAdSpaces("invalid_type", null, null, null)
        );

        assertTrue(ex.getMessage().contains("Invalid 'type' parameter"));
    }

    @Test
    @DisplayName("""
        GIVEN a blank type query parameter and a valid city
        WHEN getAdSpaces is called
        THEN the type filter is ignored and only the city filter is applied
    """)
    void getAdSpaces_blankType_ignored() {
        // GIVEN
        when(adSpaceService.searchAdSpaces(eq("Bucharest"), eq(null), any(Sort.class)))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceController.getAdSpaces("   ", "Bucharest", null, null);

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceService).searchAdSpaces(eq("Bucharest"), eq(null), any(Sort.class));
    }

    @Test
    @DisplayName("""
        GIVEN an existing ad space id
        WHEN getAdSpaceById is called
        THEN the service is invoked and the ad space is returned
    """)
    void getAdSpaceById_returnsEntity() {
        // GIVEN
        AdSpace adSpace = sampleAdSpace();
        when(adSpaceService.getAdSpaceById(1L)).thenReturn(adSpace);

        // WHEN
        AdSpace result = adSpaceController.getAdSpaceById(1L);

        // THEN
        assertSame(adSpace, result);
        verify(adSpaceService).getAdSpaceById(1L);
    }

    @Test
    @DisplayName("""
        GIVEN a non-existent ad space id
        WHEN getAdSpaceById is called
        THEN AdSpaceNotFoundException is propagated
    """)
    void getAdSpaceById_throwsWhenNotFound() {
        // GIVEN
        when(adSpaceService.getAdSpaceById(99L)).thenThrow(new AdSpaceNotFoundException(99L));

        // WHEN / THEN
        assertThrows(AdSpaceNotFoundException.class, () -> adSpaceController.getAdSpaceById(99L));
    }

    @Test
    @DisplayName("""
        GIVEN a valid ad space id and update request with name
        WHEN updateAdSpace is called
        THEN the service updates the ad space and returns the updated entity
    """)
    void updateAdSpace_withName_updatesSuccessfully() {
        // GIVEN
        Long adSpaceId = 1L;
        String newName = "Updated Billboard Name";
        AdSpace updatedAdSpace = sampleAdSpace();
        updatedAdSpace.setName(newName);
        
        UpdateAdSpaceRequest request = new UpdateAdSpaceRequest(newName);
        
        when(adSpaceService.updateAdSpace(eq(adSpaceId), eq(newName), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(updatedAdSpace);

        // WHEN
        AdSpace result = adSpaceController.updateAdSpace(adSpaceId, request);

        // THEN
        assertEquals(newName, result.getName());
        verify(adSpaceService).updateAdSpace(eq(adSpaceId), eq(newName), isNull(), isNull(), isNull(), isNull());
    }

    @Test
    @DisplayName("""
        GIVEN a non-existent ad space id
        WHEN updateAdSpace is called
        THEN AdSpaceNotFoundException is propagated
    """)
    void updateAdSpace_notFound_throwsException() {
        // GIVEN
        Long adSpaceId = 99L;
        UpdateAdSpaceRequest request = new UpdateAdSpaceRequest("New Name");
        
        when(adSpaceService.updateAdSpace(eq(adSpaceId), anyString(), any(), any(), any(), any()))
                .thenThrow(new AdSpaceNotFoundException(adSpaceId));

        // WHEN / THEN
        assertThrows(AdSpaceNotFoundException.class, 
                () -> adSpaceController.updateAdSpace(adSpaceId, request));
    }

    @Test
    @DisplayName("""
        GIVEN a valid available ad space id
        WHEN bookAdSpace is called
        THEN the service retrieves it, marks as booked, and updates
    """)
    void bookAdSpace_available_marksAsBooked() {
        // GIVEN
        Long adSpaceId = 1L;
        AdSpace adSpace = sampleAdSpace();
        AdSpace updatedAdSpace = sampleAdSpace();
        updatedAdSpace.markBooked();
        
        when(adSpaceService.getAdSpaceById(adSpaceId)).thenReturn(adSpace);
        when(adSpaceService.updateAdSpace(eq(adSpaceId), isNull(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(updatedAdSpace);

        // WHEN
        AdSpace result = adSpaceController.bookAdSpace(adSpaceId);

        // THEN
        assertEquals(AdSpaceStatus.BOOKED, result.getStatus());
        verify(adSpaceService).getAdSpaceById(adSpaceId);
        verify(adSpaceService).updateAdSpace(eq(adSpaceId), isNull(), isNull(), isNull(), isNull(), isNull());
    }

    @Test
    @DisplayName("""
        GIVEN a non-existent ad space id
        WHEN bookAdSpace is called
        THEN AdSpaceNotFoundException is propagated
    """)
    void bookAdSpace_notFound_throwsException() {
        // GIVEN
        Long adSpaceId = 99L;
        when(adSpaceService.getAdSpaceById(adSpaceId))
                .thenThrow(new AdSpaceNotFoundException(adSpaceId));

        // WHEN / THEN
        assertThrows(AdSpaceNotFoundException.class, 
                () -> adSpaceController.bookAdSpace(adSpaceId));
    }

    @Test
    @DisplayName("""
        GIVEN a valid ad space id with no dependencies
        WHEN deleteAdSpace is called
        THEN the service deletes it successfully
    """)
    void deleteAdSpace_noDependencies_deletesSuccessfully() {
        // GIVEN
        Long adSpaceId = 1L;
        doNothing().when(adSpaceService).deleteAdSpace(adSpaceId);

        // WHEN
        adSpaceController.deleteAdSpace(adSpaceId);

        // THEN
        verify(adSpaceService).deleteAdSpace(adSpaceId);
    }

    @Test
    @DisplayName("""
        GIVEN a non-existent ad space id
        WHEN deleteAdSpace is called
        THEN AdSpaceNotFoundException is propagated
    """)
    void deleteAdSpace_notFound_throwsException() {
        // GIVEN
        Long adSpaceId = 99L;
        doThrow(new AdSpaceNotFoundException(adSpaceId))
                .when(adSpaceService).deleteAdSpace(adSpaceId);

        // WHEN / THEN
        assertThrows(AdSpaceNotFoundException.class, 
                () -> adSpaceController.deleteAdSpace(adSpaceId));
    }

    @Test
    @DisplayName("""
        GIVEN an ad space id with booking dependencies
        WHEN deleteAdSpace is called
        THEN DataIntegrityViolationException is propagated
    """)
    void deleteAdSpace_withDependencies_throwsDataIntegrityViolation() {
        // GIVEN
        Long adSpaceId = 1L;
        doThrow(new DataIntegrityViolationException("Foreign key constraint violation"))
                .when(adSpaceService).deleteAdSpace(adSpaceId);

        // WHEN / THEN
        assertThrows(DataIntegrityViolationException.class, 
                () -> adSpaceController.deleteAdSpace(adSpaceId));
    }
}

