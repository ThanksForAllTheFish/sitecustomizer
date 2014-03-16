package org.mdavi.sitecustomizer.database.dao;

import org.bson.types.ObjectId;
import org.mdavi.sitecustomizer.model.Domain;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.Mongo;

public class DomainDAO extends BasicDAO<Domain, ObjectId> implements IDomainDAO
{
  public DomainDAO (final Morphia morphia, final Mongo mongo, final String dbName)
  {
    super(mongo, morphia, dbName);
  }

}
