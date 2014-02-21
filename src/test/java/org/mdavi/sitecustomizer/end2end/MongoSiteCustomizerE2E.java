package org.mdavi.sitecustomizer.end2end;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mdavi.sitecustomizer.MongoSiteCustomizer;
import org.mdavi.sitecustomizer.database.MongoConfiguratorTest;
import org.mdavi.sitecustomizer.services.PropertyRetriever;
import org.mdavi.sitecustomizer.services.Retriever;

import com.mongodb.BasicDBObject;

public class MongoSiteCustomizerE2E extends MongoConfiguratorTest
{
  private static final String VALUE = "value";
  private static final String PROPERTY_NAME = "property";
  private static final String COBRAND_NAME = "NAME";

  @BeforeClass
  public static void setup () throws UnknownHostException, IOException
  {
    configuredAndStartFakeMongoDb();
    
    BasicDBObject rootObject = new BasicDBObject("cobrand", COBRAND_NAME);
    rootObject.append("properties", buildSingleProperty(PROPERTY_NAME, VALUE));
    populateWithFakeData(getMongoDb(), rootObject);
  }
  
  @Before
  public void init () throws UnknownHostException, NoSuchFieldException, IllegalAccessException
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
  public void retrieveAProperty ()
  {
    Retriever retriever = new PropertyRetriever(dao);
    MongoSiteCustomizer siteCustomizer = new MongoSiteCustomizer(retriever);
    
    String value = siteCustomizer.getValue(COBRAND_NAME, PROPERTY_NAME);
    
    assertThat(value, equalTo(VALUE));
  }

}
