package org.mdavi.sitecustomizer;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Set;

import org.jmock.Expectations;
import org.junit.Test;
import org.mdavi.sitecustomizer.services.ICobrandRetriever;
import org.mdavi.sitecustomizer.services.Retriever;

public class DomainsRetrieverTest extends MockableTest
{
  private static final String DOMAIN_ADDRESS = "mdavi.org";
  private static final String COBRAND_NAME = "cobrand";
  private Retriever propertRetriever = context.mock(Retriever.class);
  private ICobrandRetriever cobrandRetriever = context.mock(ICobrandRetriever.class);
  private MongoSiteCustomizer siteCustomizer = new MongoSiteCustomizer(propertRetriever, cobrandRetriever);
  
  @Test
  public void returnCobrandDomains ()
  {
    givenACobrandWithDomains(COBRAND_NAME);
    
    Set<String> domains = whenRetrievingItsDomains(COBRAND_NAME);
  
    thenTheDomainsAreReturned(domains);
  }
  
  private void givenACobrandWithDomains (final String cobrandName)
  {
    context.checking(new Expectations()
    {
      {
        oneOf(propertRetriever).getDomains(cobrandName); will(returnValue(Collections.singleton(DOMAIN_ADDRESS)));
      }
    });
  }
  
  private Set<String> whenRetrievingItsDomains (String cobrandName)
  {
    return siteCustomizer.getDomains(cobrandName);
  }
  
  private void thenTheDomainsAreReturned (Set<String> domains)
  {
    assertThat(domains, contains(DOMAIN_ADDRESS));
  }
}
