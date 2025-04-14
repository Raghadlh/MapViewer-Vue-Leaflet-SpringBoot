package com.raghad.mapviewerbackend.Controller;

import com.raghad.mapviewerbackend.Model.Marker;
import com.raghad.mapviewerbackend.Model.MarkerRepository;
import com.raghad.mapviewerbackend.Model.MyAppUser;
import com.raghad.mapviewerbackend.Model.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/markers")
public class MarkerController {

    @Autowired
    private MarkerRepository markerRepository;

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    // Get markers by user
    @GetMapping("/user/{userId}")
    public List<Marker> getMarkersByUser(@PathVariable Long userId) {
        return markerRepository.findByUserId(userId);
    }

    // Create new marker for a specific user
    @PostMapping("/user/{userId}")
    public ResponseEntity<Marker> createMarkerForUser(@PathVariable Long userId, @RequestBody Marker marker) {
        return myAppUserRepository.findById(userId).map(user -> {
            marker.setUser(user);
            Marker savedMarker = markerRepository.save(marker);
            return ResponseEntity.ok(savedMarker);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marker> getMarkerById(@PathVariable Long id) {
        return markerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marker> updateMarker(@PathVariable Long id, @RequestBody Marker markerDetails) {
        return markerRepository.findById(id)
                .map(marker -> {
                    marker.setTitle(markerDetails.getTitle());
                    marker.setDescription(markerDetails.getDescription());
                    marker.setLatitude(markerDetails.getLatitude());
                    marker.setLongitude(markerDetails.getLongitude());
                    Marker updatedMarker = markerRepository.save(marker);
                    return ResponseEntity.ok(updatedMarker);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMarker(@PathVariable Long id) {
        return markerRepository.findById(id)
                .map(marker -> {
                    markerRepository.delete(marker);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
