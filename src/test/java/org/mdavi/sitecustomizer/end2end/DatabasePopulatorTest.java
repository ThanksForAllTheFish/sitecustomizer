package org.mdavi.sitecustomizer.end2end;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mdavi.sitecustomizer.matchers.SiteCustomizerMatchers.looseEqual;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.mdavi.sitecustomizer.DatabasePopulator;
import org.mdavi.sitecustomizer.database.MongoConfigurator;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.model.Domain;

public class DatabasePopulatorTest extends MongoConfigurator
{
  private static final String WWW_COBRANDTEST_COM = "www.cobrandtest.com";
  private static final String COBRANDTEST_COM = "cobrandtest.com";
  private static final String SECONDCOBRANDTEST = "SECONDCOBRANDTEST";
  private static final String COBRANDTEST = "COBRANDTEST";
  private static final String WITHPARENT = "CBWITHPARENT";
  private Properties properties;
  private DatabasePopulator creator;
  
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
    creator = new DatabasePopulator(cobrandDAO, domainDAO, Executors.newCachedThreadPool());
  }

  @Test
  public void canLoadProperties ()
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne(Cobrand.FIELD_ID, COBRANDTEST);
    
    assertThat(cobrand, looseEqual(COBRANDTEST, 33, 2));
  }
  
  @Test
  public void canLoadCobrandDomainsAssociation ()
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne(Cobrand.FIELD_ID, COBRANDTEST);
    
    assertThat(cobrand.getDomains(), contains(COBRANDTEST_COM, WWW_COBRANDTEST_COM));
  }
  
  @Test
  public void canLoadParent ()
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne(Cobrand.FIELD_ID, WITHPARENT);
    
    assertThat(cobrand.getParent().getCobrand(), equalTo(COBRANDTEST));
    assertThat(cobrand.getValuesFor("CAR_ENABLED"), contains("false"));
  }
  
  @Test
  public void theLastSettingInTheFileCounts ()
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne(Cobrand.FIELD_ID, COBRANDTEST);
    
    assertThat(cobrand.getValuesFor("ABBA_GROUP"), contains( "cobrandtest" ));
  }
  
  @Test
  public void pipedValueAreConvertedIntoCollection ()
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne(Cobrand.FIELD_ID, COBRANDTEST);
    
    assertThat(cobrand.getValuesFor("CALLCENTER_INSURANCE_PHONENUMBER"), contains( "19948104", "lun-dom 24 ore su 24", "EUR 0,12" ));
  }
  
  @Test
  public void defaultSettingsAreOverridden ()
  {
    creator.importProperties(properties);
    
    Cobrand cobrand = cobrandDAO.findOne(Cobrand.FIELD_ID, COBRANDTEST);
    
    assertThat(cobrand.getValuesFor("ABBA_GROUP"), contains( "cobrandtest" ));
  }
  
  @Test
  public void cobrandsDoNotInterleave () {
    creator.importProperties(properties);
    
    Cobrand cobrandTest = cobrandDAO.findOne(Cobrand.FIELD_ID, COBRANDTEST);
    Cobrand secondCobrandTest = cobrandDAO.findOne(Cobrand.FIELD_ID, SECONDCOBRANDTEST);
    
    assertThat(cobrandTest, looseEqual(COBRANDTEST, 33, 2));
    assertThat(secondCobrandTest, looseEqual(SECONDCOBRANDTEST, 23, 0));
  }
  
  @Test
  public void canLoadDomain ()
  {
    creator.importProperties(properties);
    
    Domain dctc = domainDAO.findOne(Domain.FIELD_ID, COBRANDTEST_COM);
    assertThat(dctc.getAddress(), equalTo(COBRANDTEST_COM));
    assertThat(dctc.getInstitutional().getCobrand(), equalTo(COBRANDTEST));

    Domain wdctc = domainDAO.findOne(Domain.FIELD_ID, WWW_COBRANDTEST_COM);
    assertThat(wdctc.getAddress(), equalTo(WWW_COBRANDTEST_COM));
    assertThat(wdctc.getInstitutional().getCobrand(), equalTo(COBRANDTEST));
  }
}
