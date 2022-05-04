package ch.fuchsgroup.migeltoken.models;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduktRepository extends JpaRepository<Produkt, Integer>{
	Optional<List<Produkt>> findProduktByMigelnummer(String nummer);
	List<Produkt> findByOrderByMigelnummerAsc();
	/*@Query(value = "from produkt p where p.gültig <= :date AND   AND p.migelnummer == :nummer ODER BY p.gültig")
	public List<Produkt> getAllBetweenDates(@Param("date")LocalDateTime date,@Param("nummer")String nummer);*/
	List<Produkt> findByMigelnummerAndGueltigLessThanOrderByGueltigDesc(String nummer, LocalDateTime date);
}