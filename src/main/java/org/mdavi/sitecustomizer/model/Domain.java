package org.mdavi.sitecustomizer.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity(value = "domains", noClassnameStored = true)
public class Domain
{

  @Id private final ObjectId id = ObjectId.get();
  
  private final String address = null;
  
  @Reference("institutional")
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
