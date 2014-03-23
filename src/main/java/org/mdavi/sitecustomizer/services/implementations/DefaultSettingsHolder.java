package org.mdavi.sitecustomizer.services.implementations;

import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newHashMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

public class DefaultSettingsHolder extends AbstractHolder implements Callable<Map<String, Collection<String>>>
{
  private final Properties                      properties;

  public DefaultSettingsHolder (Properties properties)
  {
    this.properties = properties;
  }

  @Override
  public Map<String, Collection<String>> call () throws Exception
  {
    final Map<String, Collection<String>> defaultSettings = newHashMap();
    for (Object k : properties.keySet())
    {
      String key = (String) k;
    
      List<String> values = split(properties.getProperty(key), "\\|");
      List<String> keyElements = split(key, "\\.");
      if (isDefaultProperty(keyElements.get(0)))
      {
        String mongoKeyName = getPropertyNameInMongoFormat(keyElements.subList(1, keyElements.size()));
        defaultSettings.put(mongoKeyName, values);
      }
    }
    return defaultSettings;
  }

  private static boolean isDefaultProperty (String prefix)
  {
    return "DEFAULT".equals(prefix);
  }
}
