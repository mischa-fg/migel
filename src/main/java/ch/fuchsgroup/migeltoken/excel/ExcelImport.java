package ch.fuchsgroup.migeltoken.excel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.fuchsgroup.migeltoken.models.Produkt;
import ch.fuchsgroup.migeltoken.models.ProduktService;



@Service
public class ExcelImport {
	private ProduktService produktService;
		
	@Autowired
	public ExcelImport(ProduktService produktService) {
		this.produktService = produktService;
	}

	public void importExcel(Workbook workbook) {
		 Sheet sheet = workbook.getSheetAt(0);
		 //Zeilen lesen und Lernende erstellen
		 int end = sheet.getLastRowNum();
		 for(int i = 1; i <= end; i++) {
			 Produkt produkt = new Produkt();
			 Row row = sheet.getRow(i);
			 Cell cell  = row.getCell(7);
			 if(cell != null && cell.getStringCellValue().length() > 1) {
				 produkt.setMigelnummer(cell.getStringCellValue());
				 cell = row.getCell(9);
				 produkt.setBezeichnung(cell.getStringCellValue());
				 cell = row.getCell(10);
				 if(cell != null) {
					 produkt.setLimitation(cell.getStringCellValue());
				 }
				 cell = row.getCell(11);
				 if(cell != null) { 
					 produkt.setMenge(cell.getStringCellValue());
				 }
				 cell = row.getCell(12);
				 if(cell!= null && cell.getCellType() == CellType.NUMERIC) {
					 produkt.setHvbSelbstanwendung(BigDecimal.valueOf( cell.getNumericCellValue()));
				 }
				 cell = row.getCell(13);
				 if(cell != null && cell.getCellType() == CellType.NUMERIC) {
					 produkt.setHvbPflege(BigDecimal.valueOf( cell.getNumericCellValue()));
				 }else {
					 produkt.setHvbPflege(BigDecimal.valueOf(0));
				 }
				 cell = row.getCell(14);
				 produkt.setGueltig(cell.getLocalDateTimeCellValue());
				 produktService.addProdukt(produkt);
			 }
		 }
	}
}
