package ru.projectosnova.xmltransform;

public class App
{
    final static String FOLDER_SOURCE="resources/dirty";
    final static String FOLDER_TMP="resources/tmp";
    final static String FOLDER_CLEAN="resources/clean";

    public static void main( String[] args )
    {

      System.out.println("App started");
      //XmlTransform.unpack(FOLDER_SOURCE,FOLDER_TMP);
      //XmlTransform.unpack(FOLDER_TMP,FOLDER_TMP);
      //XmlTransform.clean(FOLDER_TMP,FOLDER_CLEAN);
      XmlTransform.convert(FOLDER_CLEAN, "resources/xml/data.xml");

      XmlTransform.export("resources/xml/data.xml","resources/xsl/reestr.xsl","resources/html/reestr.html");
      // XmlTransform.export("resources/xml/data.xml","resources/xsl/bulletin.xsl","resources/html/bulletin.html");
        
      //Csv2Xml.convert("old_reestr.csv", "testdata.xml", ";", "data", "element");


    }

}
