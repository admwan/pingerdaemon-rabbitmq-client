package net.spikesync.pingerdeamonrabbitmqclient;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//import ch.qos.logback.core.joran.spi.JoranException;
//import net.spikesync.pingerdaemonrabbitmqclient.LogbackConfigurator;
import net.spikesync.pingerdaemonrabbitmqclient.SilverCloud;
import net.spikesync.pingerdaemonrabbitmqclient.SilverCloudNode;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:beans.xml")
public class SimpleJunit5Test {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleJunit5Test.class);
	private Properties prop;
	private static String TEST_PROPERTY = "test-pingerdaemon-context";

	@Value("${test-pingerdaemon-context}")
	private String testingEnabled;

	@Autowired
	private SilverCloud sc;

	@BeforeAll
	static void initAll() {
		System.out.println("---Inside SimpleJunit5Test.initAll---");
	}

	@BeforeEach
	void init(TestInfo testInfo) {
		System.out.println("Start SimpleJunit5Test." + testInfo.getDisplayName());
	}

	@Test
	public void messageTest() {
		
//		try {
//			LogbackConfigurator.configure("logback.xml");
//		} catch (JoranException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		logger.info("Now in SimpleJunit5Test.messageTest!!!! LOGGER WORKS @INFO ------------------------------------------");
		System.out.println("Logger name: " + logger.getName());
		System.out.println("Logger enabled for debugging? " + logger.isDebugEnabled());
		System.out.println("Logger enabled for error? " + logger.isErrorEnabled());
		System.out.println("Now in SimpleJunit5Test.messageTest ---  System.out, not the logger!!!!");
		System.out.println("Value of SimpleJunit5Test.testingEnabled: " + testingEnabled);

		
		ArrayList<SilverCloudNode> nodes = sc.getScNodes();
		SilverCloudNode targetNode = null;
		for (SilverCloudNode node : nodes) {
			if (node.getNodeName().equals("SURFIE")) targetNode = node;  
		}
		assertEquals("192.168.50.100", targetNode.getIpAddress());
	}

	
	@AfterEach
	void tearDown(TestInfo testInfo) {
		System.out.println("Finished..." + testInfo.getDisplayName());
	}

	@AfterAll
	static void tearDownAll() {
		System.out.println("---Inside tearDownAll---");
	}
	
	private boolean testingEnabled() {
		logger.debug("Value of this.testingEnabled: " + this.testingEnabled);

		if ((this.testingEnabled != null) && (testingEnabled.compareToIgnoreCase("TRUE") >= 0)) {
			logger.debug("Method testingEnabled is returning true!!");
			return true;
		} else {
			logger.debug("Method testingEnabled is returning false!!");
			return false;
		}
	}

} 