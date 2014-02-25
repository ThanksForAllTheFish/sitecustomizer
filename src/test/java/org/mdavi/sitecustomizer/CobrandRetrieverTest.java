package org.mdavi.sitecustomizer;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.jmock.Expectations;
import org.junit.Test;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.services.Retriever;
import org.mdavi.sitecustomizer.services.implementations.ICobrandRetriever;

public class CobrandRetrieverTest extends MockableTest
{ 
  private static final Map<String, Collection<String>> EMPTY_PROPERTIES = Collections.<String, Collection<String>>emptyMap();
  private static final String DOMAIN_ADDRESS = "mdavi.org";
  private static final String COBRAND_NAME = "cobrand";
  private Retriever propertRetriever = context.mock(Retriever.class);
  private ICobrandRetriever cobrandRetriever = context.mock(ICobrandRetriever.class);
  private MongoSiteCustomizer siteCustomizer = new MongoSiteCustomizer(propertRetriever, cobrandRetriever);
  
  @Test
  public void canRetrieveACobrandName () throws NoSuchFieldException, IllegalAccessException
  {
    givenADomain(DOMAIN_ADDRESS, prepareFakeCobrand(COBRAND_NAME, EMPTY_PROPERTIES));
    
    String cobrandName = whenLookingForItsInstitutionalCobrand(DOMAIN_ADDRESS);
  
    thenTheNameIsRetrieved(cobrandName);
  }
  
  @Test
  public void returnNull () throws NoSuchFieldException, IllegalAccessException
  {
    givenADomainWithoutCobrand(DOMAIN_ADDRESS);
    
    String cobrandName = whenLookingForItsInstitutionalCobrand(DOMAIN_ADDRESS);
  
    thenNullIsReturned(cobrandName);
  }

  private void givenADomain (final String address, final Cobrand cobrand) throws NoSuchFieldException, IllegalAccessException
  {
    context.checking(new Expectations()
    {
      {
        oneOf(cobrandRetriever).findInstitutionalCobrand(address); will(returnValue(cobrand));
      }
    });
  }

  private void givenADomainWithoutCobrand (String domainAddress) throws NoSuchFieldException, IllegalAccessException
  {
    givenADomain(domainAddress, null);
  }

  private String whenLookingForItsInstitutionalCobrand (String address)
  {
    return siteCustomizer.getInstitutionalCobrandFor(address);
  }

  private void thenNullIsReturned (String nullValue)
  {
    assertThat(nullValue, nullValue());
  }

  private void thenTheNameIsRetrieved (String cobrandName)
  {
    assertThat(cobrandName, equalTo(COBRAND_NAME));
  }
}
