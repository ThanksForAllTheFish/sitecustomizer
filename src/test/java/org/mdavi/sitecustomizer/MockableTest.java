package org.mdavi.sitecustomizer;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.mdavi.sitecustomizer.model.FakeCobrandTest;

public abstract class MockableTest extends FakeCobrandTest
{
  @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
}
