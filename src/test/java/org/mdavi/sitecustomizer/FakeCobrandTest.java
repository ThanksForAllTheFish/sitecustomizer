package org.mdavi.sitecustomizer;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matcher;
import org.mdavi.sitecustomizer.model.Cobrand;

public abstract class FakeCobrandTest
{
  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void thenTheRelativeValuesAreRetrieved (final Object value, final Matcher expectedValue)
  {
    assertThat(value, expectedValue);
  }

  protected static Cobrand prepareFakeCobrand (final String cobrandName, Map<String, Collection<String>> properties)
                                                                                                  throws NoSuchFieldException,
                                                                                                  IllegalAccessException
  {
    final Cobrand cobrand = new Cobrand();
    inject(cobrand, "cobrand", cobrandName);
    inject(cobrand, "properties", properties);
    inject(cobrand, "domains", buildFakeDomains());
    return cobrand;
  }

  protected static Map<String, Collection<String>> buildFakeProperties (String propertyName, Collection<String> propertyValues) throws NoSuchFieldException,
                                                                       IllegalAccessException
  {
    return Collections.singletonMap(propertyName, propertyValues);
  }

  private static Set<String> buildFakeDomains ()
  {
    return Collections.singleton("mdavi.org");
  }

  protected static void inject (final Object source, final String fieldName, final Object value)
                                                                                              throws NoSuchFieldException,
                                                                                              IllegalAccessException
  {
    final Field field = source.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(source, value);
  }

}
