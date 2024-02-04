package net.spikesync.pingerdeamonrabbitmqclient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import net.spikesync.pingerdaemonrabbitmqclient.PingEntry.PINGHEAT;
import net.spikesync.pingerdaemonrabbitmqclient.PingHeatMap;
import net.spikesync.pingerdaemonrabbitmqclient.PropertiesLoader;
import net.spikesync.pingerdaemonrabbitmqclient.SilverCloudNode;

import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Properties;

//@SpringJUnitConfig(locations = "classpath:beans.xml")


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:beans.xml")
class PingerdaemonRabbitmqClientApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(PingerdaemonRabbitmqClientApplicationTests.class);

	@Autowired
	private ApplicationContext context;
	private SilverCloudNode sc;

	// The class PropertiesLoader reads the properties file. Depending on the value
	// of the property below testingEnabled will be set.
	// In turn, the method testingEnabled will return boolean.true or boolean.false
	// depending on that value.
	private Properties prop;
	private static String TEST_PROPERTY = "test-pingerdaemon-context";
	@Value("${test-pingerdaemon-context}")
	private static String testingEnabled;

	@BeforeAll // From digitalocean Junit 5 tutorial
	static void beforeAll() {
		logger.debug("**--- Executed once before all test methods in this class ---**");
	}

	@BeforeEach // From digitalocean Junit 5 tutorial
	void beforeEach() {
		logger.debug("**--- Executed before each test method in this class ---**");
	}

	@AfterEach // From digitalocean Junit 5 tutorial
	void afterEach() {
		logger.debug("**--- Executed after each test method in this class ---**");
	}

	@AfterAll // From digitalocean Junit 5 tutorial
	static void afterAll() {
		logger.debug("**--- Executed once after all test methods in this class ---**");
	}

	@BeforeAll
	static void setTestsConfigurations(ExtensionContext ctx) {
		// TestConfiguration.setup(false);

	}

	public PingerdaemonRabbitmqClientApplicationTests() { // Constructor in which the properties files is read.
		prop = PropertiesLoader.loadProperties();
		testingEnabled = prop.getProperty(TEST_PROPERTY);
		logger.debug(
				"*Properties test in PingerdaemonRabbitmqClientApplicationTests* --- Value of test-pingerdaemon-context: "
						+ testingEnabled);
	}
/*
	@Test
	@EnabledIf("testingEnabled")
	void testHeatMap() {
		PingHeatMap pingHeatMap = context.getBean(PingHeatMap.class);
		// Test these two nodes whether a pingHeat value is set properly and then
		// correctly read
		SilverCloudNode rowNode = new SilverCloudNode("CAPTUW", "192.168.50.104");
		SilverCloudNode colNode = new SilverCloudNode("THORFW", "192.168.50.107");
		// Test-value for pingHeat. Put it in first, then read it and compare
		pingHeatMap.setPingHeat(rowNode, colNode, PINGHEAT.TEPID);

		PINGHEAT pingHeat = pingHeatMap.getPingHeat(rowNode, colNode);
		logger.debug("Value of pingHeatMap.getPingHeat(rowNode CAPTUW, colNode THORFW) is: " + pingHeat);
		assertThat(pingHeat).isEqualByComparingTo(PINGHEAT.TEPID);
	}
	
	

	@Test
	//@EnabledIfSystemProperty(named="ordinary.property.test", matches="TRUE")
	void testSpElConditional() {
		logger.debug("Conditionally executed this test because systemProperty('ordinary.property.test').equals('TRUE') ");
	}
	
	@Test
	@EnabledIf("testingEnabled")
	void contextLoads() {
		if (context != null)
			logger.debug("**************** AutowiredCapableBeanFactory: " + context.toString());

		else {
			logger.debug("ApplicationContext variable is null!!");
			return;
		}
		assertThat(context).isNotNull();
		assertThat(context.getBean(net.spikesync.pingerdaemonrabbitmqclient.SilverCloud.class)).isNotNull();

	}
	*/

	@SuppressWarnings("unused") // This method is used by JUnit 5 @EnabledIf to determine whether to execute the
								// test or not.
	private boolean testingEnabled() {
		logger.debug("Value of this.testingEnabled in method testingEnabled(): " + this.testingEnabled);
		if ((this.testingEnabled != null) && (testingEnabled.compareToIgnoreCase("TRUE") >= 0)) {
			logger.debug("Method testingEnabled is returning true!!\n"); logger.debug("Value of this.testingEnabled: " + this.testingEnabled);
			return true;
		} else {
			logger.debug("Method testingEnabled is returning false!!");
			return false;
		}
	}
}
