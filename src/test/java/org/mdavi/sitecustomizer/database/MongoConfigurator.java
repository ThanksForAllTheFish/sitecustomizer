package org.mdavi.sitecustomizer.database;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mdavi.sitecustomizer.FakeCobrandTest;
import org.mdavi.sitecustomizer.database.dao.CobrandDAO;
import org.mdavi.sitecustomizer.database.dao.DomainDAO;
import org.mdavi.sitecustomizer.database.dao.ICobrandDAO;
import org.mdavi.sitecustomizer.database.dao.IDomainDAO;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.model.Domain;
import org.mongodb.morphia.Morphia;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public abstract class MongoConfigurator extends FakeCobrandTest
{
  private static final String FIELD_INSTITUTIONAL = "institutional";
  private static final String FIELD_PARENT = "parent";
  private static final String FIELD_DOMAINS = "domains";
  private static final String FIELD_PROPERTIES = "properties";
  private static final String FIELD_ID = "_id";
  private static final String COLLECTION_DOMAINS = "domains";
  private static final String COLLECTION_COBRANDS = "cobrands";
  
  protected static final String   SAMPLE_DOMAIN         = "mdavi.org";
  protected static final String   SAMPLE_PROPERTY_VALUE = "value";
  protected static final String   SAMPLE_PROPERTY       = "property";
  protected static final String   EXISTING_COBRAND_NAME = "NAME";
  protected static final String PARENT_PROPERTY = "parentProperty";
  protected static final String PARENT_COBRAND_NAME = "parent";
  
  private static final String     HOST                  = "localhost";
  private static final int        PORT                  = 12345;
  private static final String     SITECUSTOMIZER_DB     = "sitecustomizer";
  private static MongodExecutable mongodExecutable;

  protected ICobrandDAO            cobrandDAO;
  protected IDomainDAO            domainDAO;
  
  private Morphia                 morphia;
  private Mongo                   mongo;

  @BeforeClass
  public static void setup () throws UnknownHostException, IOException
  {
    configuredAndStartFakeMongoDb();

    DB mongoDb = getMongoDb();
    
    BasicDBObject parent = new BasicDBObject(FIELD_ID, PARENT_COBRAND_NAME);
    parent.append(FIELD_PROPERTIES, buildSingleProperty(PARENT_PROPERTY, SAMPLE_PROPERTY_VALUE));
    populateCollectionWithFakeData(mongoDb, parent, COLLECTION_COBRANDS);
    
    BasicDBObject cobrand = new BasicDBObject(FIELD_ID, EXISTING_COBRAND_NAME);
    cobrand.append(FIELD_PROPERTIES, buildSingleProperty(SAMPLE_PROPERTY, SAMPLE_PROPERTY_VALUE));
    cobrand.append(FIELD_DOMAINS, buildFakeDomains(SAMPLE_DOMAIN));
    cobrand.append(FIELD_PARENT, getRef(mongoDb, COLLECTION_COBRANDS, mongoDb.getCollection(COLLECTION_COBRANDS).findOne(parent)) );
    populateCollectionWithFakeData(mongoDb, cobrand, COLLECTION_COBRANDS);
    
    BasicDBObject domain = new BasicDBObject(FIELD_ID, SAMPLE_DOMAIN);
    domain.append(FIELD_INSTITUTIONAL, getRef(mongoDb, COLLECTION_COBRANDS, mongoDb.getCollection(COLLECTION_COBRANDS).findOne(cobrand)));
    populateCollectionWithFakeData(mongoDb, domain, COLLECTION_DOMAINS);
  }

  @AfterClass
  public static void teardown ()
  {
    if (mongodExecutable != null) mongodExecutable.stop();
  }

  @Before
  public void init () throws UnknownHostException, NoSuchFieldException, IllegalAccessException
  {
    configureMorphia();

    setupDaos();
  }

  protected static Map<String, Collection<String>> buildSingleProperty (String name, String value)
  {
    return buildFakeProperties(name, Collections.singletonList(value));
  }

  private void configureMorphia () throws UnknownHostException
  {
    mongo = new MongoClient(HOST, PORT);
  
    morphia = new Morphia();
    morphia.map(Cobrand.class, Domain.class);
  }

  private void setupDaos ()
  {
    cobrandDAO = new CobrandDAO(morphia, mongo, SITECUSTOMIZER_DB);
    domainDAO = new DomainDAO(morphia, mongo, SITECUSTOMIZER_DB);
  }

  private static void configuredAndStartFakeMongoDb () throws UnknownHostException, IOException
  {
    final IMongodConfig config = new MongodConfigBuilder().version(Version.Main.V2_4)
        .net(new Net(PORT, Network.localhostIsIPv6())).build();
    final MongodStarter runtime = MongodStarter.getDefaultInstance();
    mongodExecutable = runtime.prepare(config);
    mongodExecutable.start();
  }

  private static DB getMongoDb () throws UnknownHostException
  {
    final MongoClient mongo = new MongoClient(HOST, PORT);
    return mongo.getDB(SITECUSTOMIZER_DB);
  }

  private static void populateCollectionWithFakeData (DB db, BasicDBObject cobrand, String collection) throws UnknownHostException
  {
    final DBCollection col = db.getCollection(collection);
    col.update(cobrand, cobrand, true, false);
  }

  private static DBRef getRef (DB mongoDb, String collection, DBObject original)
  {
    return new DBRef(mongoDb, collection, original.get(FIELD_ID));
  }

}
