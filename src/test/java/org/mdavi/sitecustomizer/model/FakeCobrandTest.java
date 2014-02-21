package org.mdavi.sitecustomizer.model;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matcher;

public abstract class FakeCobrandTest
{
  protected static Cobrand prepareFakeCobrand (final String cobrandName) throws NoSuchFieldException, IllegalAccessException
  {
    final Cobrand cobrand = new Cobrand();
    inject(cobrand, "cobrand", cobrandName);
    inject(cobrand, "properties", buildFakeProperties());
    return cobrand;
  }

  protected static Map<String, Collection<String>> buildFakeProperties () throws NoSuchFieldException, IllegalAccessException
  {
    final Map<String, Collection<String>> props = new HashMap<>();
    final Collection<String> values = new ArrayList<>();
    values.add("value");
    props.put("property", values);
    
    return props;
  }

  private static void inject (final Object source, final String fieldName, final Object value)
                                                                                              throws NoSuchFieldException,
                                                                                              IllegalAccessException
  {
    final Field field = source.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(source, value);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void thenTheRelativeValuesAreRetrieved (final Object value, final Matcher expectedValue)
  {
    assertThat(value, expectedValue);
  }

}
