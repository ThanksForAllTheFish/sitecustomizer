package org.mdavi.sitecustomizer.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("cobrands")
public class Cobrand
{
  @Id private ObjectId id;
  
  private String cobrand;
  
  @Embedded("properties")
  private Collection<Map<String, String>> properties;
  
  @Override
  public String toString ()
  {
    
    return cobrand;
  }

  public String getValueFor (String property)
  {
    Iterator<Map<String, String>> propertyIt = properties.iterator();
    if(propertyIt.hasNext())
      return propertyIt.next().get(property);
    return null;
  }

  public String getCobrand ()
  {
    return cobrand;
  }
}
