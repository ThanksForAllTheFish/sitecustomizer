package org.mdavi.sitecustomizer.services.implementations;

import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newTreeMap;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

public class ProfilePropertiesHolder extends AbstractHolder implements Callable<Map<String, List<String>>>
{
  private final Properties properties;

  public ProfilePropertiesHolder (Properties properties)
  {
    this.properties = properties;
  }

  @Override
  public Map<String, List<String>> call () throws Exception
  {
    Map<String, List<String>> orderedProperties = newTreeMap();
    for (Object k : properties.keySet())
    {
      String key = (String) k;

      List<String> values = split(properties.getProperty(key), "\\|");
      List<String> keyElements = split(key, "\\.");
      if (isProfileProperties(keyElements))
      {
        orderedProperties.put(getPropertyNameInMongoFormat(keyElements.subList(1, keyElements.size())), values);
      }
    }
    return orderedProperties;
  }

  private static boolean isProfileProperties (List<String> keyElements)
  {
    return "PROFILE".equals(keyElements.get(0));
  }
}
