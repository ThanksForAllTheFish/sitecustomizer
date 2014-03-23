package org.mdavi.sitecustomizer.services.implementations;

import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newHashMap;
import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newTreeSet;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

public class DomainsHolder extends AbstractHolder implements Callable<Map<String, Set<String>>>
{
  private final Properties         properties;

  public DomainsHolder (Properties properties)
  {
    this.properties = properties;
  }

  @Override
  public Map<String, Set<String>> call () throws Exception
  {
    final Map<String, Set<String>> domainsForCobrand = newHashMap();
    for (Object k : properties.keySet())
    {
      String key = (String) k;
    
      List<String> values = split(properties.getProperty(key), "\\|");
      List<String> keyElements = split(key, "\\.");
      if (isDomainProperty(keyElements.get(0)))
      {
        addDomain(domainsForCobrand, keyElements.subList(1, keyElements.size()), values.get(0));
      }
    }
    return domainsForCobrand;
  }

  private static boolean isDomainProperty (String prefix)
  {
    return "DOMAIN".equals(prefix);
  }

  private void addDomain (Map<String, Set<String>> domainsForCobrand, List<String> domainElements, String cobrandName)
  {
    Set<String> domains = domainsForCobrand.get(cobrandName);
    if (null == domains)
    {
      domains = newTreeSet();
    }
    domains.add(StringUtils.join(domainElements.toArray(), "."));
    domainsForCobrand.put(cobrandName, domains);
  }
}
