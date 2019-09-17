//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           GraphTest
// Files:           GraphTest.java
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

/**
 * This class contains test methods for Graph class
 */
public class GraphTest {
	Graph graph;
	
	@Before
	public void setUp() throws Exception{
		graph = new Graph();
	}
	
	@After
	public void tearDown() throws Exception{
		graph = null;
	}
	
	/**
	 * This tests if an empty graph would have an order and a size of zero
	 */
	@Test
	public void test00_order_and_size_are_zero() {
		if (graph.order() != 0 || graph.size() != 0)
			fail("The order and size of an empty graph should be zero");
	}
	
	/**
	 * This tests if the order of the graph would be 200 after 200 vertices are added
	 */
	@Test
	public void test01_order_is_200_after_200_inserted() {
		for (int i = 0; i < 200; i++) {
			graph.addVertex("" + i);
		}
		if (graph.order() != 200)
			fail("The order is not as expected after 200 vertices are added");
	}
	
	/**
	 * Construct a graph and check if the adjacency list of certain vertex is as expected
	 */
	@Test
	public void test02_adjacent_as_expected() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addVertex("E");
		graph.addEdge("A", "B");
		graph.addEdge("A", "D");
		graph.addEdge("A", "C");
		graph.addEdge("B", "C");
		graph.addEdge("D", "E");
		graph.addEdge("E", "C");
		graph.addEdge("E", "A");

		List<String> al = graph.getAdjacentVerticesOf("A");
		if (!al.contains("B") && al.contains("C") && al.contains("D")) {
			fail("the adjacency list returned is not as expected");
		}
	}
	
	/**
	 * This tests if a vertex still exists in the graph after it is removed
	 */
	@Test
	public void test03_remove() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addVertex("E");
		graph.addEdge("C", "D");
		graph.addEdge("A", "B");
		graph.addEdge("A", "D");
		graph.addEdge("A", "C");
		graph.addEdge("B", "C");
		graph.addEdge("D", "E");
		graph.addEdge("E", "C");
		graph.addEdge("E", "A");
		
		graph.removeVertex("C");
		
		List<String> alA = graph.getAdjacentVerticesOf("A");
		List<String> alB = graph.getAdjacentVerticesOf("B");
		List<String> alE = graph.getAdjacentVerticesOf("E");
		// test the existence of the removed vertex through adjacency lists
		if (alA.contains("C") || alB.contains("C") || alE.contains("C")) {
			fail("The vertex was not successfully removed");
		}
		Set<String> vertices = graph.getAllVertices();
		// test the existence of the removed vertex through the list of all packages
		if (vertices.contains("C")) {
			fail("The vertex was not successfully removed");
		}
	}
	
	/**
	 * This tests if the order and size are as expected after constructing a graph, and if they are
	 * as expected after an edge is removed
	 */
	@Test
	public void test04_order_size() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addVertex("E");
		graph.addEdge("A", "B");
		graph.addEdge("A", "D");
		graph.addEdge("A", "C");
		graph.addEdge("B", "C");
		graph.addEdge("D", "E");
		graph.addEdge("E", "C");
		graph.addEdge("E", "A");
		
		if (graph.order() != 5 || graph.size() != 7)
			fail("The graph does not have correct value of order or size");
		
		graph.removeEdge("A", "B");
		if (graph.size() != 6)
			fail("the size is not as expected after an edge is removed");
	}
	
	/**
	 * This tests if an empty graph generates an empty list of all vertices
	 */
	@Test
	public void test05_empty_all_vertices_list() {
		if (graph.getAllVertices().size() != 0)
			fail("the list of all vertices of an empty graph shoudl be empty");
	}

}
