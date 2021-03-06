package org.mdavi.sitecustomizer.database.dao;

import static org.mdavi.sitecustomizer.model.Cobrand.*;
import static org.mdavi.sitecustomizer.utilities.CollectionsUtils.newArrayList;

import java.util.Collection;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.Mongo;

public class CobrandDAO extends BasicDAO<Cobrand, ObjectId> implements ICobrandDAO
{
  public CobrandDAO (final Morphia morphia, final Mongo mongo, final String dbName)
  {
    super(mongo, morphia, dbName);
  }

  @Override
  public void upsert (Cobrand cobrand)
  {
    Datastore ds = getDatastore();
    Query<Cobrand> query = ds.createQuery(getEntityClass()).field(FIELD_ID).equal(cobrand.getCobrand());
    
    Cobrand existing = findOne(query);
    
    if(null == existing) save(cobrand);
    else 
    {
      UpdateOperations<Cobrand> updateOperations = ds.createUpdateOperations(getEntityClass());
      
      boolean perform = false;
      
      if(cobrand.hasProperties()) {
        Map<String, Collection<String>> properties = cobrand.getProperties();
        for(String property : properties.keySet()) {
          Collection<String> values = properties.get(property);
          updateOperations.set(FIELD_PROPERTIES + "." + property, values);
        }
        perform = true;
      }
      
      if(cobrand.hasDomains()) {
        updateOperations.addAll(FIELD_DOMAINS, newArrayList(cobrand.getDomains()), false);
        perform = true;
      }
      
      if(cobrand.hasParent()){
        updateOperations.set(FIELD_PARENT, cobrand.getParent());
        perform = true;
      }
      if(perform)
      update(query, updateOperations);
    }
  }
}
