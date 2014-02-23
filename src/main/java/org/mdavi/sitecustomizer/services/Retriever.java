package org.mdavi.sitecustomizer.services;

import java.util.List;
import java.util.Set;

public interface Retriever
{

  String getProperty (String cobrandName, String property);

  String getProperty (String cobrandName, String property, int position);

  List<String> getProperties (String cobrandName, String property);

  Set<String> getDomains (String cobrandName);

}
