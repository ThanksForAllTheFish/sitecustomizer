package org.mdavi.sitecustomizer.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity(value = "domains", noClassnameStored = true)
public class Domain
{
  public static final String FIELD_ID = "address";
  public static final String FIELD_INSTITUTIONAL = "institutional";

  @Id private final String address;
  
  @Reference(FIELD_INSTITUTIONAL)
  private final Cobrand institutional;
  
  public Domain() {
    this(null, null);
  }
  
  public Domain (String address, Cobrand institutional)
  {
    this.address = address;
    this.institutional = institutional;
  }

  public String getAddress ()
  {
    return address;
  }
  
  public Cobrand getInstitutional ()
  {
    return institutional;
  }
  
  public String printableDomain ()
  {
    return address + " for institutional cobrand " + institutional.printableCobrand(); 
  }
  
  /**
   * Override toString to make it usable in hamcrest matchers
   */
  @Override
  public String toString ()
  { 
    return printableDomain();
  }
}
