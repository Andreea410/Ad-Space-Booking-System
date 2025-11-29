package com.bookingsystem.repository;

import com.bookingsystem.model.AdSpace;
import com.bookingsystem.model.AdSpaceStatus;
import com.bookingsystem.model.AdSpaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdSpaceRepository extends JpaRepository<AdSpace, Long> {

    List<AdSpace> findByStatus(AdSpaceStatus status);

    List<AdSpace> findByType(AdSpaceType type);

    List<AdSpace> findByCity(String city);

    List<AdSpace> findByStatusAndCity(AdSpaceStatus status, String city);

    List<AdSpace> findByStatusAndType(AdSpaceStatus status, AdSpaceType type);

    List<AdSpace> findByStatusAndTypeAndCity(
            AdSpaceStatus status,
            AdSpaceType type,
            String city
    );

    default List<AdSpace> findAllAvailable() {
        return findByStatus(AdSpaceStatus.AVAILABLE);
    }

    default List<AdSpace> findAllBooked() {
        return findByStatus(AdSpaceStatus.BOOKED);
    }
}
