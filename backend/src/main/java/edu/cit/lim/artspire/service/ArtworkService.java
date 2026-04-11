package edu.cit.lim.artspire.service;

import edu.cit.lim.artspire.model.Artwork;
import edu.cit.lim.artspire.repository.ArtworkRepository;
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

    public void deleteArtwork(Long id) {
        artworkRepository.deleteById(id);
    }
}