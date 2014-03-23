package org.mdavi.sitecustomizer.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity(value = "cobrands", noClassnameStored = true)
public class Cobrand
{
  public static final String FIELD_ID = "cobrand";
  public static final String FIELD_PROPERTIES = "properties";
  public static final String FIELD_DOMAINS = "domains";
  public static final String FIELD_PARENT = "parent";
  
  @Id
  private final String cobrand;
  
  @Embedded(FIELD_PROPERTIES)
  private final Map<String, Collection<String>> properties;

  @Embedded(FIELD_DOMAINS)
  private final Set<String> domains;
  
  @Reference(FIELD_PARENT)
  private final Cobrand parent;
  
  public Cobrand() {
    this(null, Collections.<String, Collection<String>>emptyMap(), Collections.<String>emptySet());
  }
  
  public Cobrand (String cobrand, Map<String, Collection<String>> props, Set<String> domains)
  {
    this(cobrand, props, domains, null);
  }

  public Cobrand (String cobrand, Map<String, Collection<String>> props, Set<String> domains, Cobrand parent)
  {
    this.cobrand = cobrand;
    properties = props;
    this.domains = domains;
    this.parent = parent;
  }

  public String getCobrand ()
  {
    return cobrand;
  }
  
  public Map<String, Collection<String>> getProperties ()
  {
    return properties;
  }

  public Collection<String> getValuesFor (final String property)
  {
    Collection<String> values = properties.get(property);
    if(hasParent() && null == values) return parent.getValuesFor(property);
    return values;
  }

  public boolean hasDomains ()
  {
    return null != domains && !domains.isEmpty();
  }

  public boolean hasParent ()
  {
    return null != parent;
  }

  public boolean hasProperties ()
  {
    return null != properties && !properties.isEmpty();
  }

  public Set<String> getDomains ()
  {
    return domains;
  }
  
  public Cobrand getParent ()
  {
    return parent;
  }

  public String printableCobrand ()
  {
    return cobrand + " with properties [ " + properties + " ], with domains " + domains + parentString();
  }
  
  /**
   * Override toString to make it usable in hamcrest matchers
   */
  @Override
  public String toString ()
  { 
    return printableCobrand();
  }

  private String parentString ()
  {
    return null == parent ? "" : ", with parent " + parent.cobrand;
  }
}
