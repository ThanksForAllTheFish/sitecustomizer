package org.mdavi.sitecustomizer.services.implementations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.mdavi.sitecustomizer.database.dao.CobrandDAO;
import org.mdavi.sitecustomizer.model.Cobrand;

public class CobrandCreator
{
  private final CobrandDAO cobrandDAO;
  private final Map<String, Collection<String>> defaultSettingsHolder;
  private final Map<String, Set<String>> domainHolder;
  private final Map<String, String> sonToParent;

  public CobrandCreator (CobrandDAO cobrandDAO)
  {
    this.cobrandDAO = cobrandDAO;
    this.defaultSettingsHolder = new HashMap<>();
    this.domainHolder = new HashMap<>();
    this.sonToParent = new HashMap<>();
  }

  /*
   * TODO: optimize to perform only one save per cobrand
   * TODO: cleanup, it's ugly
   * TODO: take care of 'continue'
   * TODO: take care of structures of support
   */
  public void importProperties (Properties properties)
  {
    Cobrand cobrand = null;
    Map<String, Collection<String>> props = null;
    
    Map<String, String> orderedProperties = orderProperties(properties);
    
    for(Object k : orderedProperties.keySet()) {
      String key = (String)k;

      List<String> values = split( properties.getProperty(key), "\\|" );
      List<String> keyElements = split( key, "\\." );

      String mongoKeyName = getPropertyNameInMongoFormat(keyElements.subList(2, keyElements.size()));
      
      if(isDefaultProperty(keyElements.get(0))) {
        addProperty(keyElements.get(1) + "_" + mongoKeyName, values);
        continue;
      }
      
      if(isDomainProperty(keyElements.get(0))) {
        addDomain(keyElements.subList(1, keyElements.size()), properties.getProperty(key));
        continue;
      }

      String cobrandName = keyElements.get(1);
      
      if(isNewCobrand(cobrand, cobrandName) )
        props = newMapOrDefault(cobrandName);
      
      if(isParentProperty(mongoKeyName)) {
        sonToParent.put(cobrandName, values.get(0));
        continue;
      }
      
      props.put(mongoKeyName, values);
      
      cobrand = new Cobrand(cobrandName, props, domainHolder.get(cobrandName));
      
      cobrandDAO.upsert(cobrand);
    }
    
    for(String son : sonToParent.keySet()) {
      Cobrand parent = cobrandDAO.findOne("cobrand", sonToParent.get(son));
      cobrandDAO.upsert(new Cobrand(son, Collections.<String, Collection<String>>emptyMap(), Collections.<String>emptySet(), parent));
    }
  }

  private boolean isParentProperty (String mongoKeyName)
  {
    return "COBRAND_PARENT".equals(mongoKeyName);
  }

  private void addDomain (List<String> domainElements, String cobrandName)
  {
    Set<String> domains = domainHolder.get(cobrandName);
    if(null == domains) {
      domains = new TreeSet<>();
    }
    domains.add(StringUtils.join(domainElements.toArray(), "."));
    domainHolder.put(cobrandName, domains);
  }

  private boolean isDomainProperty (String prefix)
  {
    return "DOMAIN".equals(prefix);
  }

  private boolean isNewCobrand (Cobrand cobrand, String cobrandName)
  {
    return null == cobrand || !cobrandName.equals( cobrand.getCobrand());
  }

  private Map<String, Collection<String>> newMapOrDefault (String cobrandName)
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
    if ("it".equals(language) && "COBRANDTEST".equals(cobrandName)) return true;
    return false;
  }

  private void addProperty (String property, Collection<String> values)
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
    
    return ordered;
  }

  private String getPropertyNameInMongoFormat (List<String> keyElements)
  {
    String mongoKeyName = keyElements.get(0);
    
    for(String element : keyElements.subList(1, keyElements.size()))
      mongoKeyName += "_" + element;
    return mongoKeyName;
  }

  private List<String> split (String property, String separator)
  {
    return Arrays.asList(property.split(separator));
  }

}
