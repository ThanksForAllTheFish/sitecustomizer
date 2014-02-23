package org.mdavi.sitecustomizer.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "cobrands", noClassnameStored = true)
public class Cobrand
{
  @Id private final ObjectId id = ObjectId.get();
  
  private final String cobrand = null;
  
  @Embedded("properties")
  private final Map<String, Collection<String>> properties = Collections.emptyMap();

  @Embedded("domains")
  private final Set<String> domains = Collections.emptySet();
  
  public String getCobrand ()
  {
    return cobrand;
  }

  public Collection<String> getValuesFor (final String property)
  {
    return properties.get(property);
  }

  public Set<String> getDomains ()
  {
    return domains;
  }

  public String printableCobrand ()
  {
    return cobrand + " with properties [ " + properties + " ], with domains " + domains;
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
