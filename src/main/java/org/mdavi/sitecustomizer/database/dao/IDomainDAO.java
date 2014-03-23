package org.mdavi.sitecustomizer.database.dao;

import org.bson.types.ObjectId;
import org.mdavi.sitecustomizer.model.Domain;
import org.mongodb.morphia.dao.DAO;

public interface IDomainDAO extends DAO<Domain, ObjectId>
{

}
