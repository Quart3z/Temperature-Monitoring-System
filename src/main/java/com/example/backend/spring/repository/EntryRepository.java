package com.example.backend.spring.repository;

import com.example.backend.spring.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    @Query(
            value = "SELECT * FROM Entry " +
                    "WHERE Entry.temperature >= :min AND Entry.temperature <= :max " +
                    "AND (:start is null OR Entry.timestamp >= :start) AND (:end is null OR Entry.timestamp <= :end) " +
                    "ORDER BY Entry.timestamp DESC LIMIT :count ",
            nativeQuery = true)
    Collection<Entry> findEntries(@Param("count") int count, @Param("start") long start, @Param("end") long end, @Param("min") float min, @Param("max") float max);



}
