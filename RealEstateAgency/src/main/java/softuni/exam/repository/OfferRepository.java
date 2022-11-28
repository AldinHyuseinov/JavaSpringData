package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.dto.ExportBestOfferDTO;
import softuni.exam.models.entity.Offer;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query("SELECT CONCAT(a.firstName, ' ', a.lastName) AS agentFullName, o.id AS offerId, ap.area AS apartmentArea, " +
            "t.townName AS townName, o.price AS price FROM Offer AS o JOIN o.agent AS a JOIN o.apartment AS ap JOIN ap.town AS t " +
            "WHERE ap.apartmentType = 'three_rooms' ORDER BY ap.area DESC, o.price")
    List<ExportBestOfferDTO> findBestOffers();
}
