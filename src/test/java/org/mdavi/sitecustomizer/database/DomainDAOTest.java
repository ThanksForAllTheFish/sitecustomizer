package org.mdavi.sitecustomizer.database;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mdavi.sitecustomizer.matchers.SiteCustomizerMatchers.equalCobrandContainingPropertiesAndDomains;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.mdavi.sitecustomizer.model.Domain;

public class DomainDAOTest extends MongoConfigurator
{

  @Test
  public void canRetrieveADomain ()
  {
    Domain domain = domainDAO.findOne("address", "mdavi.org");
    
    assertThat(domain.getAddress(), equalTo("mdavi.org"));
    assertThat(domain.getInstitutional(), equalCobrandContainingPropertiesAndDomains("NAME", Collections.<String, Collection<String>>emptyMap(), buildFakeDomains("mdavi.org")));
  }

}
