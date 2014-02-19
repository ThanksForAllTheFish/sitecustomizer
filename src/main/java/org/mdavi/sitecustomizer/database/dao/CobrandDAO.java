package org.mdavi.sitecustomizer.database.dao;

import org.bson.types.ObjectId;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.Mongo;

public class CobrandDAO extends BasicDAO<Cobrand, ObjectId>
{
  public CobrandDAO (Morphia morphia, Mongo mongo, String dbName)
  {
    super(mongo, morphia, dbName);
  }
}
