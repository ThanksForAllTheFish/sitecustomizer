package org.mdavi.sitecustomizer.services;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mdavi.sitecustomizer.matchers.SiteCustomizerMatchers.equalCobrandContainingPropertiesAndDomains;

import java.util.Collection;
import java.util.Collections;

import org.jmock.Expectations;
import org.junit.Test;
import org.mdavi.sitecustomizer.MockableTest;
import org.mdavi.sitecustomizer.database.dao.IDomainDAO;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.services.implementations.CobrandRetriever;

public class CobrandRetrieverTest extends MockableTest
{
  private final IDomainDAO domainDAO = context.mock(IDomainDAO.class);
  private ICobrandRetriever cobrandRetriever = new CobrandRetriever(domainDAO);

  @Test
  public void canRetrieveACobrand () throws NoSuchFieldException, IllegalAccessException
  {
    givenAnExistingDomain("mdavi.org");
    
    Cobrand cobrand = whenLookForTheInstitutionalCobrand("mdavi.org");
    
    thenTheCobrandAndItsPropertiesAreRetrieved(cobrand);
  }
  
  @Test
  public void canRetrieveNull () throws NoSuchFieldException, IllegalAccessException
  {
    givenANonExistentDomain("whoami.org");
    
    Cobrand cobrand = whenLookForTheInstitutionalCobrand("whoami.org");
    
    thenNothingIsRetrieved(cobrand);
  }

  private void givenAnExistingDomain (final String address) throws NoSuchFieldException, IllegalAccessException
  {
    context.checking(new Expectations()
    {
      {
        oneOf(domainDAO).findOne("address", address); will(returnValue(buildFakeDomain(address)));
      }
    });
  }
  
  private void givenANonExistentDomain (final String address) throws NoSuchFieldException, IllegalAccessException
  {
    context.checking(new Expectations()
    {
      {
        oneOf(domainDAO).findOne("address", address); will(returnValue(null));
      }
    });
  }

  private Cobrand whenLookForTheInstitutionalCobrand (String address)
  {
    return cobrandRetriever.findInstitutionalCobrand(address);
  }

  private void thenTheCobrandAndItsPropertiesAreRetrieved (Cobrand cobrand)
  {
    assertThat(cobrand, equalCobrandContainingPropertiesAndDomains("cobrand", Collections.<String, Collection<String>>emptyMap(), buildFakeDomains("mdavi.org")));
  }

  private void thenNothingIsRetrieved (Cobrand cobrand)
  {
    assertThat(cobrand, nullValue());
  }

}
