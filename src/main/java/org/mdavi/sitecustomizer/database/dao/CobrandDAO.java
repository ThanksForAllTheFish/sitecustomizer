package org.mdavi.sitecustomizer.database.dao;

import org.bson.types.ObjectId;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.Mongo;

public class CobrandDAO extends BasicDAO<Cobrand, ObjectId>
{
  public CobrandDAO (final Morphia morphia, final Mongo mongo, final String dbName)
  {
    super(mongo, morphia, dbName);
  }

  public void upsert (Cobrand cobrand)
  {
    Datastore ds = getDatastore();
    Query<Cobrand> query = ds.createQuery(getEntityClass()).field("cobrand").equal(cobrand.getCobrand());
    
    Cobrand existing = findOne(query);
    
    if(null == existing) save(cobrand);
    else 
    {
      UpdateOperations<Cobrand> updateOperations = ds.createUpdateOperations(getEntityClass()).set("properties", cobrand.getProperties());
      
      if(cobrand.hasDomains())
        updateOperations.add("domains", cobrand.getDomains());
      
      if(cobrand.hasParent())
        updateOperations.add("parent", cobrand.getParent());
      
      update(query, updateOperations);
    }
  }
}
