package edu.cit.lim.artspire.controller;

import edu.cit.lim.artspire.model.Artwork;
import edu.cit.lim.artspire.service.ArtworkService;
import edu.cit.lim.artspire.service.SupabaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/artworks")
@CrossOrigin(origins = "http://localhost:5173")
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;

    @Autowired
    private SupabaseStorageService storageService;

    @GetMapping
    public ResponseEntity<List<Artwork>> getAllArtworks() {
        return ResponseEntity.ok(artworkService.getAllArtworks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artwork> getArtworkById(@PathVariable Long id) {
        return ResponseEntity.ok(artworkService.getArtworkById(id));
    }

    @PostMapping
    public ResponseEntity<?> createArtwork(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image) {

        try {
            // Validate inputs
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Title is required"));
            }

            if (image == null || image.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Image file is required"));
            }

            // Validate file type
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only image files are allowed (JPG, PNG, GIF)"));
            }

            // Upload image to Supabase Storage
            String imageUrl = storageService.uploadImage(image);

            // Create artwork record
            Artwork artwork = new Artwork();
            artwork.setTitle(title);
            artwork.setDescription(description);
            artwork.setImageUrl(imageUrl);

            Artwork savedArtwork = artworkService.createArtwork(artwork);
            return new ResponseEntity<>(savedArtwork, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload artwork: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtwork(@PathVariable Long id) {
        try {
            Artwork artwork = artworkService.getArtworkById(id);

            // Delete image from Supabase Storage
            try {
                storageService.deleteImage(artwork.getImageUrl());
            } catch (Exception e) {
                System.err.println("Warning: Failed to delete image from storage: " + e.getMessage());
            }

            // Delete from database
            artworkService.deleteArtwork(id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete artwork: " + e.getMessage()));
        }
    }
}