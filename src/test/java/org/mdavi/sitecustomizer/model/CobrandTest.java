package org.mdavi.sitecustomizer.model;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.hamcrest.Matcher;
import org.junit.Test;

public class CobrandTest extends FakeCobrandTest
{

  private Cobrand            cobrand;
  private Collection<String> value;

  @Test
  public void canRetrieveValues () throws SecurityException, NoSuchFieldException, IllegalArgumentException,
                                  IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName"));

    whenILookForAKey("property");

    thenTheRelativeValuesAreRetrieved(contains("value"));
  }

  @Test
  public void canRetrieveNull () throws SecurityException, NoSuchFieldException, IllegalArgumentException,
                                IllegalAccessException
  {
    givenACobrand(new Cobrand());

    whenILookForANonExistentKey("notExistent");

    thenTheRelativeValuesAreRetrieved(nullValue(String.class));
  }

  private void whenILookForANonExistentKey (String key)
  {
    whenILookForAKey(key);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void thenTheRelativeValuesAreRetrieved (Matcher expectedValue)
  {
    assertThat(this.value, expectedValue);
  }

  private void whenILookForAKey (String key)
  {
    value = cobrand.getValuesFor(key);
  }

  private void givenACobrand (Cobrand fakeCobrand) throws NoSuchFieldException, IllegalAccessException
  {
    cobrand = fakeCobrand;
  }

}
