package org.mdavi.sitecustomizer.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mdavi.sitecustomizer.model.Cobrand;

public class CobrandTest
{

  private Cobrand cobrand;
  private String value;

  @Test
  public void canRetrieveAValue () throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
  {
    givenACobrand();
    
    whenILookForAKey("aKey");
    
    thenTheRelativeValueIsRetrieved(equalTo("aValue"));
  }
  
  @Test
  public void canRetrieveNull () throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
  {
    givenACobrand();
    
    whenILookForANonExistentKey("notExistent");
    
    thenTheRelativeValueIsRetrieved(nullValue(String.class));
  }

  private void whenILookForANonExistentKey (String key)
  {
    whenILookForAKey(key);
  }

  private void thenTheRelativeValueIsRetrieved (Matcher<String> expectedValue)
  {
    assertThat(this.value, expectedValue);
  }

  private void whenILookForAKey (String key)
  {
    value = cobrand.getValueFor(key);
  }

  private void givenACobrand () throws NoSuchFieldException, IllegalAccessException
  {
    cobrand = prepareFakeCobrand();
  }

  private Cobrand prepareFakeCobrand () throws NoSuchFieldException, IllegalAccessException
  {
    final Cobrand cobrand = new Cobrand();    
    inject(cobrand, "keys", buildFakeKeys());
    return cobrand;
  }

  private static ArrayList<Map<String, String>> buildFakeKeys ()
  {
    final ArrayList<Map<String, String>> keys = new ArrayList<>();
    final Map<String, String> values = new HashMap<>();
    
    values.put("aKey", "aValue");
        
    keys.add(values);
    return keys;
  }

  private static void inject (final Object source, final String fieldName, final Object value) throws NoSuchFieldException, IllegalAccessException
  {
    final Field keysField = source.getClass().getDeclaredField(fieldName);
    keysField.setAccessible(true);
    keysField.set(source, value);
  }

}
