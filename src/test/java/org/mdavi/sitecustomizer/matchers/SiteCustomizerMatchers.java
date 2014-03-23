package org.mdavi.sitecustomizer.matchers;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.mdavi.sitecustomizer.model.Cobrand;

public class SiteCustomizerMatchers
{

  public static Matcher<Cobrand> equalCobrandContainingPropertiesAndDomains (final String cobrandName,
      final Map<String, Collection<String>> expectedProperties, final Set<String> expectedDomains)
  {
    return new TypeSafeMatcher<Cobrand>()
    {
      @Override
      public void describeTo (final Description description)
      {
        description.appendText("<" + cobrandName + " with properties [ " + expectedProperties + " ], with domains " + expectedDomains + ">");
      }

      @Override
      protected boolean matchesSafely (final Cobrand cobrand)
      {
        if (!cobrandName.equals(cobrand.getCobrand()))
        {
          return false;
        }

        for (final String property : expectedProperties.keySet())
        {
          final Collection<String> expectedValue = expectedProperties.get(property);
          if (!expectedValue.equals(cobrand.getValuesFor(property)))
          {
            return false;
          }
        }

        if (!cobrand.getDomains().equals(expectedDomains)) return false;

        return true;
      }
    };
  }

  public static Matcher<Cobrand> looseEqual (final String cobrandName, final int numberOfProperties, final int numberOfDomains)
  {
    return new TypeSafeMatcher<Cobrand>()
    {

      @Override
      public void describeTo (Description description)
      {
        description.appendText("<" + cobrandName + " with " + numberOfProperties + " properties, with " + numberOfDomains + " domains>");
      }

      @Override
      protected boolean matchesSafely (Cobrand cobrand)
      {
        if (!cobrandName.equals(cobrand.getCobrand()))
        {
          return false;
        }
        
        if(numberOfProperties != cobrand.getProperties().size()) return false;
        
        if(numberOfDomains != cobrand.getDomains().size()) return false;
        
        return true;
      }
    };
  }
}
