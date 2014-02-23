package org.mdavi.sitecustomizer.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "cobrands", noClassnameStored = true)
public class Cobrand
{
  @Id private ObjectId id;
  
  private String cobrand;
  
  @Embedded("properties")
  private final Map<String, Collection<String>> properties = Collections.emptyMap();
  
  public Collection<String> getValuesFor (final String property)
  {
    return properties.get(property);
  }

  public String getCobrand ()
  {
    return cobrand;
  }

  public String printableCobrand ()
  {
    return cobrand + " with properties [ " + properties + " ]";
  }
  
  /**
   * Override toString to make it usable in hamcrest matchers
   */
  @Override
  public String toString ()
  { 
    return printableCobrand();
  }
}
