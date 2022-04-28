package za.co.spsi.toolkit.ee.properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(Arquillian.class)
public class TestAgencyConfReader extends AbstractTestConfReader {


	@Inject
	private PropertiesAgency propertiesAgency;

	@Deployment
	public static JavaArchive createMyDeployment() {
		MockPropertiesAgency.setAgency("2000001");
		return AbstractTestConfReader.createDeployment();
	}

	@Test
	public void should_be_deployed() {
		Assert.assertEquals(testBoolean,false);
		Assert.assertEquals(testInteger,new Integer(13));
		Assert.assertEquals(testDouble,new Double(13.33));
		Assert.assertEquals(testString,"Test String");
	}
}
