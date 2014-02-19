package org.mdavi.sitecustomizer.database;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mdavi.sitecustomizer.matchers.SiteCustomizerMatchers.equalCobrandWithProperties;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mdavi.sitecustomizer.database.dao.CobrandDAO;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mongodb.morphia.Morphia;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class CobrandDAOTest
{
  private static final String     HOST              = "localhost";
  private static final int        PORT              = 12345;
  private static final String     SITECUSTOMIZER_DB = "sitecustomizer";
  private static MongodExecutable mongodExecutable;
  private Morphia morphia;
  private Mongo mongo;
  private CobrandDAO dao;
  private Cobrand cobrand;

  @BeforeClass
  public static void setup () throws UnknownHostException, IOException
  {
    configuredAndStartFakeMongoDb();
    
    populateWithFakeData(getMongoDb(), new BasicDBObject("cobrand", "NAME"), new BasicDBObject("$push", new BasicDBObject("properties", buildSingleProperty("aKey", "aValue"))));
  }
  
  @Before
  public void init () throws UnknownHostException
  {
    configureMorphia();

    setupCobrandDao();
  }

  @AfterClass
  public static void teardown ()
  {
    if (mongodExecutable != null) mongodExecutable.stop();
  }

  @Test
  public void canRetrieveExistingCobrand ()
  {
    whenILookForAnExistingCobrand("NAME");

    thenTheCobrandAndItsPropertiesAreLoaded("NAME", buildSingleProperty("aKey", "aValue"));
  }
  
  @Test
  public void canRetrieveNull ()
  {
    whenILookForANonExistentCobrand("NONE");

    thenNullIsLoaded();
  }
  
  private void whenILookForANonExistentCobrand (final String cobrandName)
  {
    whenILookForAnExistingCobrand(cobrandName);
  }

  private void whenILookForAnExistingCobrand (final String cobrandName)
  {
    cobrand = dao.findOne("cobrand", cobrandName);
  }

  private void thenNullIsLoaded ()
  {
    assertThat(cobrand, nullValue());
  }

  private void thenTheCobrandAndItsPropertiesAreLoaded (String cobrandName, Map<String, String> properties)
  {
    assertThat(cobrand, equalCobrandWithProperties(cobrandName, properties));
  }

  private void setupCobrandDao ()
  {
    dao = new CobrandDAO(morphia, mongo, SITECUSTOMIZER_DB);
  }

  private void configureMorphia () throws UnknownHostException
  {
    mongo = new MongoClient(HOST, PORT);

    morphia = new Morphia();
    morphia.map(Cobrand.class);
  }
  
  private static void populateWithFakeData (DB db, BasicDBObject rootObject, BasicDBObject properties) throws UnknownHostException
  {
    final DBCollection col = db.getCollection("cobrands");
    col.update(rootObject, properties, true, false);
  }

  private static Map<String, String> buildSingleProperty (String name, String value)
  {
    final Map<String, String> keys = new HashMap<>();
    keys.put(name, value);
    return keys;
  }

  private static DB getMongoDb () throws UnknownHostException
  {
    final MongoClient mongo = new MongoClient(HOST, PORT);
    final DB db = mongo.getDB(SITECUSTOMIZER_DB);
    return db;
  }

  private static void configuredAndStartFakeMongoDb () throws UnknownHostException, IOException
  {
    final IMongodConfig config = new MongodConfigBuilder().version(Version.Main.V2_4)
        .net(new Net(PORT, Network.localhostIsIPv6())).build();
    final MongodStarter runtime = MongodStarter.getDefaultInstance();
    mongodExecutable = runtime.prepare(config);
    mongodExecutable.start();
  }
}
