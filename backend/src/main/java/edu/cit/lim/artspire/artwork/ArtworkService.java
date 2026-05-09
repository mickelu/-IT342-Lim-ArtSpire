package edu.cit.lim.artspire.artwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArtworkService {

    @Autowired
    private ArtworkRepository artworkRepository;

    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAllByOrderByCreatedAtDesc();
    }

    public Artwork createArtwork(Artwork artwork) {
        return artworkRepository.save(artwork);
    }

    public Artwork getArtworkById(Long id) {
        return artworkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artwork not found with id: " + id));
    }

    public Artwork updateArtwork(Long id, Artwork artwork) {
        Artwork existingArtwork = getArtworkById(id);
        existingArtwork.setTitle(artwork.getTitle());
        existingArtwork.setDescription(artwork.getDescription());
        existingArtwork.setArtistName(artwork.getArtistName());
        existingArtwork.setImageUrl(artwork.getImageUrl());
        return artworkRepository.save(existingArtwork);
    }

    public void deleteArtwork(Long id) {
        artworkRepository.deleteById(id);
    }
}
