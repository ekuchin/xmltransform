package ru.projectosnova.xmltransform;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;

public class Csv2Xml {
    public static void convert(String csvFileName, String xmlFileName, String csvSplit, String rootElementName, String commonElementName) {
        BufferedReader br = null;
    
        System.out.println("Started converting for CSV - "+csvFileName+" to XML - "+xmlFileName);

        try {
    
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            //Create root element
            Element rootElement = doc.createElement(rootElementName);
            doc.appendChild(rootElement);
    
            br = new BufferedReader(new FileReader(csvFileName));
            //Read config - first line in csv file
            String line = br.readLine();
            String[] attr=line.split(csvSplit);

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] arr = line.split(csvSplit);
                //Create child elements and fill attributes
                Element element = doc.createElement(commonElementName);
                for (int i = 0; i < attr.length; i++) {
                    element.setAttribute(attr[i], arr[i]); 
                }
                rootElement.appendChild(element);
          }
    
          // Write XML
          // write the content into xml file
          TransformerFactory transformerFactory = TransformerFactory.newInstance();
          Transformer transformer = transformerFactory.newTransformer();
          transformer.setOutputProperty(OutputKeys.INDENT, "yes");
          DOMSource source = new DOMSource(doc);
          StreamResult result = new StreamResult(new File(xmlFileName));

          transformer.transform(source, result);
    
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (ParserConfigurationException e) {
          e.printStackTrace();
        } catch (TransformerException e) {
          e.printStackTrace();
        } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("Finished converting");
          }
}