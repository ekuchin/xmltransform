package ru.projectosnova.xmltransform;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class RosreestrRecordTest extends Assert {
    private final String xmlfilename="resources/xml/sample.xml";
    private RosreestrRecord source = new RosreestrRecord(xmlfilename);

    @Test
    public void streetIsNotEmpty(){
        String s = source.getStreet();
        System.out.print("Street is ");
        System.out.println(s);
        assertFalse(s.isEmpty());
    }

    @Test
    public void houseIsNotEmpty(){
        String s = source.getHouse("1");
        System.out.print("House is ");
        System.out.println(s);
        assertFalse(s.isEmpty());
    }

    @Test
    public void apartmentIsNotEmpty(){
        String s = source.getApartment();
        System.out.print("Apartment is ");
        System.out.println(s);
        assertFalse(s.isEmpty());
    }

    @Test
    public void areaIsNotEmpty(){
        String s = source.getArea();
        System.out.print("Area is ");
        System.out.println(s);
        assertFalse(s.isEmpty());
    }

    @Test
    public void cadastralNumberIsNotEmpty(){
        String s = source.getCadastralNumber();
        System.out.print("Cadastral Number is ");
        System.out.println(s);
        assertFalse(s.isEmpty());
    }

    @Test
    public void ownerIsNotEmpty(){
        ArrayList arr = source.getOwners();
        System.out.print("Owners count is ");
        System.out.println(arr.size());
        assertTrue(arr.size()>0);
    }


}
