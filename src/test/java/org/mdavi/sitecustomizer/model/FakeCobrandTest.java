package org.mdavi.sitecustomizer.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class FakeCobrandTest
{
  protected static Cobrand prepareFakeCobrand (String cobrandName) throws NoSuchFieldException, IllegalAccessException
  {
    final Cobrand cobrand = new Cobrand();
    inject(cobrand, "cobrand", cobrandName);
    inject(cobrand, "properties", buildFakeProperties());
    return cobrand;
  }

  private static Collection<Map<String, Collection<String>>> buildFakeProperties ()
  {
    final Collection<Map<String, Collection<String>>> properties = new HashSet<>();
    final Map<String, Collection<String>> property = new HashMap<>();

    Collection<String> values = new ArrayList<>();
    values.add("value");

    property.put("property", values);

    properties.add(property);
    return properties;
  }

  private static void inject (final Object source, final String fieldName, final Object value)
                                                                                              throws NoSuchFieldException,
                                                                                              IllegalAccessException
  {
    final Field field = source.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(source, value);
  }

}
