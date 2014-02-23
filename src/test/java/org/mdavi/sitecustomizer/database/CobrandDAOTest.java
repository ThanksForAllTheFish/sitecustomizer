package org.mdavi.sitecustomizer.database;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mdavi.sitecustomizer.matchers.SiteCustomizerMatchers.equalCobrandContainingPropertiesAndDomains;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mdavi.sitecustomizer.model.Cobrand;

public class CobrandDAOTest extends MongoConfigurator
{
  private Cobrand cobrand;

  @Test
  public void canRetrieveExistingCobrand ()
  {
    whenILookForAnExistingCobrand(EXISTING_COBRAND_NAME);

    thenTheCobrandAndItsPropertiesAreLoaded(EXISTING_COBRAND_NAME, buildSingleProperty(SAMPLE_PROPERTY, SAMPLE_PROPERTY_VALUE), buildSingleDomain(SAMPLE_DOMAIN));
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

  private void thenTheCobrandAndItsPropertiesAreLoaded (String cobrandName, Map<String, Collection<String>> properties, Set<String> domains)
  {
    assertThat(cobrand, equalCobrandContainingPropertiesAndDomains(cobrandName, properties, domains));
  }
}
