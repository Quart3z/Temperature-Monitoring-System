package com.example.backend.spring.repository;

import com.example.backend.spring.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface EntryRepository extends JpaRepository<Entry, Long> {

//    @Query(value = "SELECT * FROM Entry WHERE Entry.uuid = :uuid AND Entry.timestamp = :timestamp", nativeQuery = true)
//    Entry findByUuidAndTimestamp(@Param("uuid") String uuid, @Param("timestamp") Long timestamp);

    @Query(value = "SELECT * FROM Entry ORDER BY Entry.timestamp DESC LIMIT :count ", nativeQuery = true)
    Collection<Entry> findEntries(@Param("count") int count);

}
