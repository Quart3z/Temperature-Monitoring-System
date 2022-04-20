package com.example.backend.spring.repository;

import com.example.backend.spring.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query(value = "SELECT * FROM Device WHERE Device.uuid = :uuid", nativeQuery = true)
    Device findByUuid(@Param("uuid") String uuid);

}
