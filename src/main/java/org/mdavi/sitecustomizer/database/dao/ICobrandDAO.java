package org.mdavi.sitecustomizer.database.dao;

import org.bson.types.ObjectId;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mongodb.morphia.dao.DAO;

public interface ICobrandDAO extends DAO<Cobrand, ObjectId>
{
  void upsert (Cobrand cobrand);

}
