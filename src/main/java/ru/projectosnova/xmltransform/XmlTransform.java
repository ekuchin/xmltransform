package ru.projectosnova.xmltransform;

//import java.util.UUID;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

import java.io.File;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlTransform {

    public static void unpack(String folderIn, String folderOut){
        final File folder=new File(folderIn);
        String ext;String name;
      
        File[] folderEntries = folder.listFiles();
          for (File entry : folderEntries){
            if (!entry.isDirectory()){
      
              name = entry.getName(); // получим название файла
              ext = name.substring(name.length()-4);
      
              if (ext.equals(".zip")){
      
              try(ZipInputStream zin = new ZipInputStream(new FileInputStream(entry.getPath()))){
                  ZipEntry zipentry;
                  while((zipentry=zin.getNextEntry())!=null){
                      name = zipentry.getName(); // получим название файла
                      FileOutputStream fout = new FileOutputStream(folderOut+"/" + name);
                        for (int c = zin.read(); c != -1; c = zin.read()) {
                            fout.write(c);
                        }
                        fout.flush();
                        zin.closeEntry();
                        fout.close();
                      }
                }
              catch(Exception ex){
                  ex.printStackTrace();
              }
              }
            }
          }
      }
 
      public static void clean(String folderIn, String folderOut){
        final File folder=new File(folderIn);
        File[] folderEntries = folder.listFiles();
        String ext;String name;
      
        for (File entry : folderEntries){

            if (!entry.isDirectory()){
              name = entry.getName(); // получим название файла
              ext = name.substring(name.length()-4);
              if (ext.equals(".xml")){
                try{

                  RosreestrRecord record = new RosreestrRecord(entry.getPath());
                  String ul=record.getStreet();
                  String dom=record.getHouse();
                  String kv=record.getApartment();
                  String sq=record.getArea();

                  System.out.println(entry.getName());
                  String outfile=ul+" "+dom+" "+String.format("%1$3s", kv)+" "+sq;
                  System.out.println(outfile);
                    //копировать файл
                  Files.copy(Paths.get(entry.getPath()), new FileOutputStream(folderOut+"/"+outfile+".xml"));
                  //}
      
                }catch (Exception e) {
                  e.printStackTrace();
                }
      
              }
            }
        }
      
      }

      public static void convert(String folderName, String outFileName) {
        final File folder = new File(folderName);
        File[] folderEntries = folder.listFiles();
        Document xmlresult;
        try {
    
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder = factory.newDocumentBuilder();
          xmlresult = builder.newDocument();
          Element root = xmlresult.createElement("data");
          xmlresult.appendChild(root);
    
          for (File entry : folderEntries) {
            // File fXmlFile = new File(entry.getPath());
            System.out.println(entry.getName());
            RosreestrRecord record = new RosreestrRecord(entry.getPath());
    
            for (Owner owner : record.getOwners()) {
              Element node = xmlresult.createElement("element");
              root.appendChild(node);
              node.appendChild(xmlresult.createTextNode(entry.getName()));
              node.setAttribute("kv", record.getApartment());
              node.setAttribute("sq", record.getArea());
              node.setAttribute("part", owner.getPart());
              node.setAttribute("reg", owner.getRegistration());
              node.setAttribute("owner", owner.getName());
              // node.setAttribute("guid",UUID.randomUUID().toString());
              node.setAttribute("id", record.getCadastralNumber());
              node.setAttribute("voices", record.getVoices(owner.getPart()));
            }
          }
    
          TransformerFactory transformerFactory = TransformerFactory.newInstance();
          Transformer transformer = transformerFactory.newTransformer();
    
          transformer.setOutputProperty(OutputKeys.INDENT, "yes");
          DOMSource source = new DOMSource(xmlresult);
    
          StreamResult file = new StreamResult(new File(outFileName));
          transformer.transform(source, file);
    
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

      public static void export(String sourceFileName, String xslFileName, String outFileName) {
        try {
    
          TransformerFactory factory = TransformerFactory.newInstance();
          StreamSource xslt = new StreamSource(new File(xslFileName));
          Transformer transformer = factory.newTransformer(xslt);
    
          StreamSource text = new StreamSource(new File(sourceFileName));
          transformer.transform(text, new StreamResult(new File(outFileName)));
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

}