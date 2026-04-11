package edu.cit.lim.artspire.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArtworkResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Long userId;
    private LocalDateTime createdAt;
}