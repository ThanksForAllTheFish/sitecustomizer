package org.mdavi.sitecustomizer.model;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collection;

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

    thenTheRelativeValuesAreRetrieved(this.value, contains("value"));
  }

  @Test
  public void canRetrieveNull () throws SecurityException, NoSuchFieldException, IllegalArgumentException,
                                IllegalAccessException
  {
    givenACobrand(new Cobrand());

    whenILookForANonExistentKey("notExistent");

    thenTheRelativeValuesAreRetrieved(this.value, nullValue(String.class));
  }

  private void whenILookForANonExistentKey (final String key)
  {
    whenILookForAKey(key);
  }

  private void whenILookForAKey (final String key)
  {
    value = cobrand.getValuesFor(key);
  }

  private void givenACobrand (final Cobrand fakeCobrand) throws NoSuchFieldException, IllegalAccessException
  {
    cobrand = fakeCobrand;
  }

}
