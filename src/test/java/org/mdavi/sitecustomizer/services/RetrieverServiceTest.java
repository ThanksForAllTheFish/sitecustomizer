package org.mdavi.sitecustomizer.services;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RetrieverServiceTest
{
  private final Retriever service = new Retriever();

  @Test
  public void canCreateRetrievingService ()
  {
    assertThat(service, not(nullValue()));
  }

}
