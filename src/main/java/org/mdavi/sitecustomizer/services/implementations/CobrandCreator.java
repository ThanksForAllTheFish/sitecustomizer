package org.mdavi.sitecustomizer.services.implementations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.mdavi.sitecustomizer.database.dao.CobrandDAO;
import org.mdavi.sitecustomizer.model.Cobrand;

public class CobrandCreator
{
  private CobrandDAO cobrandDAO;

  public CobrandCreator (CobrandDAO cobrandDAO)
  {
    this.cobrandDAO = cobrandDAO;
  }

  /*
   * TODO: optimize to perform only one save per cobrand
   * It maybe make no sense since there is no warranty that the properties are loaded ordered by cobrand.
   * Cobrands may interleave
   * 
   * NOW there is warranty :)
   */
  public void importProperties (Properties properties)
  {
    Cobrand cobrand = null;
    Map<String, Collection<String>> props = null;
    Map<String, Collection<String>> defaultSettingsHolder = new HashMap<>();;
    
    Map<String, String> orderedProperties = orderProperties(properties);
    
    for(Object k : orderedProperties.keySet()) {
      String key = (String)k;

      Collection<String> values = split( properties.getProperty(key), "\\|" );
      
      String[] keyElements = key.split("\\.");
      String mongoKeyName = getPropertyNameInMongoFormat(keyElements);
      
      if(isDefaultProperty(keyElements[0])) {
        addPropertyTo(defaultSettingsHolder, keyElements[1] + "_" + mongoKeyName, values);
        continue;
      }

      String cobrandName = keyElements[1];
      
      if(null == cobrand || !cobrandName.equals( cobrand.getCobrand()) )
        props = newMapOrDefault(defaultSettingsHolder, cobrandName);
      
      props.put(mongoKeyName, values);
      
      cobrand = new Cobrand(cobrandName, props, Collections.<String>emptySet());
      
      cobrandDAO.upsert(cobrand);
    }
  }

  private Map<String, Collection<String>> newMapOrDefault (Map<String, Collection<String>> defaultSettingsHolder, String cobrandName)
  {
    Map<String, Collection<String>> map = new TreeMap<>();
    
    for(String property : defaultSettingsHolder.keySet()) {
      if(isInstitutionalFor(cobrandName, property))
        map.put(property.substring( property.indexOf("_") + 1), defaultSettingsHolder.get(property));
    }
    
    return map;
  }

  private boolean isInstitutionalFor (String cobrandName, String property)
  {
    String language = property.substring(0, property.indexOf("_"));
    if("it".equals(language) && "COBRANDTEST".equals(cobrandName)) return true;
    return false;
  }

  private void addPropertyTo (Map<String, Collection<String>> defaultSettingsHolder, String property, Collection<String> values)
  {
    defaultSettingsHolder.put(property, values);
  }

  private boolean isDefaultProperty (String prefix)
  {
    return "DEFAULT".equals(prefix);
  }

  private Map<String, String> orderProperties (Properties properties)
  {
    Map<String, String> ordered = new TreeMap<>();
    
    for(Object key : properties.keySet())
      ordered.put(key.toString(), properties.get(key).toString());
    
    for(String key : ordered.keySet())
      System.out.println(key + " -> " + ordered.get(key));
    
    return ordered;
  }

  private String getPropertyNameInMongoFormat (String[] keyElements)
  {
    String mongoKeyName = keyElements[2];
    
    for(int i = 3; i < keyElements.length; i++)
      mongoKeyName += "_" + keyElements[i];
    return mongoKeyName;
  }

  private Collection<String> split (String property, String separator)
  {
    return Arrays.asList(property.split(separator));
  }

}
