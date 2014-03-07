package org.mdavi.sitecustomizer.services.implementations;

import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newHashMap;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

public class ParentsHolder extends AbstractHolder implements Callable<Map<String, String>>
{
  private final Properties          properties;

  public ParentsHolder (Properties properties)
  {
    this.properties = properties;
  }

  @Override
  public Map<String, String> call () throws Exception
  {
    final Map<String, String> sonToParent = newHashMap();
    for (Object k : properties.keySet())
    {
      String key = (String) k;
  
      List<String> values = split(properties.getProperty(key), "\\|");
      List<String> keyElements = split(key, "\\.");
      if (isParentProperty(getPropertyNameInMongoFormat(keyElements.subList(2, keyElements.size()))))
      {
        sonToParent.put(keyElements.get(1), values.get(0));
      }
    }
    return sonToParent;
  }

  private static boolean isParentProperty (String mongoKeyName)
  {
    return "COBRAND_PARENT".equals(mongoKeyName);
  }
}
