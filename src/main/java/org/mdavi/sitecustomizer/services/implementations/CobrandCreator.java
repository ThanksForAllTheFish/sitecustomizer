package org.mdavi.sitecustomizer.services.implementations;

import static org.mdavi.sitecustomizer.model.Cobrand.FIELD_ID;
import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newHashMap;
import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newTreeMap;
import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newTreeSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
    this.defaultSettingsHolder = newHashMap();
    this.domainHolder = newHashMap();
    this.sonToParent = newHashMap();
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
      
      if(isParentProperty(mongoKeyName)) {
        sonToParent.put(cobrandName, values.get(0));
        continue;
      }

      Map<String, Collection<String>> props = newTreeMap();
      if(isNewCobrand(cobrand, cobrandName) )
        props = prevalorizeWithDefaultValues(cobrandName);
      
      props.put(mongoKeyName, values);
      
      cobrand = new Cobrand(cobrandName, props, domainHolder.remove(cobrandName));
      
      cobrandDAO.upsert(cobrand);
    }
    
    for(String son : sonToParent.keySet()) {
      Cobrand parent = cobrandDAO.findOne(FIELD_ID, sonToParent.get(son));
      cobrandDAO.upsert(new Cobrand(son, Collections.<String, Collection<String>>emptyMap(), Collections.<String>emptySet(), parent));
    }
  }

  private void addDomain (List<String> domainElements, String cobrandName)
  {
    Set<String> domains = domainHolder.get(cobrandName);
    if(null == domains) {
      domains = newTreeSet();
    }
    domains.add(StringUtils.join(domainElements.toArray(), "."));
    domainHolder.put(cobrandName, domains);
  }

  private void addProperty (String property, Collection<String> values)
  {
    defaultSettingsHolder.put(property, values);
  }

  private Map<String, Collection<String>> prevalorizeWithDefaultValues (String cobrandName)
  {
    Map<String, Collection<String>> map = newTreeMap();
    
    for(String property : defaultSettingsHolder.keySet()) {
      if(isInstitutionalFor(cobrandName, property))
        map.put(property.substring( property.indexOf("_") + 1), defaultSettingsHolder.get(property));
    }
    
    return map;
  }

  private static boolean isParentProperty (String mongoKeyName)
  {
    return "COBRAND_PARENT".equals(mongoKeyName);
  }

  private static boolean isDomainProperty (String prefix)
  {
    return "DOMAIN".equals(prefix);
  }

  private static boolean isNewCobrand (Cobrand cobrand, String cobrandName)
  {
    return null == cobrand || !cobrandName.equals( cobrand.getCobrand());
  }

  private static boolean isInstitutionalFor (String cobrandName, String property)
  {
    String language = property.substring(0, property.indexOf("_"));
    if ("it".equals(language) && "COBRANDTEST".equals(cobrandName)) return true;
    return false;
  }

  private static boolean isDefaultProperty (String prefix)
  {
    return "DEFAULT".equals(prefix);
  }

  private static Map<String, String> orderProperties (Properties properties)
  {
    Map<String, String> ordered = newTreeMap();
    
    for(Object key : properties.keySet())
      ordered.put(key.toString(), properties.get(key).toString());
    
    return ordered;
  }

  private static String getPropertyNameInMongoFormat (List<String> keyElements)
  {
    String mongoKeyName = keyElements.get(0);
    
    for(String element : keyElements.subList(1, keyElements.size()))
      mongoKeyName += "_" + element;
    return mongoKeyName;
  }

  private static List<String> split (String property, String separator)
  {
    return Arrays.asList(property.split(separator));
  }

}
