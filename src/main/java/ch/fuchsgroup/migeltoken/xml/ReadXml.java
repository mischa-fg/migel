package ch.fuchsgroup.migeltoken.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ch.fuchsgroup.migeltoken.models.Produkt;
import ch.fuchsgroup.migeltoken.models.ProduktService;



public class ReadXml {
	private ProduktService produktService;
	
	@Autowired
	public ReadXml(ProduktService produktService) {
		this.produktService = produktService;
	}
	
	public List<String> readXml(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		List<String> output = new ArrayList<String>();
		String s = "";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbf.newDocumentBuilder();
		Document document = dBuilder.parse(is);
		document.getDocumentElement().normalize();
		NodeList list = document.getElementsByTagName("invoice:record_other");
		for (int temp = 0; temp < list.getLength(); temp++) {

              Node node = list.item(temp);

              if (node.getNodeType() == Node.ELEMENT_NODE) {

                  Element element = (Element) node;
                  List<Produkt> produkte = produktService.getProdukt(element.getAttribute("code"), LocalDateTime.parse(element.getAttribute("date_begin")));
                  if(produkte.size() > 0) {
                	  Produkt produkt = produkte.get(0);
                	
                	  if(produkt.getHvbPflege().compareTo(BigDecimal.valueOf(Double.parseDouble(element.getAttribute("unit"))))<0) {
                		 // s = element.getAttribute("record_id")+ ", " +element.getAttribute("code")+", " + element.getAttribute("date_begin")+ ", Nicht OK";
                		  s = element.getAttribute("record_id")+ ", " +element.getAttribute("code")+", " + element.getAttribute("date_begin")+ ", HVB " +BigDecimal.valueOf(Double.parseDouble(element.getAttribute("unit"))).subtract(produkt.getHvbPflege())+ " zu viel, "+produkt.getHvbPflege();
                	  }else {
                		  s = element.getAttribute("record_id")+ ", " +element.getAttribute("code")+", " + element.getAttribute("date_begin")+ ", OK";
                	  }
                	  
                  }else {
                	  if(produktService.getProdukte(element.getAttribute("code")).size() > 0) {
                		  s = element.getAttribute("record_id")+ ", " +element.getAttribute("code")+", " + element.getAttribute("date_begin")+ ", "+ "Keine Daten für diesen Zeitraum";
                	  }else {
                		  s = element.getAttribute("record_id")+ ", " +element.getAttribute("code")+", " + element.getAttribute("date_begin")+ ", Migelnummer unbekannt";
                	  }
                	  
                  }
                  output.add(s);
              }
          }
		return output;
	}
	public List<String> readXml(String is) throws ParserConfigurationException, SAXException, IOException {
		List<String> output = new ArrayList<String>();
		String s = "";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbf.newDocumentBuilder();
		Document document = dBuilder.parse(new InputSource(new StringReader(is)));
		document.getDocumentElement().normalize();
		NodeList list = document.getElementsByTagName("invoice:record_other");
		for (int temp = 0; temp < list.getLength(); temp++) {

              Node node = list.item(temp);

              if (node.getNodeType() == Node.ELEMENT_NODE) {

                  Element element = (Element) node;
                  List<Produkt> produkte = produktService.getProdukt(element.getAttribute("code"), LocalDateTime.parse(element.getAttribute("date_begin")));
                  if(produkte.size() > 0) {
                	  Produkt produkt = produkte.get(0);
                	
                	  if(produkt.getHvbPflege().compareTo(BigDecimal.valueOf(Double.parseDouble(element.getAttribute("unit"))))<0) {
                 		 // s = element.getAttribute("record_id")+ ", " +element.getAttribute("code")+", " + element.getAttribute("date_begin")+ ", Nicht OK";
                 		  s = element.getAttribute("record_id")+ ", " +element.getAttribute("code")+", " + element.getAttribute("date_begin")+ ", HVB " +BigDecimal.valueOf(Double.parseDouble(element.getAttribute("unit"))).subtract(produkt.getHvbPflege())+ " zu viel, "+produkt.getHvbPflege();
                 	  }else {
                 		  s = element.getAttribute("record_id")+ ", " +element.getAttribute("code")+", " + element.getAttribute("date_begin")+ ", OK";
                 	  }
                 	  
                   }else {
                 	  if(produktService.getProdukte(element.getAttribute("code")).size() > 0) {
                 		  s = element.getAttribute("record_id")+ ", " +element.getAttribute("code")+", " + element.getAttribute("date_begin")+ ", "+ "Keine Daten für diesen Zeitraum";
                 	  }else {
                 		  s = element.getAttribute("record_id")+ ", " +element.getAttribute("code")+", " + element.getAttribute("date_begin")+ ", Migelnummer unbekannt";
                 	  }
                  }
                  output.add(s);
              }
          }
		return output;
	}
}
