package org.mdavi.sitecustomizer.model;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Test;
import org.mdavi.sitecustomizer.FakeCobrandTest;

public class CobrandTest extends FakeCobrandTest
{

  private Cobrand            cobrand;
  private Collection<String> value;
  
  @Test
  public void canRetrieveValues () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName", true));

    whenILookForAKey("property");

    thenTheRelativeValuesAreRetrieved(this.value, contains("value"));
  }

  @Test
  public void canRetrieveNull ()
  {
    givenACobrand(new Cobrand());

    whenILookForANonExistentKey("notExistent");

    thenTheRelativeValuesAreRetrieved(this.value, nullValue(String.class));
  }

  @Test
  public void printIsHumanReadable () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName", true));
    
    final String cobrand = whenIPrintIt();
  
    thenTheStringIsHumanReadable(cobrand);
  }

  private void givenACobrand (final Cobrand fakeCobrand)
  {
    cobrand = fakeCobrand;
  }

  private void whenILookForANonExistentKey (final String key)
  {
    whenILookForAKey(key);
  }

  private void whenILookForAKey (final String key)
  {
    value = cobrand.getValuesFor(key);
  }

  private String whenIPrintIt ()
  {
    return cobrand.toString();
  }

  private void thenTheStringIsHumanReadable (final String cobrand)
  {
    assertThat(cobrand, equalTo("cobrandName with properties [ {property=[value]} ], with domains [mdavi.org]"));
  }

}
