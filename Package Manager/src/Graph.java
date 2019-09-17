//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           Graph
// Files:           Graph.java
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
import java.util.List;
import java.util.Set;

/**
 * Filename:   Graph.java
 * Project:    p4
 * Authors:    Congkai Tan
 * 
 * Directed and unweighted graph implementation
 */

public class Graph implements GraphADT {
	/**
	 * The list storing all vertices in the graph
	 */
	private List<String> vertices;

	/**
	 * The adjacency matrix
	 */
	private boolean[][] matrix;

	/**
	 * The number of vertices in the graph
	 */
	private int order;

	/**
	 * The number of edges in the graph
	 */
	private int size;

	/**
	 * Default no-argument constructor
	 */ 
	public Graph() {
		vertices = new java.util.ArrayList<String>();
		// the adjacency matrix would be initialized to have a size of 100*100, and every time the matrix
		// is full, it would be resized to a doubled size
		matrix = new boolean[100][100];
		order = size = 0;
	}

	/**
	 * Add new vertex to the graph.
	 *
	 * If vertex is null or already exists,
	 * method ends without adding a vertex or 
	 * throwing an exception.
	 * 
	 * Valid argument conditions:
	 * 1. vertex is non-null
	 * 2. vertex is not already in the graph 
	 */
	public void addVertex(String vertex) {
		// check if the passed in argument is null
		if (vertex == null)
			return;
		// check if there is duplicate vertices in the graph
		for (String temp : vertices) {
			if (temp.equals(vertex))
				return;
		}
		// Resize the adjacency matrix if it is full
		if (vertices.size() == matrix.length)
			matrix = resize(matrix);
		// Store the vertex and increment order
		vertices.add(vertex);
		order++;
	}

	/**
	 * This helper method helps resize the adjacency matrix by doubling its original size
	 * @param matrix - the old matrix to be resized
	 * @return the new resized matrix
	 */
	private boolean[][] resize(boolean[][] matrix) {
		// Double the old size to acquire the size of the new adjacency matrix
		int newSize = 2 * matrix.length;
		// create the new adjacency matrix
		boolean[][] newMatrix = new boolean[newSize][newSize];
		// copy each element in the old matrix to the new matrix
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				newMatrix[i][j] = matrix[i][j];
			}
		}
		return newMatrix;
	}

	/**
	 * Remove a vertex and all associated 
	 * edges from the graph.
	 * 
	 * If vertex is null or does not exist,
	 * method ends without removing a vertex, edges, 
	 * or throwing an exception.
	 * 
	 * Valid argument conditions:
	 * 1. vertex is non-null
	 * 2. vertex is not already in the graph 
	 */
	public void removeVertex(String vertex) {
		// check if the passed in argument is null
		if (vertex == null)
			return;
		// check if the vertex is present in the graph
		if (!vertices.contains(vertex))
			return;
		// remove the row of the vertex to be removed
		for (int i = vertices.indexOf(vertex); i < order - 1; i++) {
			matrix[i] = matrix[i + 1];
		}
		matrix[order] = new boolean[matrix.length];
		// remove the column of the vertex to be removed
		for (int i = 0; i < order - 1; i++) {
			for (int j = vertices.indexOf(vertex); j < order - 1; j++) {
				matrix[i][j] = matrix[i][j + 1];
			}
			matrix[i][order] = false;
		}
		// remove the vertex in the vertices list and decrement order
		vertices.remove(vertex);
		order--;
	}

	/**
	 * Add the edge from vertex1 to vertex2
	 * to this graph.  (edge is directed and unweighted)
	 * If either vertex does not exist,
	 * no edge is added and no exception is thrown.
	 * If the edge exists in the graph,
	 * no edge is added and no exception is thrown.
	 * 
	 * Valid argument conditions:
	 * 1. neither vertex is null
	 * 2. both vertices are in the graph 
	 * 3. the edge is not in the graph
	 */
	public void addEdge(String vertex1, String vertex2) {
		// check if both of the edges are not null
		if (vertex1 == null || vertex2 == null)
			return;
		// check if both edges exist in the graph
		if (!vertices.contains(vertex1) || !vertices.contains(vertex2)) {
			addVertex(vertex1);
			addVertex(vertex2);
		}
		// The index of the vertices in the adjacency matrix would be equal to their indices in the 
		// vertices list
		int index1 = vertices.indexOf(vertex1);
		int index2 = vertices.indexOf(vertex2);
		// check if the edge already exists
		if (!matrix[index1][index2]) {
			// Store the added edge in adjacency matrix and increment size
			matrix[index1][index2] = true;
			size++;
		}
	}

	/**
	 * Remove the edge from vertex1 to vertex2
	 * from this graph.  (edge is directed and unweighted)
	 * If either vertex does not exist,
	 * or if an edge from vertex1 to vertex2 does not exist,
	 * no edge is removed and no exception is thrown.
	 * 
	 * Valid argument conditions:
	 * 1. neither vertex is null
	 * 2. both vertices are in the graph 
	 * 3. the edge from vertex1 to vertex2 is in the graph
	 */
	public void removeEdge(String vertex1, String vertex2) {
		// check if both vertices are not null
		if (!(vertex1 != null && vertex2 != null)) 
			return;
		// check if both vertices are present in the graph
		if (!(vertices.contains(vertex1) && vertices.contains(vertex2)))
			return;
		int index1 = vertices.indexOf(vertex1);
		int index2 = vertices.indexOf(vertex2);
		// remove the edge if it is present
		if (matrix[index1][index2]) {
			matrix[index1][index2] = false;
			size--;
		}
	}	

	/**
	 * Returns a Set that contains all the vertices
	 * 
	 */
	public Set<String> getAllVertices() {
		Set<String> set = new java.util.HashSet<String>();
		for (String temp : vertices) {
			set.add(temp);
		}
		return set;
	}

	/**
	 * Get all the neighbor (adjacent) vertices of a vertex
	 *
	 */
	public List<String> getAdjacentVerticesOf(String vertex) {
		// check if the vertex is in the graph
		if (!vertices.contains(vertex))
			return null;
		List<String> neighbors = new java.util.ArrayList<String>();
		int index = vertices.indexOf(vertex);
		// check in the Adjacency Matrix if two nodes are connected
		for (int i = 0; i < order; i++) {
			if (matrix[index][i]) {
				neighbors.add(vertices.get(i));
			}
		}
		// Sort in alphabetic order
		neighbors.sort(String.CASE_INSENSITIVE_ORDER);
		return neighbors;
	}

	/**
	 * Returns the number of edges in this graph.
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns the number of vertices in this graph.
	 */
	public int order() {
		return order;
	}
}
