package org.mdavi.sitecustomizer;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.junit.After;
import org.junit.Test;
import org.mdavi.sitecustomizer.services.Retriever;

public class MongoSiteCustomizerTest extends MockableTest
{
  private static final String VALUE_2 = "value2";
  private static final String VALUE_1 = "value1";
  private static final String PROPERTY_NAME = "property";
  private static final String COBRAND_NAME = "cobrand";
  private Retriever retriever = context.mock(Retriever.class);
  private MongoSiteCustomizer siteCustomizer = new MongoSiteCustomizer(retriever);
  
  @After
  public void teardown() {
    context.assertIsSatisfied();
  }

  @Test
  public void canRetrieveAStringSingleValue ()
  {
    givenACobrandAndAProperty(COBRAND_NAME, PROPERTY_NAME, VALUE_1);
    
    String value = whenRetrievingExistentPropertyForCobrand(COBRAND_NAME, PROPERTY_NAME);

    thenThePropertyValueIsReturned(value, equalTo(VALUE_1));
  }

  @Test
  public void canRetrieveAStringSingleValue_fromASpecificPosition ()
  {
    givenACobrandAndAProperty(COBRAND_NAME, PROPERTY_NAME, VALUE_1, VALUE_2);
    
    String value2 = whenRetrievingExistentPropertyForCobrandInPosition(COBRAND_NAME, PROPERTY_NAME, 1);
  
    thenThePropertyValueIsReturned(value2, equalTo(VALUE_2));
  }

  @Test
  public void canRetrieveStringsMultiValue ()
  {
    givenACobrandAndAProperty(COBRAND_NAME, PROPERTY_NAME, VALUE_1, VALUE_2);
    
    List<String> value = whenRetrievingAPropertyWithMoreValuesForCobrand(COBRAND_NAME, PROPERTY_NAME);
  
    thenAListOfValueIsRetrieved(value);
  }

  @Test
  public void singleValueAndMultiValueAreRelated ()
  {
    givenACobrandAndAProperty(COBRAND_NAME, PROPERTY_NAME, VALUE_1, VALUE_2);
    givenACobrandAndAProperty(COBRAND_NAME, PROPERTY_NAME, VALUE_1, VALUE_2);
    
    List<String> values = whenRetrievingAPropertyWithMoreValuesForCobrand(COBRAND_NAME, PROPERTY_NAME);
    String value = whenRetrievingExistentPropertyForCobrand(COBRAND_NAME, PROPERTY_NAME);
  
    thenValueIsFirstInValues(values, value);
  }
  
  @Test
  public void returnCobrandDomains ()
  {
    givenACobrandWithDomains(COBRAND_NAME);
    
    Set<String> domains = whenRetrievingItsDomains(COBRAND_NAME);
  
    thenTheDomainsAreReturned(domains);
  }

  @Test
  public void returnNull_dueTogNonExistentProperty ()
  {
    givenACobrandAndANonExistentProperty(COBRAND_NAME, "NON_EXISTENT");
    
    String nullValue = whenRetrievingNonExistentPropertyForCobrandAndPosition(COBRAND_NAME, "NON_EXISTENT", 0);
  
    thenNullIsReturned(nullValue);
  }

  @Test
  public void returnNull_dueToLowerBoundPosition ()
  {
    givenACobrandAndAProperty(COBRAND_NAME, PROPERTY_NAME, VALUE_1);
    
    String nullValue = whenRetrievingNonExistentPropertyForCobrandAndPosition(COBRAND_NAME, PROPERTY_NAME, -1);
  
    thenNullIsReturned(nullValue);
  }

  @Test
  public void returnNull_dueToUpperBoundPosition ()
  {
    givenACobrandAndAProperty(COBRAND_NAME, PROPERTY_NAME, VALUE_1);
    
    String nullValue = whenRetrievingNonExistentPropertyForCobrandAndPosition(COBRAND_NAME, PROPERTY_NAME, 2);
  
    thenNullIsReturned(nullValue);
  }

  private void givenACobrandWithDomains (final String cobrandName)
  {
    context.checking(new Expectations()
    {
      {
        oneOf(retriever).getDomains(cobrandName); will(returnValue(Collections.singleton("mdavi.org")));
      }
    });
    
  }

  private void givenACobrandAndANonExistentProperty (final String cobrand, final String property)
  {
    context.checking(new Expectations()
    {
      {
        oneOf(retriever).getProperties(cobrand, property); will(returnValue(null));
      }
    });
  }

  private void givenACobrandAndAProperty (final String cobrand, final String property, final String... propertyValues)
  {
    context.checking(new Expectations()
    {
      {
        oneOf(retriever).getProperties(cobrand, property); will(returnValue(Arrays.asList(propertyValues)));
      }
    });
  }

  private String whenRetrievingExistentPropertyForCobrand (String cobrand, String property)
  {
    return siteCustomizer.getValue(cobrand, property);
  }

  private String whenRetrievingExistentPropertyForCobrandInPosition (String cobrand, String property, int position)
  {
    return getValueFor(cobrand, property, position);
  }

  private String whenRetrievingNonExistentPropertyForCobrandAndPosition (String cobrand, String property, int position)
  {
    return getValueFor(cobrand, property, position);
  }

  private List<String> whenRetrievingAPropertyWithMoreValuesForCobrand (String cobrand, String property)
  {
    return siteCustomizer.getValues(cobrand, property);
  }

  private Set<String> whenRetrievingItsDomains (String cobrandName)
  {
    return siteCustomizer.getDomains(cobrandName);
  }

  private void thenValueIsFirstInValues (List<String> values, String value)
  {
    thenAListOfValueIsRetrieved(values);
    thenThePropertyValueIsReturned(value, equalTo(VALUE_1));
  }

  private void thenThePropertyValueIsReturned (String value, Matcher<String> expectedValue)
  {
    assertThat(value, expectedValue);
  }

  private void thenNullIsReturned (String nullValue)
  {
    thenThePropertyValueIsReturned(nullValue, nullValue(String.class));;
  }

  private void thenAListOfValueIsRetrieved (List<String> value)
  {
    assertThat(value, contains(VALUE_1, VALUE_2));
  }

  private void thenTheDomainsAreReturned (Set<String> domains)
  {
    assertThat(domains, contains("mdavi.org"));
  }

  private String getValueFor (String cobrand, String property, int position)
  {
    return siteCustomizer.getValue(cobrand, property, position);
  }
}
