package com.rounak.impression.repository;


import com.rounak.impression.model.GameImpression; // Import your GameImpression model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Marks this interface as a Spring Data JPA repository component
public interface GameImpressionRepository extends JpaRepository<GameImpression, Long> {

}