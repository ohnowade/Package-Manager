//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           PackageManagerTest
// Files:           PackageManagerTest.java
// Course:          CS 400 SP19
//
// Author:          Congkai Tan
// Email:           ctan46@wisc.edu
// Lecturer's Name: Deb Deppeler
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:    Chenlyu Zhao
// Partner Email:   czhao96@wisc.edu
// Partner Lecturer's Name: Deb Deppeler
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//   ___ Write-up states that pair programming is allowed for this assignment.
//   ___ We have both read and understand the course Pair Programming Policy.
//   ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates, 
// strangers, and others do.  If you received no outside help from either type
//  of source, then please explicitly indicate NONE.
//
// Persons:         (identify each person and describe their help in detail)
// Online Sources:  (identify each URL and describe their assistance in detail)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
import static org.junit.jupiter.api.Assertions.*; // org.junit.Assert.*; 

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

import java.io.FileNotFoundException;

/**
 * This class contains test methods for PackageManager class
 */
public class PackageManagerTest {
	PackageManager pm;

	@Before
	public void setUp() throws Exception{
		pm = new PackageManager();
	}

	@After
	public void tearDown() throws Exception{
		pm = null;
	}

	/**
	 * This tests if a FileNotFoundException would be thrown if a file that does not exist is to be read
	 */
	@Test
	public void test01_FileNotFoundException_is_thrown() {
		try {
			pm.constructGraph("abc");
			fail("A FileNotFoundException should be thrown when a file that does not exist is to be read");
		} catch (FileNotFoundException e) {
			// passed
		} catch (Exception e) {
			fail("Unexpected exception");
		}
	}

	/**
	 * This tests if, after reading a json file of a graph, all vertices are present in the graph
	 */
	@Test
	public void test02_check_all_vertices_present() {
		try {
			pm.constructGraph("E:\\CS400\\Package Manager\\src\\topo.json");
			Set<String> vertices = pm.getAllPackages();
			for (int i = 65; i < 74; i++) {
				char temp = (char)i;
				String vertex = "" + temp;
				if (!vertices.contains(vertex))
					fail("The vertex " + vertex + " should be present in the graph");
			}
		} catch (Exception e) {
			fail("Unexpected exception");
		}
	}

	/**
	 * This tests if the installation order of a specific package is as expected
	 */
	@Test
	public void test03_installation_order_of_a_package() {
		try {
			pm.constructGraph("E:\\CS400\\Package Manager\\src\\topo.json");
			// the expected installation order
			List<String> expected = new ArrayList<String>();
			expected.add("E");
			expected.add("G");
			expected.add("I");
			expected.add("A");
			// actual order
			List<String> actualOrder = pm.getInstallationOrder("A");
			// check if two lists are equal
			for (int i =0; i < Math.max(expected.size(), actualOrder.size()); i++) {
				if (!expected.get(i).equals(actualOrder.get(i)))
					fail ("The installation order of vertex A is not as expected");
			}
		} catch (Exception e) {
			fail("Unexpected exception");
		}
	}

	/**
	 * This tests if PackageNotFoudnExceptions is thrown for getInstallationOrder() method
	 */
	@Test
	public void test04_PackageNotFoundException_is_thrown() {
		try {
			pm.constructGraph("E:\\CS400\\Package Manager\\src\\topo.json");
			try {
				pm.getInstallationOrder("Z");
				fail("A PackageNotFoundException should have been thrown");
			} catch (PackageNotFoundException e) {
				// pass
			} catch (Exception e) {
				fail("Unexpected exception");
			}
		} catch (Exception e) {
			fail("Unexpected exception");
		}
	}

	/**
	 * This tests if getPackageWithMaxDependencies() return the correct package
	 */
	@Test
	public void test05_right_package_with_max_dependency() {
		try {
			pm.constructGraph("E:\\CS400\\Package Manager\\src\\topo.json");
			if (!(pm.getPackageWithMaxDependencies().equals("D") || 
					pm.getPackageWithMaxDependencies().equals("H")))
				fail("The right package has not been thrown");
		} catch (Exception e) {
			fail("Unexpected exception");
		}
	}

	/**
	 * This tests if the installation order of all packages are as expected
	 */
	@Test
	public void test06_order_of_all_packages_correct() {
		try {
			pm.constructGraph("E:\\CS400\\Package Manager\\src\\shared_dependencies.json");
			// the expected installation order
			List<String> expected = new ArrayList<String>();
			expected.add("D");
			expected.add("B");
			expected.add("C");
			expected.add("A");
			// actual order
			List<String> actualOrder = pm.getInstallationOrderForAllPackages();
			// check if two lists are equal
			for (int i =0; i < Math.max(expected.size(), actualOrder.size()); i++) {
				if (!expected.get(i).equals(actualOrder.get(i)))
					fail ("The installation order of vertex A is not as expected");
			}
		} catch (Exception e) {
			fail("Unexpected exception: " + e.getClass().getName());
		}
	}
	
	/**
	 * This tests if CycleException would be thrown when trying to get installation 
	 * order of all packages
	 */
	@Test
	public void test07_CycleException_are_thrown() {
		try {
			pm.constructGraph("E:\\CS400\\Package Manager\\src\\cycle.json");
			try {
				pm.getInstallationOrderForAllPackages();
				fail("A CycleException should have been thrown");
			} catch (CycleException e) {
				// pass
			} catch (Exception e) {
				fail("Unexpected Exception: " + e.getClass().getName());
			}
		} catch (Exception e) {
			fail("Unexpected exception: " + e.getClass().getName());
		}
	}

}
