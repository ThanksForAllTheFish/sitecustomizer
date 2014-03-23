package org.mdavi.sitecustomizer.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class CollectionsUtils
{
  
  public static <KEY, VALUE> HashMap<KEY, VALUE> newHashMap ()
  {
    return new HashMap<>();
  }

  public static <KEY, VALUE> TreeMap<KEY, VALUE> newTreeMap ()
  {
    return new TreeMap<>();
  }
  
  public static <TYPE> TreeSet<TYPE> newTreeSet ()
  {
    return new TreeSet<>();
  }
  
  public static <TYPE> ArrayList<TYPE> newArrayList (Collection<TYPE> toWrap)
  {
    return new ArrayList<>(toWrap);
  }
}
