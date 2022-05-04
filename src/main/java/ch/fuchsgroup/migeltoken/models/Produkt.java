package ch.fuchsgroup.migeltoken.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

@Entity
@Table
public class Produkt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String migelnummer;
	@Column(columnDefinition="TEXT")
	private String bezeichnung;
    @Column(columnDefinition="TEXT")
	private String limitation;
	private String menge;
	private BigDecimal hvbSelbstanwendung;
	private BigDecimal hvbPflege;
	@Column(name="gültig", nullable = false)
	private LocalDateTime gueltig;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMigelnummer() {
		return migelnummer;
	}
	public void setMigelnummer(String migelnummer) {
		this.migelnummer = migelnummer;
	}
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public String getLimitation() {
		return limitation;
	}
	public void setLimitation(String limitation) {
		this.limitation = limitation;
	}
	public String getMenge() {
		return menge;
	}
	public void setMenge(String menge) {
		this.menge = menge;
	}
	public BigDecimal getHvbSelbstanwendung() {
		return hvbSelbstanwendung;
	}
	public void setHvbSelbstanwendung(BigDecimal hvbSelbstanwendung) {
		this.hvbSelbstanwendung = hvbSelbstanwendung;
	}
	public BigDecimal getHvbPflege() {
		return hvbPflege;
	}
	public void setHvbPflege(BigDecimal hvbPflege) {
		this.hvbPflege = hvbPflege;
	}
	public LocalDateTime getGueltig() {
		return gueltig;
	}
	public void setGueltig(LocalDateTime gueltig) {
		this.gueltig = gueltig;
	}
}
