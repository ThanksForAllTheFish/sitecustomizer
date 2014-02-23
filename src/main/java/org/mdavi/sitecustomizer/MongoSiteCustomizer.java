 package org.mdavi.sitecustomizer;

import java.util.List;
import java.util.Set;

import org.mdavi.sitecustomizer.services.Retriever;

public class MongoSiteCustomizer
{
  private final Retriever retriever;

  public MongoSiteCustomizer (Retriever retriever)
  {
    this.retriever = retriever;
  }

  public String getValue (String cobrand, String key)
  {
    return getValue(cobrand, key, 0);
  }
  
  public List<String> getValues (String cobrand, String key)
  {
    return retriever.getProperties(cobrand, key);
  }

  public String getValue (String cobrand, String key, int position)
  {
    List<String> values = getValues(cobrand, key);
    if(position >= 0 && position < values.size())
      return values.get(position);
    return null;
  }

  public Set<String> getDomains (String cobrandName)
  {
    return retriever.getDomains(cobrandName);
  }

}
