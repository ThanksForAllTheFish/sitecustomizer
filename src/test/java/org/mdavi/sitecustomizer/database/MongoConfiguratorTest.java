package org.mdavi.sitecustomizer.database;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

public abstract class MongoConfiguratorTest
{

  private static final String HOST = "localhost";
  private static final int PORT = 12345;
  private static final String SITECUSTOMIZER_DB = "sitecustomizer";
  protected static MongodExecutable mongodExecutable;
  protected Morphia morphia;
  protected Mongo mongo;
  protected CobrandDAO dao;

  protected void setupCobrandDao ()
  {
    dao = new CobrandDAO(morphia, mongo, SITECUSTOMIZER_DB);
  }

  protected void configureMorphia () throws UnknownHostException
  {
    mongo = new MongoClient(HOST, PORT);
  
    morphia = new Morphia();
    morphia.map(Cobrand.class);
  }

  protected static void populateWithFakeData (DB db, BasicDBObject cobrand) throws UnknownHostException
  {
    final DBCollection col = db.getCollection("cobrands");
    col.save(cobrand);
  }

  protected static Map<String, Collection<String>> buildSingleProperty (String name, String value)
  {
    final Map<String, Collection<String>> keys = new HashMap<>();
    Collection<String> values = new ArrayList<>();
    values.add(value);
    keys.put(name, values);
    return keys;
  }

  protected static DB getMongoDb () throws UnknownHostException
  {
    final MongoClient mongo = new MongoClient(HOST, PORT);
    final DB db = mongo.getDB(SITECUSTOMIZER_DB);
    return db;
  }

  protected static void configuredAndStartFakeMongoDb () throws UnknownHostException, IOException
  {
    final IMongodConfig config = new MongodConfigBuilder().version(Version.Main.V2_4)
        .net(new Net(PORT, Network.localhostIsIPv6())).build();
    final MongodStarter runtime = MongodStarter.getDefaultInstance();
    mongodExecutable = runtime.prepare(config);
    mongodExecutable.start();
  }

}
