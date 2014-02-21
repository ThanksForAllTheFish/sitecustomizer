package org.mdavi.sitecustomizer.services;

import java.util.ArrayList;
import java.util.Collection;

import org.bson.types.ObjectId;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mongodb.morphia.dao.DAO;

public class Retriever
{
  private DAO<Cobrand, ObjectId> cobrandDAO;

  public Retriever (DAO<Cobrand, ObjectId> cobrandDAO)
  {
    this.cobrandDAO = cobrandDAO;
  }

  public String getValue (String cobrandName, String property)
  {
    Cobrand cobrand = cobrandDAO.findOne("cobrand", cobrandName);
    return null == cobrand? null : getValue(cobrand, property, 0);
  }

  public String getValue (Cobrand cobrand, String property, int position)
  {
    Collection<String> values = cobrand.getValuesFor(property);
    if(null == values) return null;
    return new ArrayList<>(values).get(position);
  }

}
