package org.mdavi.sitecustomizer;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mdavi.sitecustomizer.MongoSiteCustomizer;

public class MongoSiteCustomizerTest
{
  private MongoSiteCustomizer siteCustomizer = new MongoSiteCustomizer();

  @Test
  public void canRetrieveAStringSingleValue ()
  {
    String value = whenRetrievingExistentPropertyForCobrand("cobrand", "property");

    thenThePropertyValueIsReturned(value, equalTo("value1"));
  }

  @Test
  public void canRetrieveAStringSingleValue_fromASpecificPosition ()
  {
    String value1 = whenRetrievingExistentPropertyForCobrandInPosition("cobrand", "a key", 0);
    String value2 = whenRetrievingExistentPropertyForCobrandInPosition("cobrand", "a key", 1);
  
    thenThePropertyValueIsReturned(value1, equalTo("value1"));
    thenThePropertyValueIsReturned(value2, equalTo("value2"));
  }

  @Test
  public void returnNull ()
  {
    String nullValue = whenRetrievingNonExistentPropertyForCobrandAndPosition("cobrand", "a key", 3);
  
    thenNullIsReturned(nullValue);
  }

  @Test
  public void canRetrieveStringsMultiValue ()
  {
    List<String> value = whenRetrievingAPropertyWithMoreValuesForCobrand("cobrand", "a key");
  
    thenAListOfValueIsRetrived(value);
  }

  @Test
  public void singleValueAndMultiValueAreRelated ()
  {
    List<String> values = whenRetrievingAPropertyWithMoreValuesForCobrand("cobrand", "a key");
    String value = whenRetrievingExistentPropertyForCobrand("cobrand", "a key");
  
    thenValueIsFirstInValues(values, value);
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

  private void thenValueIsFirstInValues (List<String> values, String value)
  {
    thenAListOfValueIsRetrived(values);
    thenThePropertyValueIsReturned(value, equalTo("value1"));
  }

  private void thenThePropertyValueIsReturned (String value, Matcher<String> expectedValue)
  {
    assertThat(value, expectedValue);
  }

  private void thenNullIsReturned (String nullValue)
  {
    thenThePropertyValueIsReturned(nullValue, nullValue(String.class));;
  }

  private void thenAListOfValueIsRetrived (List<String> value)
  {
    assertThat(value, contains("value1", "value2"));
  }

  private String getValueFor (String cobrand, String property, int position)
  {
    return siteCustomizer.getValue(cobrand, property, position);
  }
}
