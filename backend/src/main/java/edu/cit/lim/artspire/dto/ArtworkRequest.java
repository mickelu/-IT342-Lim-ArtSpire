package edu.cit.lim.artspire.dto;

import lombok.Data;

@Data
public class ArtworkRequest {
    private String title;
    private String description;
    private String imageUrl;
}