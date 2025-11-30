package com.bookingsystem.service;

import com.bookingsystem.exception.AdSpaceNotFoundException;
import com.bookingsystem.model.AdSpace;
import com.bookingsystem.model.AdSpaceStatus;
import com.bookingsystem.model.AdSpaceType;
import com.bookingsystem.repository.AdSpaceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class AdSpaceService {

    private final AdSpaceRepository adSpaceRepository;

    public AdSpaceService(AdSpaceRepository adSpaceRepository) {
        this.adSpaceRepository = adSpaceRepository;
    }

    public AdSpace createAdSpace(
            String name,
            AdSpaceType type,
            String city,
            String address,
            BigDecimal pricePerDay
    ) {
        AdSpace adSpace = new AdSpace(
                name,
                type,
                city,
                address,
                pricePerDay,
                AdSpaceStatus.AVAILABLE
        );

        return adSpaceRepository.save(adSpace);
    }

    @Transactional(readOnly = true)
    public AdSpace getAdSpaceById(Long id) {
        return adSpaceRepository.findById(id)
                .orElseThrow(() -> new AdSpaceNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<AdSpace> getAllAvailableAdSpaces() {
        return adSpaceRepository.findByStatus(AdSpaceStatus.AVAILABLE, Sort.by("name").ascending());
    }

    @Transactional(readOnly = true)
    public List<AdSpace> searchAdSpaces(String city, AdSpaceType type, Sort sort) {
        if (sort == null) {
            sort = Sort.by("name").ascending();
        }

        if (city != null && type != null) {
            return adSpaceRepository.findByStatusAndTypeAndCityContainingIgnoreCase(
                    AdSpaceStatus.AVAILABLE,
                    type,
                    city,
                    sort
            );
        }

        if (city != null) {
            return adSpaceRepository.findByStatusAndCityContainingIgnoreCase(
                    AdSpaceStatus.AVAILABLE,
                    city,
                    sort
            );
        }

        if (type != null) {
            return adSpaceRepository.findByStatusAndType(
                    AdSpaceStatus.AVAILABLE,
                    type,
                    sort
            );
        }

        return adSpaceRepository.findByStatus(AdSpaceStatus.AVAILABLE, sort);
    }

    public AdSpace markForMaintenance(Long id) {
        AdSpace adSpace = getAdSpaceById(id);
        adSpace.markMaintenance();
        return adSpaceRepository.save(adSpace);
    }

    public AdSpace markAsAvailable(Long id) {
        AdSpace adSpace = getAdSpaceById(id);
        adSpace.markAvailable();
        return adSpaceRepository.save(adSpace);
    }

    public AdSpace updateAdSpace(
            Long id,
            String name,
            AdSpaceType type,
            String city,
            String address,
            BigDecimal pricePerDay
    ) {
        AdSpace adSpace = getAdSpaceById(id);

        if (name != null) adSpace.setName(name);
        if (type != null) adSpace.changeType(type);
        if (city != null) adSpace.setCity(city);
        if (address != null) adSpace.setAddress(address);
        if (pricePerDay != null) adSpace.setPricePerDay(pricePerDay);

        return adSpaceRepository.save(adSpace);
    }

    public void deleteAdSpace(Long id) {
        AdSpace adSpace = getAdSpaceById(id);
        adSpaceRepository.delete(adSpace);
    }
}
