package com.bookingsystem.repository;

import com.bookingsystem.model.AdSpace;
import com.bookingsystem.model.AdSpaceStatus;
import com.bookingsystem.model.AdSpaceType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdSpaceRepository extends JpaRepository<AdSpace, Long> {

    List<AdSpace> findByStatus(AdSpaceStatus status, Sort sort);

    List<AdSpace> findByType(AdSpaceType type);

    List<AdSpace> findByCity(String city);

    List<AdSpace> findByStatusAndCity(AdSpaceStatus status, String city);

    List<AdSpace> findByStatusAndCityContainingIgnoreCase(AdSpaceStatus status, String cityFragment, Sort sort);

    List<AdSpace> findByStatusAndType(AdSpaceStatus status, AdSpaceType type, Sort sort);

    List<AdSpace> findByStatusAndTypeAndCity(
            AdSpaceStatus status,
            AdSpaceType type,
            String city
    );

    List<AdSpace> findByStatusAndTypeAndCityContainingIgnoreCase(
            AdSpaceStatus status,
            AdSpaceType type,
            String cityFragment,
            Sort sort
    );

    default List<AdSpace> findAllAvailable() {
        return findByStatus(AdSpaceStatus.AVAILABLE, Sort.by("name").ascending());
    }

    default List<AdSpace> findAllBooked() {
        return findByStatus(AdSpaceStatus.BOOKED, Sort.by("name").ascending());
    }
}
