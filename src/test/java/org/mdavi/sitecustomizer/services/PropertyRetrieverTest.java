package org.mdavi.sitecustomizer.services;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.junit.Test;
import org.mdavi.sitecustomizer.MockableTest;
import org.mdavi.sitecustomizer.database.dao.ICobrandDAO;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.services.implementations.PropertyRetriever;

public class PropertyRetrieverTest extends MockableTest
{
  private final ICobrandDAO cobrandDAO = context.mock(ICobrandDAO.class);
  private final Retriever service = new PropertyRetriever(cobrandDAO);
  
  @Test
  public void canRetrieveASinglePropertyValue () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName", buildFakeProperties("property", Collections.singletonList("value"))), "cobrandName");
    
    final String value = whenLookingForAValue("cobrandName", "property");
    
    thenTheRelativeValuesAreRetrieved(value, equalTo("value"));
  }
  
  @Test
  public void canRetrieveASinglePropertyValue_fromPosition () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName", buildFakeProperties("property", Collections.singletonList("value"))), "cobrandName");
    
    final String value = whenLookingForAValueInPosition("cobrandName", "property", 0);
    
    thenTheRelativeValuesAreRetrieved(value, equalTo("value"));
  }
  
  @Test
  public void canRetrieveAMultiPropertyValue () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName", buildFakeProperties("property", Collections.singletonList("value"))), "cobrandName");
    
    final Collection<String> value = whenLookingForMultiValuesProperty("cobrandName", "property");
    
    thenTheRelativeValuesAreRetrieved(value, contains("value"));
  }

  @Test
  public void canRetrieveNull_forNonExistentCobrand () {
    givenACobrand(null, "nonExistent");
    
    final String value = whenLookingForAValue("nonExistent", "property");
    
    thenTheRelativeValuesAreRetrieved(value, nullValue());
  }
  
  @Test
  public void canRetrieveNull_forNonExistentProperty () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName", buildFakeProperties("property", Collections.singletonList("value"))), "cobrandName");
    
    final String value = whenLookingForAValue("cobrandName", "nonExistent");
    
    thenTheRelativeValuesAreRetrieved(value, nullValue());
  }

  @Test
  public void canRetrieveDomains () throws NoSuchFieldException, IllegalAccessException {
    givenACobrand(prepareFakeCobrand("cobrandName", buildFakeProperties("property", Collections.singletonList("value"))), "cobrandName");
    
    final Set<String> domains = whenLookingForItsDomains("cobrandName");
    
    thenTheRelativeDomainsAreRetrieved(domains, containsInAnyOrder("mdavi.org"));
  }
  
  @Test
  public void canRetrieveEmptyDomains () throws NoSuchFieldException, IllegalAccessException {
    givenACobrand(prepareFakeCobrandWithoutDomain(), "cobrandName");
    
    final Set<String> domains = whenLookingForItsDomains("cobrandName");
    
    thenTheRelativeDomainsAreRetrieved(domains, emptyIterable());
  }
  
  private void givenACobrand (final Cobrand cobrand, final String cobrandName)
  {
    context.checking(new Expectations()
    {
      {
        oneOf(cobrandDAO).findOne("cobrand", cobrandName); will(returnValue(cobrand));
      }
    });
  }

  private String whenLookingForAValue (final String cobrandName, final String property)
  {
    return service.getProperty(cobrandName, property);
  }

  private String whenLookingForAValueInPosition (String cobrandName, String property, int position)
  {
    return service.getProperty(cobrandName, property, position);
  }

  private Collection<String> whenLookingForMultiValuesProperty (String cobrandName, String property)
  {
    return service.getProperties(cobrandName, property);
  }

  private Set<String> whenLookingForItsDomains (String cobrandName)
  {
    return service.getDomains(cobrandName);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void thenTheRelativeDomainsAreRetrieved (Set<String> domains, Matcher expectedDomains)
  {
    assertThat(domains, expectedDomains);
  }

  private Cobrand prepareFakeCobrandWithoutDomain () throws NoSuchFieldException, IllegalAccessException
  {
    Cobrand cobrand = prepareFakeCobrand("cobrandName", buildFakeProperties("property", Collections.singletonList("value")));
    inject(cobrand, "domains", Collections.emptySet());
    return cobrand;
  }
}
