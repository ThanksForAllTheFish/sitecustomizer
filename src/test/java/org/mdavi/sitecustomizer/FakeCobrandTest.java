package org.mdavi.sitecustomizer;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matcher;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.model.Domain;

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
    inject(cobrand, "domains", buildFakeDomains("mdavi.org"));
    return cobrand;
  }

  protected static Map<String, Collection<String>> buildFakeProperties (String propertyName, Collection<String> propertyValues)
  {
    return Collections.singletonMap(propertyName, propertyValues);
  }

  protected static Set<String> buildFakeDomains (String address)
  {
    return Collections.singleton(address);
  }

  protected static Domain buildFakeDomain (String address) throws NoSuchFieldException, IllegalAccessException
  {
    Domain domain = new Domain(address, prepareFakeCobrand("cobrand", Collections.<String, Collection<String>>emptyMap()));
    return domain;
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
