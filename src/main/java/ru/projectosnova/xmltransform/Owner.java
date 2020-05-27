package ru.projectosnova.xmltransform;

public class Owner{
  private String name;
  private String part;
  private String reg;

  Owner(){
    
  }

  Owner(String name, String part, String registration){
    this.setName(name);
    this.setPart(part);
    this.setRegistration(registration);
  }

  public String getName(){
    return this.name;
  }
  public void setName(String name){
    this.name=name;
  }

  public String getPart(){
    return this.part;
  }
  public void setPart(String part){
    this.part=part;
  }

  public String getRegistration(){
    return this.reg;
  }
  public void setRegistration(String registration){
    this.reg=registration;
  }

}
