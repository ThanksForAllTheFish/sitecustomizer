package org.mdavi.sitecustomizer.database;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mdavi.sitecustomizer.matchers.SiteCustomizerMatchers.equalCobrandWithProperties;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mdavi.sitecustomizer.model.Cobrand;

import com.mongodb.BasicDBObject;

public class CobrandDAOTest extends MongoConfiguratorTest
{
  private Cobrand cobrand;

  @BeforeClass
  public static void setup () throws UnknownHostException, IOException
  {
    configuredAndStartFakeMongoDb();
    
    BasicDBObject rootObject = new BasicDBObject("cobrand", "NAME");
    rootObject.append("properties", buildSingleProperty("aKey", "aValue"));
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

  private void thenTheCobrandAndItsPropertiesAreLoaded (String cobrandName, Map<String, Collection<String>> properties)
  {
    assertThat(cobrand, equalCobrandWithProperties(cobrandName, properties));
  }
}
