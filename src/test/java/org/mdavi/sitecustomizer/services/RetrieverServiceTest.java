package org.mdavi.sitecustomizer.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.bson.types.ObjectId;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mdavi.sitecustomizer.model.FakeCobrandTest;
import org.mongodb.morphia.dao.DAO;

public class RetrieverServiceTest extends FakeCobrandTest
{
  @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
  
  @SuppressWarnings("unchecked")
  private DAO<Cobrand, ObjectId> dao = context.mock(DAO.class);
  private final Retriever service = new Retriever(dao);
  
  @After
  public void tearDown () {
    context.assertIsSatisfied();
  }

  @Test
  public void canRetrieveASinglePropertyValue () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName"), "cobrandName");
    
    String value = whenLookingForAValue("cobrandName", "property");
    
    assertThat(value, equalTo("value"));
  }

  @Test
  public void canRetrieveNull_forNonExistentCobrand () {
    givenACobrand(null, "nonExistent");
    
    String value = whenLookingForAValue("nonExistent", "property");
    
    assertThat(value, nullValue());
  }
  
  @Test
  public void canRetrieveNull_forNonExistentProperty () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName"), "cobrandName");
    
    String value = whenLookingForAValue("cobrandName", "nonExistent");
    
    assertThat(value, nullValue());
  }

  private void givenACobrand (final Cobrand cobrand, final String cobrandName)
  {
    context.checking(new Expectations()
    {
      {
        oneOf(dao).findOne("cobrand", cobrandName); will(returnValue(cobrand));
      }
    });
  }

  private String whenLookingForAValue (String cobrand, String property)
  {
    return service.getValue(cobrand, property);
  }
}
