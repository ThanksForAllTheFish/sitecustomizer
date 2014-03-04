package org.mdavi.sitecustomizer.services;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mdavi.sitecustomizer.matchers.SiteCustomizerMatchers.looseEqual;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mdavi.sitecustomizer.database.MongoConfigurator;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.services.implementations.CobrandCreator;

public class CobrandCreatorTest extends MongoConfigurator
{
  private static final String SECONDCOBRANDTEST = "SECONDCOBRANDTEST";
  private static final String COBRANDTEST = "COBRANDTEST";
  private static final String WITHPARENT = "CBWITHPARENT";
  private Properties properties;
  private CobrandCreator creator;
  
  @Before
  public void init() throws UnknownHostException, NoSuchFieldException, IllegalAccessException {
    super.init();
    try
    {
      properties = new Properties();
      properties.load(getClass().getClassLoader().getResourceAsStream("sitecustomizer.properties"));
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    creator = new CobrandCreator(cobrandDAO);
  }

  @Test
  public void canLoadProperties () throws IOException
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne("cobrand", COBRANDTEST);
    
    assertThat(cobrand, looseEqual(COBRANDTEST, 33, 2));
  }
  
  @Test
  public void canLoadDomains () throws IOException
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne("cobrand", COBRANDTEST);
    
    assertThat(cobrand.getDomains(), contains("cobrandtest.com", "www.cobrandtest.com"));
  }
  
  @Test
  public void canLoadParent () throws IOException
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne("cobrand", WITHPARENT);
    
    assertThat(cobrand.getParent().getCobrand(), equalTo(COBRANDTEST));
    assertThat(cobrand.getValuesFor("CAR_ENABLED"), contains("false"));
    
  }
  
  @Test
  public void theLastSettingInTheFileCounts () throws IOException
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne("cobrand", COBRANDTEST);
    
    assertThat(cobrand.getValuesFor("ABBA_GROUP"), contains( "cobrandtest" ));
  }
  
  @Test
  public void pipedValueAreConvertedIntoCollection () throws IOException
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne("cobrand", COBRANDTEST);
    
    assertThat(cobrand.getValuesFor("CALLCENTER_INSURANCE_PHONENUMBER"), contains( "19948104", "lun-dom 24 ore su 24", "EUR 0,12" ));
  }
  
  @Test
  public void defaultSettingsAreOverridden () throws IOException
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne("cobrand", COBRANDTEST);
    
    assertThat(cobrand.getValuesFor("ABBA_GROUP"), contains( "cobrandtest" ));
  }
  
  @Test
  public void cobrandsDoNotInterleave () {
    creator.importProperties(properties);
    
    Cobrand cobrandTest = cobrandDAO.findOne("cobrand", COBRANDTEST);
    Cobrand secondCobrandTest = cobrandDAO.findOne("cobrand", SECONDCOBRANDTEST);
    
    assertThat(cobrandTest, looseEqual(COBRANDTEST, 33, 2));
    assertThat(secondCobrandTest, looseEqual(SECONDCOBRANDTEST, 23, 0));
  }
}
