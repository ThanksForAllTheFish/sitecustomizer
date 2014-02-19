package org.mdavi.sitecustomizer.database;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
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
    
    populateWithFakeData();
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
  public void canRetrieveExistingCobrand () throws Exception
  {
    whenILookForAnExistingCobrand("NAME");

    thenTheCobrandAndItsPropertiesAreLoaded();
  }
  
  @Test
  public void canRetrieveNull () throws Exception
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

  private void thenTheCobrandAndItsPropertiesAreLoaded ()
  {
    final Map<String, String> properties = new HashMap<>();
    properties.put("aKey", "aValue");
    assertThat(cobrand, equalCobrandWithProperties("NAME", properties));
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
  
  private static void populateWithFakeData () throws UnknownHostException
  {
    final MongoClient mongo = new MongoClient(HOST, PORT);
    final DB db = mongo.getDB(SITECUSTOMIZER_DB);
    final DBCollection col = db.createCollection("cobrands", new BasicDBObject());
    final Map<String, String> keys = new HashMap<>();
    keys.put("aKey", "aValue");
    final BasicDBObject query = new BasicDBObject("cobrand", "NAME");
    final BasicDBObject update = new BasicDBObject("$push", new BasicDBObject("properties", keys));
    col.update(query, update, true, false);
  }

  private static void configuredAndStartFakeMongoDb () throws UnknownHostException, IOException
  {
    final IMongodConfig config = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
        .net(new Net(PORT, Network.localhostIsIPv6())).build();
    final MongodStarter runtime = MongodStarter.getDefaultInstance();
    mongodExecutable = runtime.prepare(config);
    mongodExecutable.start();
  }
}
