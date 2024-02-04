package net.spikesync.pingerdeamonrabbitmqclient;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import net.spikesync.pingerdaemonrabbitmqclient.SilverCloudNode;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:beans.xml")
public class JUnit5Sample1Test {

	private static final Logger logger = LoggerFactory.getLogger(JUnit5Sample1Test.class);

	@Autowired
	private ApplicationContext context;
	private SilverCloudNode sc;

  @BeforeAll
  static void beforeAll() {
    logger.info("**--- Executed once before all test methods in this class ---**");
  }

  @BeforeEach
  void beforeEach() {
    System.out.println("**--- Executed before each test method in this class ---**");
  }

  @Test
  void testMethod1() {
    System.out.println("**--- Test method1 executed ---**");
  }

  @DisplayName("Test method2 with condition")
  @Test
  void testMethod2() {
    System.out.println("**--- Test method2 executed ---**");
  }

  @Test
  @Disabled("implementation pending")
  void testMethod3() {
	  System.out.println("**--- Test method3 executed ---**");
  }

  @AfterEach
  void afterEach() {
    System.out.println("**--- Executed after each test method in this class ---**");
  }

  @AfterAll
  static void afterAll() {
    System.out.println("**--- Executed once after all test methods in this class ---**");
  }


}