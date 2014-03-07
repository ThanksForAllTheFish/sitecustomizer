package org.mdavi.sitecustomizer.services.implementations;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractHolder
{

  protected static String getPropertyNameInMongoFormat (List<String> keyElements)
  {
    String mongoKeyName = keyElements.get(0);
    
    for(String element : keyElements.subList(1, keyElements.size()))
      mongoKeyName += "_" + element;
    return mongoKeyName;
  }
  
  protected static List<String> split (String property, String separator)
  {
    return Arrays.asList(property.split(separator));
  }

}
