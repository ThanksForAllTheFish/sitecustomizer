package org.mdavi.sitecustomizer.matchers;

import java.util.Collection;
import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.mdavi.sitecustomizer.model.Cobrand;

public class SiteCustomizerMatchers
{

  public static Matcher<Cobrand> equalCobrandWithProperties (final String cobrandName, final Map<String, Collection<String>> properties)
  {
    return new TypeSafeMatcher<Cobrand>()
    {

      @Override
      public void describeTo (final Description description)
      {
      }

      @Override
      protected boolean matchesSafely (final Cobrand cobrand)
      {
        if(!cobrandName.equals(cobrand.getCobrand()))
          return false;
        for(final String property : properties.keySet()) {
          final Collection<String> expectedValue = properties.get(property);
          if(!expectedValue.equals(cobrand.getValuesFor(property)))
            return false;
        }
        return true;
      }
    };
  }
}
