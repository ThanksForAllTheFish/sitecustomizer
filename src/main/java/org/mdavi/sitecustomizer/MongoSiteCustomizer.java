 package org.mdavi.sitecustomizer;

import java.util.List;
import java.util.Set;

import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.services.Retriever;
import org.mdavi.sitecustomizer.services.implementations.ICobrandRetriever;

public class MongoSiteCustomizer
{
  private final Retriever propertyRetriever;
  private ICobrandRetriever cobrandRetriever;

  public MongoSiteCustomizer (Retriever retriever, ICobrandRetriever cobrandRetriever)
  {
    this.propertyRetriever = retriever;
    this.cobrandRetriever = cobrandRetriever;
  }

  public String getValue (String cobrand, String key)
  {
    return getValue(cobrand, key, 0);
  }
  
  public List<String> getValues (String cobrand, String key)
  {
    return propertyRetriever.getProperties(cobrand, key);
  }

  public String getValue (String cobrand, String key, int position)
  {
    List<String> values = getValues(cobrand, key);
    if (null != values && position >= 0 && position < values.size()) return values.get(position);
    return null;
  }

  public Set<String> getDomains (String cobrandName)
  {
    return propertyRetriever.getDomains(cobrandName);
  }

  public String getInstitutionalCobrandFor (String address)
  {
    Cobrand cobrand = cobrandRetriever.findInstitutionalCobrand(address);
    return null == cobrand ? null : cobrand.getCobrand();
  }

}
