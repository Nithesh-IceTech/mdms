package za.co.spsi.toolkit.ee.properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

public abstract class AbstractTestConfReader {

	@Inject
	@ConfValue("test.boolean")
	Boolean testBoolean;

	@Inject
	@ConfValue("test.integer")
	Integer testInteger;

	@Inject
	@ConfValue("test.double")
	Double testDouble;

	@Inject
	@ConfValue("test.string")
	String testString;

	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class).addClass(FileReader.class)
				.addPackages(true,ConfValue.class.getPackage())
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void should_be_deployed() {
		Assert.assertEquals(testBoolean,true);
		Assert.assertEquals(testInteger,new Integer(12));
		Assert.assertEquals(testDouble,new Double(12.33));
		Assert.assertEquals(testString,"Test String");
	}
}
