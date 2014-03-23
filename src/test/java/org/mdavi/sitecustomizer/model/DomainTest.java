package org.mdavi.sitecustomizer.model;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mdavi.sitecustomizer.FakeCobrandTest;

public class DomainTest extends FakeCobrandTest
{

  @Test
  public void printIsHumanReadable () throws NoSuchFieldException, IllegalAccessException
  {
    Domain domain = buildFakeDomain("mdavi.org");
    
    assertThat(domain.toString(), equalTo("mdavi.org for institutional cobrand cobrand with properties [ {} ], with domains [mdavi.org]"));
  }

}
