package org.mdavi.sitecustomizer.services.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.services.Retriever;
import org.mongodb.morphia.dao.DAO;

public class PropertyRetriever implements Retriever
{
  private final DAO<Cobrand, ObjectId> cobrandDAO;

  public PropertyRetriever (DAO<Cobrand, ObjectId> cobrandDAO)
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
    return cobrandDAO.findOne("cobrand", cobrandName);
  }

  private List<String> getCobrandProperties (Cobrand cobrand, String property)
  {
    if(null == cobrand) return null;
    Collection<String> values = cobrand.getValuesFor(property);
    return null == values? null : new ArrayList<>(values);
  }

}
