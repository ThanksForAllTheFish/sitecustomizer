package org.mdavi.sitecustomizer.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity(value = "domains", noClassnameStored = true)
public class Domain
{
  public static final String FIELD_ID = "address";
  public static final String FIELD_INSTITUTIONAL = "institutional";

  @Id private final String address = null;
  
  @Reference(FIELD_INSTITUTIONAL)
  private final Cobrand institutional = null;
  
  public String getAddress ()
  {
    return address;
  }
  
  public Cobrand getInstitutional ()
  {
    return institutional;
  }
}
