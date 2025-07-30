package com.rounak.impression.controller;


import com.rounak.impression.model.GameImpression;
import com.rounak.impression.service.GameImpressionService; // <--- REFERENCING THE RENAMED SERVICE CLASS
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/impressions")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class GameImpressionController {

    // Removed @Autowired, made fields final, and added @RequiredArgsConstructor
    // (This class will also benefit from @RequiredArgsConstructor if you add final fields for injection)

    private final GameImpressionService gameImpressionService; // <--- RENAMED REFERENCE
    private final ObjectMapper objectMapper; // Assuming you'll make this final and inject via constructor too

    // Using constructor injection with @Autowired (or @RequiredArgsConstructor on the class)
    // for controllers is the recommended practice.
    @Autowired
    public GameImpressionController(GameImpressionService gameImpressionService, ObjectMapper objectMapper) {
        this.gameImpressionService = gameImpressionService;
        this.objectMapper = objectMapper;
    }


    // --- READ OPERATIONS (Accessible to anyone) ---

    @GetMapping
    public List<GameImpression> getAllGameImpressions() {
        return gameImpressionService.getAllGameImpressions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameImpression> getGameImpressionById(@PathVariable Long id) {
        return gameImpressionService.getGameImpressionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- WRITE OPERATIONS (Protected by Interceptor) ---

    @PostMapping
    public ResponseEntity<GameImpression> createGameImpression(
            @RequestPart("impression") String impressionJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        try {
            GameImpression gameImpression = objectMapper.readValue(impressionJson, GameImpression.class);
            GameImpression savedImpression = gameImpressionService.saveGameImpression(gameImpression, imageFile);
            return new ResponseEntity<>(savedImpression, HttpStatus.CREATED);
        } catch (IOException e) {
            System.err.println("Error processing image or JSON for creation: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println("Error creating impression: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameImpression> updateGameImpression(
            @PathVariable Long id,
            @RequestPart("impression") String impressionJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        try {
            GameImpression gameImpressionDetails = objectMapper.readValue(impressionJson, GameImpression.class);
            GameImpression updatedImpression = gameImpressionService.updateGameImpression(id, gameImpressionDetails, imageFile);
            return ResponseEntity.ok(updatedImpression);
        } catch (IOException e) {
            System.err.println("Error processing image or JSON for update: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            System.err.println("Error updating impression (not found): " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error updating impression: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameImpression(@PathVariable Long id) {
        gameImpressionService.deleteGameImpression(id);
        return ResponseEntity.noContent().build();
    }
}