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
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mongodb.morphia.Morphia;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
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

  protected CobrandDAO            dao;
  
  private Morphia                 morphia;
  private Mongo                   mongo;

  @BeforeClass
  public static void setup () throws UnknownHostException, IOException
  {
    configuredAndStartFakeMongoDb();

    DB mongoDb = getMongoDb();
    
    BasicDBObject parent = new BasicDBObject("cobrand", PARENT_COBRAND_NAME);
    parent.append("properties", buildSingleProperty(PARENT_PROPERTY, SAMPLE_PROPERTY_VALUE));
    populateWithFakeData(mongoDb, parent);
    
    BasicDBObject cobrand = new BasicDBObject("cobrand", EXISTING_COBRAND_NAME);
    cobrand.append("properties", buildSingleProperty(SAMPLE_PROPERTY, SAMPLE_PROPERTY_VALUE));
    cobrand.append("domains", buildFakeDomains(SAMPLE_DOMAIN));
    cobrand.append("parent", getParentRef(mongoDb, parent) );
    populateWithFakeData(mongoDb, cobrand);
    
    mongoDb.getCollection("cobrands").createIndex(new BasicDBObject("cobrand", 1));
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

    setupCobrandDao();
  }

  protected static Map<String, Collection<String>> buildSingleProperty (String name, String value)
  {
    return buildFakeProperties(name, Collections.singletonList(value));
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

  private static void populateWithFakeData (DB db, BasicDBObject cobrand) throws UnknownHostException
  {
    final DBCollection col = db.getCollection("cobrands");
    col.update(cobrand, cobrand, true, false);
  }

  private void configureMorphia () throws UnknownHostException
  {
    mongo = new MongoClient(HOST, PORT);
  
    morphia = new Morphia();
    morphia.map(Cobrand.class);
  }

  private void setupCobrandDao ()
  {
    dao = new CobrandDAO(morphia, mongo, SITECUSTOMIZER_DB);
  }

  private static DBRef getParentRef (DB mongoDb, BasicDBObject parent)
  {
    return new DBRef(mongoDb, "cobrands", mongoDb.getCollection("cobrands").findOne(parent).get("_id"));
  }

}