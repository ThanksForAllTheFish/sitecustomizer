package org.mdavi.sitecustomizer.services.implementations;

import org.bson.types.ObjectId;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.model.Domain;
import org.mongodb.morphia.dao.DAO;

public class CobrandRetriever implements ICobrandRetriever
{
  private final DAO<Domain, ObjectId> domainDAO;

  public CobrandRetriever (DAO<Domain, ObjectId> domainDAO)
  {
    this.domainDAO = domainDAO;
  }

  @Override
  public Cobrand findInstitutionalCobrand (String address)
  {
    Domain domain = domainDAO.findOne("address", address);
    return null == domain? null : domain.getInstitutional();
  }

}
