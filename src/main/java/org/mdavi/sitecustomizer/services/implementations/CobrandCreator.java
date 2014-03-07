package org.mdavi.sitecustomizer.services.implementations;

import static org.mdavi.sitecustomizer.model.Cobrand.FIELD_ID;
import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newHashMap;
import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newTreeMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.mdavi.sitecustomizer.database.dao.CobrandDAO;
import org.mdavi.sitecustomizer.model.Cobrand;

public class CobrandCreator
{
  private final CobrandDAO                      cobrandDAO;
  private final Map<String, Collection<String>> defaultSettingsHolder;
  private final Map<String, Set<String>>        domainHolder;
  private final Map<String, String>             sonToParent;
  private final Map<String, List<String>>       orderedProperties;
  private final ExecutorService                 threadExecutor;

  public CobrandCreator (CobrandDAO cobrandDAO, ExecutorService threadExecutor)
  {
    this.cobrandDAO = cobrandDAO;
    this.threadExecutor = threadExecutor;
    this.defaultSettingsHolder = newHashMap();
    this.domainHolder = newHashMap();
    this.sonToParent = newHashMap();
    this.orderedProperties = newTreeMap();
  }

  public void importProperties (final Properties properties)
  {
    Cobrand cobrand = null;

    orderProperties(properties);

    for (String key : orderedProperties.keySet())
    {
      List<String> keyElements = split(key, "\\_");

      String cobrandName = keyElements.get(0);

      Map<String, Collection<String>> props = newTreeMap();
      if (isNewCobrand(cobrand, cobrandName)) props = prevalorizeWithDefaultValues(cobrandName);

      String mongoKeyName = StringUtils.join(keyElements.subList(1, keyElements.size()), "_");
      props.put(mongoKeyName, orderedProperties.get(key));

      cobrand = new Cobrand(cobrandName, props, domainHolder.remove(cobrandName));

      cobrandDAO.upsert(cobrand);
    }

    updateWithParents();
  }

  private static boolean isNewCobrand (Cobrand cobrand, String cobrandName)
  {
    return null == cobrand || !cobrandName.equals(cobrand.getCobrand());
  }

  private static boolean isInstitutionalFor (String cobrandName, String property)
  {
    String language = property.substring(0, property.indexOf("_"));
    if ("it".equals(language) && "COBRANDTEST".equals(cobrandName)) return true;
    return false;
  }

  private static List<String> split (String property, String separator)
  {
    return Arrays.asList(property.split(separator));
  }

  private void updateWithParents ()
  {
    for (String son : sonToParent.keySet())
    {
      Cobrand parent = cobrandDAO.findOne(FIELD_ID, sonToParent.get(son));
      cobrandDAO.upsert(new Cobrand(son, Collections.<String, Collection<String>>emptyMap(), Collections
          .<String>emptySet(), parent));
    }
  }

  private Map<String, Collection<String>> prevalorizeWithDefaultValues (String cobrandName)
  {
    Map<String, Collection<String>> map = newTreeMap();

    for (String property : defaultSettingsHolder.keySet())
    {
      if (isInstitutionalFor(cobrandName, property)) map.put(property.substring(property.indexOf("_") + 1),
          defaultSettingsHolder.get(property));
    }

    return map;
  }

  private void orderProperties (final Properties properties)
  {
    Future<Map<String, List<String>>> profileFuture = threadExecutor.submit(new ProfilePropertiesHolder(properties));
    
    Future<Map<String, Collection<String>>> defaultFuture = threadExecutor.submit(new DefaultSettingsHolder(properties));

    Future<Map<String, Set<String>>> doaminsFuture = threadExecutor.submit(new DomainsHolder(properties));

    Future<Map<String, String>> sonFuture = threadExecutor.submit(new ParentsHolder(properties));

    try
    {
      orderedProperties.putAll(profileFuture.get());
      defaultSettingsHolder.putAll(defaultFuture.get());
      domainHolder.putAll(doaminsFuture.get());
      sonToParent.putAll(sonFuture.get());
    }
    catch (InterruptedException | ExecutionException e)
    {
      throw new RuntimeException(e);
    }
  }

}
