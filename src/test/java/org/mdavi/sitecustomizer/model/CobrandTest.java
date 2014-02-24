package org.mdavi.sitecustomizer.model;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.mdavi.sitecustomizer.FakeCobrandTest;

public class CobrandTest extends FakeCobrandTest
{

  private Cobrand            cobrand;
  private Collection<String> value;
  
  @Test
  public void canRetrieveValues () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName", buildFakeProperties("property", (Collection<String>) Collections.singletonList("value"))));

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
  public void canRetrieveFromParent () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareCobrandWithParent());

    whenILookForAKey("parentProperty");

    thenTheRelativeValuesAreRetrieved(this.value, contains("value"));
  }

  @Test
  public void printIsHumanReadable_withoutParent () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName", buildFakeProperties("property", Collections.singletonList("value"))));
    
    final String cobrand = whenIPrintIt();
  
    thenTheStringIsHumanReadable(cobrand, "cobrandName with properties [ {property=[value]} ], with domains [mdavi.org]");
  }
  
  @Test
  public void printIsHumanReadable_withParent () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareCobrandWithParent());
    
    final String cobrand = whenIPrintIt();
  
    thenTheStringIsHumanReadable(cobrand, "son with properties [ {property=[value]} ], with domains [mdavi.org], with parent parent");
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

  private void thenTheStringIsHumanReadable (final String cobrand, String expectedToString)
  {
    assertThat(cobrand, equalTo(expectedToString));
  }

  private Cobrand prepareCobrandWithParent () throws NoSuchFieldException, IllegalAccessException
  {
    Cobrand cobrand = prepareFakeCobrand("son", buildFakeProperties("property", Collections.singletonList("value")));
    inject(cobrand, "parent", prepareFakeCobrand("parent", buildFakeProperties("parentProperty", Collections.singletonList("value"))));
    return cobrand;
  }

}
