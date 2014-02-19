package org.mdavi.sitecustomizer;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.mdavi.sitecustomizer.MongoSiteCustomizer;

public class MongoSiteCustomizerTest
{
  private MongoSiteCustomizer siteCustomizer = new MongoSiteCustomizer();

  @Test
  public void canRetrieveAStringSingleValue ()
  {
    String value = siteCustomizer.getValue("cobrand", "a key");

    assertThat(value, equalTo("value1"));
  }
  
  @Test
  public void canRetrieveAStringSingleValue_fromASpecificPosition ()
  {
    String value1 = siteCustomizer.getValue("cobrand", "a key", 0);
    String value2 = siteCustomizer.getValue("cobrand", "a key", 1);

    assertThat(value1, equalTo("value1"));
    assertThat(value2, equalTo("value2"));
  }
  
  @Test
  public void returnNull_fromOutOfBoundPosition ()
  {
    String nullValue = siteCustomizer.getValue("cobrand", "a key", 3);

    assertThat(nullValue, nullValue());
  }

  @Test
  public void canRetrieveStringsMultiValue ()
  {
    List<String> value = siteCustomizer.getValues("cobrand", "a key");

    assertThat(value, contains("value1", "value2"));
  }

  @Test
  public void singleValueAndMultiValueAreRelated ()
  {
    List<String> values = siteCustomizer.getValues("cobrand", "a key");
    String value = siteCustomizer.getValue("cobrand", "a key");

    assertThat(values, contains("value1", "value2"));
    assertThat(value, equalTo("value1"));
  }
}
