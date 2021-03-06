package org.mdavi.sitecustomizer.services.implementations;

import static org.mdavi.sitecustomizer.model.Cobrand.FIELD_ID;
import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newArrayList;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.mdavi.sitecustomizer.database.dao.ICobrandDAO;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.services.Retriever;

public class PropertyRetriever implements Retriever
{
  private final ICobrandDAO cobrandDAO;

  public PropertyRetriever (ICobrandDAO cobrandDAO)
  {
    this.cobrandDAO = cobrandDAO;
  }

  @Override
  public String getProperty (String cobrandName, String property)
  {
    return getProperty(cobrandName, property, 0);
  }

  @Override
  public String getProperty (String cobrandName, String property, int position)
  {
    List<String> values = getProperties(cobrandName, property);
    return null == values? null : values.get(position);
  }

  @Override
  public List<String> getProperties (String cobrandName, String property)
  {
    return getCobrandProperties(getCobrand(cobrandName), property);
  }

  @Override
  public Set<String> getDomains (String cobrandName)
  {
    return getCobrand(cobrandName).getDomains();
  }

  private Cobrand getCobrand (String cobrandName)
  {
    return cobrandDAO.findOne(FIELD_ID, cobrandName);
  }

  private List<String> getCobrandProperties (Cobrand cobrand, String property)
  {
    if(null == cobrand) return null;
    Collection<String> values = cobrand.getValuesFor(property);
    return null == values? null : newArrayList(values);
  }

}
