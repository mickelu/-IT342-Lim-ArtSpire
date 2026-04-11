package edu.cit.lim.artspire.repository;

import edu.cit.lim.artspire.model.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findAllByOrderByCreatedAtDesc();
}