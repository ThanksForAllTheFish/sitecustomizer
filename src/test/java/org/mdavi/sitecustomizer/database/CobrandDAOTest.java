package org.mdavi.sitecustomizer.database;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mdavi.sitecustomizer.matchers.SiteCustomizerMatchers.equalCobrandContainingPropertiesAndDomains;

import java.util.Collection;
import java.util.Collections;
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

    thenTheCobrandAndItsPropertiesAreLoaded(EXISTING_COBRAND_NAME, buildSingleProperty(SAMPLE_PROPERTY, SAMPLE_PROPERTY_VALUE), buildFakeDomains(SAMPLE_DOMAIN));
  }
  
  @Test
  public void canRetrieveParentCobrand ()
  {
    whenILookForTheParentCobrandOf(EXISTING_COBRAND_NAME);
    
    thenTheCobrandAndItsPropertiesAreLoaded(PARENT_COBRAND_NAME, buildSingleProperty(PARENT_PROPERTY, SAMPLE_PROPERTY_VALUE), Collections.<String>emptySet());
  }
  
  @Test
  public void canRetrieveNull ()
  {
    whenILookForANonExistentCobrand("NONE");

    thenNullIsLoaded();
  }
  
  private void whenILookForAnExistingCobrand (final String cobrandName)
  {
    cobrand = dao.findOne("cobrand", cobrandName);
  }

  private void whenILookForTheParentCobrandOf (String existingCobrandName)
  {
    whenILookForAnExistingCobrand(existingCobrandName);
    cobrand = cobrand.getParent();
  }

  private void whenILookForANonExistentCobrand (final String cobrandName)
  {
    whenILookForAnExistingCobrand(cobrandName);
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
