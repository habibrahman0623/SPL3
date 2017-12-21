package test;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XMLPerser {
	
	public static void main(String[] args) {

	      try {
	         File inputFile = new File("short_desc.xml");
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	         NodeList nList = doc.getElementsByTagName("report");
	         System.out.println("----------------------------");
	         
	         
	         PrintWriter writer = new PrintWriter("description.txt", "UTF-8");
	         String report = "";
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               report = eElement.getAttribute("id");
	               
	               NodeList nodelist = eElement.getElementsByTagName("what");
	               //for(int i = 0; i < nodelist.getLength(); i++){
		               
		              report = report + " " + eElement.getElementsByTagName("what").item(0).getTextContent();
		               
		  	           
	               //}
	               writer.println(report);
	               
	               
	            }
	            
	         }
	         writer.close();
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }

}
