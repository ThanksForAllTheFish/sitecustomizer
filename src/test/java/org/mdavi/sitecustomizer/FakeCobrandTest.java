package org.mdavi.sitecustomizer;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.hamcrest.Matcher;
import org.mdavi.sitecustomizer.model.Cobrand;

public abstract class FakeCobrandTest
{
  protected static Cobrand prepareFakeCobrand (final String cobrandName) throws NoSuchFieldException, IllegalAccessException
  {
    final Cobrand cobrand = new Cobrand();
    inject(cobrand, "cobrand", cobrandName);
    inject(cobrand, "properties", buildFakeProperties());
    return cobrand;
  }

  private static Map<String, Collection<String>> buildFakeProperties () throws NoSuchFieldException, IllegalAccessException
  {
    return Collections.singletonMap("property", (Collection<String>) Collections.singletonList("value"));
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
