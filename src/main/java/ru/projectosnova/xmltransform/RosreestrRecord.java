package ru.projectosnova.xmltransform;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.util.ArrayList;

public class RosreestrRecord{
  final static String COMMON_PROPERTY="Совместная";
  private Document xmldoc = null;

RosreestrRecord(String xmlFilename){
  try{
    File fXmlFile = new File(xmlFilename);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    xmldoc = dBuilder.parse(fXmlFile);
    //optional, but recommend ed
    //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    xmldoc.getDocumentElement().normalize();
  } catch (Exception e) {
    e.printStackTrace();
  }
}

  public String getStreet(){
    String res="";
    NodeList nList = xmldoc.getElementsByTagName("adrs:Street");
    Node rights=nList.item(0);
    try{
      res=rights.getAttributes().getNamedItem("Name").getNodeValue();
    }
    catch(Exception e){}
    return res;
  }

  public String getHouse(String level){
    String res="";
    NodeList nList = xmldoc.getElementsByTagName("adrs:Level"+level);
    Node rights=nList.item(0);
    try{
      res=rights.getAttributes().getNamedItem("Type").getNodeValue();
      res+=rights.getAttributes().getNamedItem("Value").getNodeValue();
      res = res.replace('/','-');
    }
    catch(Exception e){}
    return res;
  }

public String getArea(){
  NodeList nList = xmldoc.getElementsByTagName("Area");
  Node rights=nList.item(0);
  String res=rights.getTextContent();
  return res;
}

public String getApartment(){
  String res="";
  NodeList nList = xmldoc.getElementsByTagName("adrs:Apartment");
  Node rights=nList.item(0);
  try{
      res=rights.getAttributes().getNamedItem("Value").getNodeValue();
  }
  catch(Exception e){}
  return res;
}

public String getAddressOther(){
  NodeList nList = xmldoc.getElementsByTagName("adrs:Other");
  Node rights=nList.item(0);
  return rights.getTextContent();
}

public String getCadastralNumber(){
  NodeList nodeList = xmldoc.getElementsByTagName("Flat");
  Node node=nodeList.item(0);
  String res=node.getAttributes().getNamedItem("CadastralNumber").getNodeValue();
  return res;
}

public String getVoices(String part){
  String voices=this.getArea();
  if (!part.equals("1") && !part.equals(RosreestrRecord.COMMON_PROPERTY)){
    int drob=part.indexOf("/");
    String sq=this.getArea();

    float ch=Float.parseFloat(part.substring(0,drob));
    float zn=Float.parseFloat(part.substring(drob+1));
    float sqarr=Float.parseFloat(sq);

    voices=String.valueOf(ch/zn*sqarr);
    drob=voices.indexOf(".")+3;
    if(drob>voices.length()){
      drob=voices.length();
    }
    voices=voices.substring(0, drob);
  }
  return voices;
}

public ArrayList<Owner> getOwners(){
  ArrayList<Owner> res = new ArrayList<>();
  NodeList nList = xmldoc.getElementsByTagName("Rights");
  Node rights=nList.item(0);
  if (rights!=null){
    String reg="";String part="1";
    String owner="";
    Node right = rights.getFirstChild();
    while( right.getNextSibling()!=null ){
      if (right.getNodeType()==1){
//Обрабатываем дочерние узлы Right, формируем, корректируем данные
        Node tempnode = right.getFirstChild();
        while(tempnode.getNextSibling()!=null ){
          if(tempnode.getNodeType()==1){
            //System.out.println(tempnode.getNodeName());

            switch (tempnode.getNodeName()) {
              case  ("Owners"):
                owner="";  
                Node ownode = tempnode.getFirstChild();
                while(ownode.getNextSibling()!=null ){
                    if (ownode.getNodeName().equals("Owner")){
                      
                      Node persnode = ownode.getFirstChild();
                      while(persnode.getNextSibling()!=null ){
                          if (persnode.getNodeName().equals("Person")){
                            String f="";String i="";String o="";
                            Node namenode = persnode.getFirstChild();

                            while(namenode.getNextSibling()!=null ){
                              //System.out.println(namenode.getNodeName());
                              if (namenode.getNodeName().equals("FamilyName")){f=namenode.getTextContent();}
                              if (namenode.getNodeName().equals("FirstName")){i=namenode.getTextContent();}
                              if (namenode.getNodeName().equals("Patronymic")){o=namenode.getTextContent();}
                              namenode = namenode.getNextSibling();
                            }
                            if(owner.length()!=0){
                              owner+=", ";
                              part=RosreestrRecord.COMMON_PROPERTY;
                            }
                            owner+=f+" "+i+" "+o;
                            
                          }

                          if (persnode.getNodeName().equals("Organization") || persnode.getNodeName().equals("Governance")){
                            Node namenode = persnode.getFirstChild();
                            namenode = namenode.getNextSibling();
                            owner=namenode.getTextContent();
                          }
                          persnode = persnode.getNextSibling();
                        }
                        System.out.println(owner);
                      }
                      ownode = ownode.getNextSibling();
                }

              break;

              case ("Registration"):
                String n="";String d="";
                Node regnode = tempnode.getFirstChild();
                while(regnode.getNextSibling()!=null ){
                  if (regnode.getNodeName().equals("RegNumber")){n=regnode.getTextContent();};
                  if (regnode.getNodeName().equals("RegDate")){d=regnode.getTextContent();};
                  regnode = regnode.getNextSibling();
                }
                String[] a = d.split("-");
                reg=n+" от "+a[2]+"."+a[1]+"."+a[0];
              break;

              case ("ShareText"):
                part=tempnode.getTextContent();
                int spac=part.indexOf(" ");
                System.out.println(spac);
                if(spac!=-1){
                  part=part.substring(0,spac);
                }
              break;

              case ("Share"):
                Element el = (Element) tempnode;
                part=el.getAttribute("Numerator")+"/"+el.getAttribute("Denominator");
              break;
            }
          }
          tempnode = tempnode.getNextSibling();
        }
        //Здесь кладем значения в атрибуты
        Owner newOwner=new Owner(owner, part, reg);
        res.add(newOwner);
      }
      right = right.getNextSibling();
    }
  }
  else{
    res.add(new Owner("","1",""));
  }
  return res;
}
}
