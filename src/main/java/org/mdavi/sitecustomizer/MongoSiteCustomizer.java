package org.mdavi.sitecustomizer;

import java.util.ArrayList;
import java.util.List;

public class MongoSiteCustomizer
{

  public String getValue (String cobrand, String key)
  {
    return getValue(cobrand, key, 0);
  }
  
  public List<String> getValues (String cobrand, String key)
  {
    List<String> values = new ArrayList<>();
    values.add("value1");
    values.add("value2");
    return values;
  }

  public String getValue (String cobrand, String key, int position)
  {
    List<String> values = getValues(cobrand, key);
    if(position >= 0 && position < values.size())
      return values.get(position);
    return null;
  }

}
