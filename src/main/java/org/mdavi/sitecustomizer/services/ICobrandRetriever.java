package org.mdavi.sitecustomizer.services;

import org.mdavi.sitecustomizer.model.Cobrand;

public interface ICobrandRetriever
{

  Cobrand findInstitutionalCobrand (String address);

}
