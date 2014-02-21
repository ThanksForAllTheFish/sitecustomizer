package org.mdavi.sitecustomizer.services;

import java.util.List;

public interface Retriever
{

  String getProperty (String cobrandName, String property);

  String getProperty (String cobrandName, String property, int position);

  List<String> getProperties (String cobrandName, String property);

}
