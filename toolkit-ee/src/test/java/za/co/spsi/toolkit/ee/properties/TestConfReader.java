package za.co.spsi.toolkit.ee.properties;

import za.co.spsi.toolkit.ee.properties.FileReader;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

@RunWith(Arquillian.class)
public class TestConfReader extends AbstractTestConfReader {

	@Deployment
	public static JavaArchive createMyDeployment() {
		return AbstractTestConfReader.createDeployment();
	}


		@Test
	public void should_be_deployed() {
		Assert.assertEquals(testBoolean,true);
		Assert.assertEquals(testInteger,new Integer(12));
		Assert.assertEquals(testDouble,new Double(12.33));
		Assert.assertEquals(testString,"Test String");
	}
}
