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
  
  @Embedded("keys")
  private Collection<Map<String, String>> keys;
  
  public Collection<Map<String, String>> getKeys ()
  {
    return keys;
  }
  
  @Override
  public String toString ()
  {
    
    return cobrand;
  }

  public String getValueFor (String key)
  {
    Iterator<Map<String, String>> keyIt = keys.iterator();
    if(keyIt.hasNext())
      return keyIt.next().get(key);
    return null;
  }

  public String getCobrand ()
  {
    return cobrand;
  }
}
