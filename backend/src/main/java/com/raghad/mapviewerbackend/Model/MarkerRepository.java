package com.raghad.mapviewerbackend.Model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarkerRepository extends JpaRepository<Marker, Long> {
    List<Marker> findByUserId(Long userId);

}