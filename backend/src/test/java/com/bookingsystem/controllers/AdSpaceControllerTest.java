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

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        THEN all available spaces are returned succesfully
    """)
    void getAdSpaces_returnsList() {
        // GIVEN
        when(adSpaceService.searchAdSpaces(null, null))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceController.getAdSpaces(null, null);

        // THEN
        assertEquals(1, result.size());
        assertEquals("Sample Billboard", result.get(0).getName());
        assertEquals(AdSpaceType.BILLBOARD, result.get(0).getType());
        assertEquals("Bucharest", result.get(0).getCity());
        verify(adSpaceService).searchAdSpaces(null, null);
    }

    @Test
    @DisplayName("""
        GIVEN a request with city and type filters
        WHEN getAdSpaces is called
        THEN type is parsed case-insensitively and passed to the service together with the city
    """)
    void getAdSpaces_filtersByCityAndType() {
        // GIVEN
        when(adSpaceService.searchAdSpaces("Bucharest", AdSpaceType.BILLBOARD))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceController.getAdSpaces("billboard", "Bucharest");

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceService).searchAdSpaces(eq("Bucharest"), eq(AdSpaceType.BILLBOARD));
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
                () -> adSpaceController.getAdSpaces("invalid_type", null)
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
        when(adSpaceService.searchAdSpaces("Bucharest", null))
                .thenReturn(List.of(sampleAdSpace()));

        // WHEN
        List<AdSpace> result = adSpaceController.getAdSpaces("   ", "Bucharest");

        // THEN
        assertEquals(1, result.size());
        verify(adSpaceService).searchAdSpaces(eq("Bucharest"), eq(null));
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
}

