package ch.fuchsgroup.migeltoken.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProduktService {
	private ProduktRepository produktRepository;
	
	@Autowired
	public ProduktService(ProduktRepository produktRepository) {
		this.produktRepository = produktRepository;
	}

	public void addProdukt(Produkt produkt) {
		Optional<List<Produkt>> p = produktRepository.findProduktByMigelnummer(produkt.getMigelnummer());
		if(p.isPresent() && p.get().stream().filter(prod -> prod.getGueltig().equals(produkt.getGueltig())).findFirst().isPresent()) {
			
		}else {
			produktRepository.save(produkt);
		}
	}
	
	public List<Produkt> alleProdukte() {
		return produktRepository.findByOrderByMigelnummerAsc();
	}
	
	public List<Produkt> getProdukte(String migelnummer) {
		return produktRepository.findProduktByMigelnummer(migelnummer).get();
	}
	public List<Produkt> getProdukt(String migelnummer, LocalDateTime date) {
		return produktRepository.findByMigelnummerAndGueltigLessThanOrderByGueltigDesc(migelnummer, date);
	}
}
