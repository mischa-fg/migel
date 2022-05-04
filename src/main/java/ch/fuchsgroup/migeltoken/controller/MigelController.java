package ch.fuchsgroup.migeltoken.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import ch.fuchsgroup.migeltoken.excel.ExcelImport;
import ch.fuchsgroup.migeltoken.models.Produkt;
import ch.fuchsgroup.migeltoken.models.ProduktService;
import ch.fuchsgroup.migeltoken.xml.ReadXml;






@RestController
@RequestMapping(path = "migel")
public class MigelController {
	private ExcelImport excelImport;
	private ProduktService produktService;
	
	@Autowired
	public MigelController(ExcelImport excelImport, ProduktService produktService) {
		this.excelImport = excelImport;
		this.produktService = produktService;
	}


	@PostMapping(path = "/import")
	public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) {
		try {
			InputStream stream = new BufferedInputStream(file.getInputStream());
			String name = file.getOriginalFilename();
			if(name.contains("xlsx")) {
    			try {
    				XSSFWorkbook wb = new XSSFWorkbook(stream);
					excelImport.importExcel(wb);
					return new ResponseEntity<>("Erfolgreich", HttpStatus.OK);
    			}catch (Exception e) {
    				e.printStackTrace();
                }
			}else {
				try {
					HSSFWorkbook wb = new HSSFWorkbook(stream);
					excelImport.importExcel(wb);
					return new ResponseEntity<>("Erfolgreich", HttpStatus.OK);
    			}catch (Exception e) {
    				e.printStackTrace();
                }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>("Fehler beim Lesen.", HttpStatus.BAD_REQUEST);
	}
	@GetMapping(path = "/suche")
	public List<Produkt> getProdukteNummer(@RequestParam String migelnummer, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date date) {
		if(migelnummer.length() < 1) {
			return getProdukte();
		}
		if(date == null) {
			return produktService.getProdukte(migelnummer);
		}
		return produktService.getProdukt(migelnummer, date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDateTime());
	}
	@GetMapping(path = "")
	public List<Produkt> getProdukte() {
		return produktService.alleProdukte();
	}
	@PostMapping(path = "/xml")
	public  ResponseEntity<List<String>> rechnungTest(@RequestParam("file") MultipartFile file) {
		try {
			InputStream stream = new BufferedInputStream(file.getInputStream());
			String name = file.getOriginalFilename();
			if(name.contains("xml")) {
				ReadXml readXml = new ReadXml(produktService);
				return new ResponseEntity<>(readXml.readXml(stream), HttpStatus.OK);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		return new ResponseEntity<>(List.of("Fehler beim Lesen."), HttpStatus.BAD_REQUEST);
	}
	@PostMapping(path = "/xml/string")
	public  ResponseEntity<List<String>> rechnungTestString(@RequestBody String text) {
		try {
			ReadXml readXml = new ReadXml(produktService);
			return new ResponseEntity<>(readXml.readXml(text), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ResponseEntity<>(List.of("Fehler beim Lesen."), HttpStatus.BAD_REQUEST);
	}
}
