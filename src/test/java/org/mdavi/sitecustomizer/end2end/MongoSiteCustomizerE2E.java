package org.mdavi.sitecustomizer.end2end;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.UnknownHostException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mdavi.sitecustomizer.MongoSiteCustomizer;
import org.mdavi.sitecustomizer.database.MongoConfigurator;
import org.mdavi.sitecustomizer.services.PropertyRetriever;
import org.mdavi.sitecustomizer.services.Retriever;

public class MongoSiteCustomizerE2E extends MongoConfigurator
{
  private Retriever           retriever;
  private MongoSiteCustomizer siteCustomizer;

  @Before
  public void init () throws UnknownHostException, NoSuchFieldException, IllegalAccessException
  {
    super.init();
    retriever = new PropertyRetriever(dao);
    siteCustomizer = new MongoSiteCustomizer(retriever);
  }

  @Test
  public void retrieveAProperty ()
  {
    final String value = siteCustomizer.getValue(EXISTING_COBRAND_NAME, SAMPLE_PROPERTY);

    assertThat(value, equalTo(SAMPLE_PROPERTY_VALUE));
  }

  @Test
  public void retrieveDomains ()
  {
    final Set<String> domains = siteCustomizer.getDomains(EXISTING_COBRAND_NAME);

    assertThat(domains, contains(SAMPLE_DOMAIN));
  }
}
