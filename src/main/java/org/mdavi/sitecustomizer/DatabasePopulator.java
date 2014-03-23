package org.mdavi.sitecustomizer;

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
import org.mdavi.sitecustomizer.database.dao.ICobrandDAO;
import org.mdavi.sitecustomizer.database.dao.IDomainDAO;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.model.Domain;
import org.mdavi.sitecustomizer.services.implementations.DefaultSettingsHolder;
import org.mdavi.sitecustomizer.services.implementations.DomainsHolder;
import org.mdavi.sitecustomizer.services.implementations.ParentsHolder;
import org.mdavi.sitecustomizer.services.implementations.ProfilePropertiesHolder;

public class DatabasePopulator
{
  private final ICobrandDAO                     cobrandDAO;
  private final IDomainDAO                      domainDAO;
  private final Map<String, Collection<String>> defaultSettingsHolder;
  private final Map<String, Set<String>>        domainHolder;
  private final Map<String, String>             sonToParent;
  private final Map<String, List<String>>       orderedProperties;
  private final ExecutorService                 threadExecutor;

  public DatabasePopulator (ICobrandDAO cobrandDAO, IDomainDAO domainDAO, ExecutorService threadExecutor)
  {
    this.cobrandDAO = cobrandDAO;
    this.domainDAO = domainDAO;
    this.threadExecutor = threadExecutor;
    this.defaultSettingsHolder = newHashMap();
    this.domainHolder = newHashMap();
    this.sonToParent = newHashMap();
    this.orderedProperties = newTreeMap();
  }

  public void importProperties (final Properties properties)
  {
    fillSupportStructure(properties);

    createCobrands();
  }

  private void fillSupportStructure (final Properties properties)
  {
    Future<Map<String, List<String>>> profileFuture = threadExecutor.submit(new ProfilePropertiesHolder(properties));
  
    Future<Map<String, Collection<String>>> defaultFuture = threadExecutor
        .submit(new DefaultSettingsHolder(properties));
  
    Future<Map<String, Set<String>>> domainsFuture = threadExecutor.submit(new DomainsHolder(properties));
  
    Future<Map<String, String>> sonFuture = threadExecutor.submit(new ParentsHolder(properties));
  
    try
    {
      orderedProperties.putAll(profileFuture.get());
      defaultSettingsHolder.putAll(defaultFuture.get());
      domainHolder.putAll(domainsFuture.get());
      sonToParent.putAll(sonFuture.get());
    }
    catch (InterruptedException | ExecutionException e)
    {
      throw new RuntimeException(e);
    }
  }

  private void createCobrands ()
  {
    Cobrand cobrand = null;
    for (String key : orderedProperties.keySet())
    {
      List<String> keyElements = split(key, "\\_");
  
      String cobrandName = keyElements.get(0);
  
      Map<String, Collection<String>> props = newTreeMap();
      if (isNewCobrand(cobrand, cobrandName)) props = prevalorizeWithDefaultValues(cobrandName);
  
      String mongoKeyName = StringUtils.join(keyElements.subList(1, keyElements.size()), "_");
      props.put(mongoKeyName, orderedProperties.get(key));
  
      final Set<String> domains = domainHolder.remove(cobrandName);
  
      cobrand = new Cobrand(cobrandName, props, domains);
  
      cobrandDAO.upsert(cobrand);
  
      if (null != domains) updateWithDomains(cobrand, domains);
    }
  
    updateWithParents();
  }

  private void updateWithDomains (final Cobrand cobrand, final Set<String> domains)
  {
    threadExecutor.execute(new Runnable()
    {
      @Override
      public void run ()
      {
        for (String d : domains)
        {
          Domain domain = new Domain(d, cobrand);
          domainDAO.save(domain);
        }
      }
    });
  }

  private void updateWithParents ()
  {
    for (String son : sonToParent.keySet())
    {
      Cobrand parent = cobrandDAO.findOne(Cobrand.FIELD_ID, sonToParent.get(son));
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

}
