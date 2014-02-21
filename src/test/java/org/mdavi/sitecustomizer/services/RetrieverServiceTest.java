package org.mdavi.sitecustomizer.services;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collection;

import org.bson.types.ObjectId;
import org.jmock.Expectations;
import org.junit.After;
import org.junit.Test;
import org.mdavi.sitecustomizer.MockableTest;
import org.mdavi.sitecustomizer.model.Cobrand;
import org.mongodb.morphia.dao.DAO;

public class RetrieverServiceTest extends MockableTest
{
  @SuppressWarnings("unchecked")
  private final DAO<Cobrand, ObjectId> dao = context.mock(DAO.class);
  private final Retriever service = new PropertyRetriever(dao);
  
  @After
  public void tearDown () {
    context.assertIsSatisfied();
  }

  @Test
  public void canRetrieveASinglePropertyValue () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName"), "cobrandName");
    
    final String value = whenLookingForAValue("cobrandName", "property");
    
    thenTheRelativeValuesAreRetrieved(value, equalTo("value"));
  }
  
  @Test
  public void canRetrieveASinglePropertyValue_fromPosition () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName"), "cobrandName");
    
    final String value = whenLookingForAValueInPosition("cobrandName", "property", 0);
    
    thenTheRelativeValuesAreRetrieved(value, equalTo("value"));
  }
  
  @Test
  public void canRetrieveAMultiPropertyValue () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName"), "cobrandName");
    
    final Collection<String> value = whenLookingForAValues("cobrandName", "property");
    
    thenTheRelativeValuesAreRetrieved(value, contains("value"));
  }

  private Collection<String> whenLookingForAValues (String cobrandName, String property)
  {
    return service.getProperties(cobrandName, property);
  }

  private String whenLookingForAValueInPosition (String cobrandName, String property, int position)
  {
    return service.getProperty(cobrandName, property, position);
  }

  @Test
  public void canRetrieveNull_forNonExistentCobrand () {
    givenACobrand(null, "nonExistent");
    
    final String value = whenLookingForAValue("nonExistent", "property");
    
    thenTheRelativeValuesAreRetrieved(value, nullValue());
  }
  
  @Test
  public void canRetrieveNull_forNonExistentProperty () throws NoSuchFieldException, IllegalAccessException
  {
    givenACobrand(prepareFakeCobrand("cobrandName"), "cobrandName");
    
    final String value = whenLookingForAValue("cobrandName", "nonExistent");
    
    thenTheRelativeValuesAreRetrieved(value, nullValue());
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

  private String whenLookingForAValue (final String cobrand, final String property)
  {
    return service.getProperty(cobrand, property);
  }
}
