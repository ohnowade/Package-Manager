import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Filename:   PackageManager.java
 * Project:    p4
 * Authors:    Congkai Tan
 * 
 * PackageManager is used to process json package dependency files
 * and provide function that make that information available to other users.
 * 
 * Each package that depends upon other packages has its own
 * entry in the json file.  
 * 
 * Package dependencies are important when building software, 
 * as you must install packages in an order such that each package 
 * is installed after all of the packages that it depends on 
 * have been installed.
 * 
 * For example: package A depends upon package B,
 * then package B must be installed before package A.
 * 
 * This program will read package information and 
 * provide information about the packages that must be 
 * installed before any given package can be installed.
 * all of the packages in
 * 
 * You may add a main method, but we will test all methods with
 * our own Test classes.
 */

public class PackageManager {

	private Graph graph;

	/*
	 * Package Manager default no-argument constructor.
	 */
	public PackageManager() {
		graph = new Graph();
	}

	/**
	 * Takes in a file path for a json file and builds the
	 * package dependency graph from it. 
	 * 
	 * @param jsonFilepath the name of json data file with package dependency information
	 * @throws FileNotFoundException if file path is incorrect
	 * @throws IOException if the give file cannot be read
	 * @throws ParseException if the given json cannot be parsed 
	 */
	public void constructGraph(String jsonFilepath) throws FileNotFoundException, IOException, ParseException {
		// parse the json file at the path and retrieve the array of all packages
		Object obj = new JSONParser().parse(new FileReader(jsonFilepath));
		JSONObject jo = (JSONObject) obj;
		JSONArray packages = (JSONArray) jo.get("packages");

		// Retrieve the name and dependencies of every package 
		for (int i = 0; i < packages.size(); i++) {
			JSONObject eachPackage = (JSONObject) packages.get(i);
			// Store this package in the graph
			String name = (String) eachPackage.get("name");
			graph.addVertex(name);
			// retrieve every package in its dependencies list and store it and the 
			// edge connecting it and current package in the graph
			JSONArray dependencies = (JSONArray) eachPackage.get("dependencies");
			for (int j = 0; j < dependencies.size(); j++) {
				String dependency = (String)dependencies.get(j);
				graph.addVertex(dependency);
				graph.addEdge(name, dependency);
			}
		}
	}

	/**
	 * Helper method to get all packages in the graph.
	 * 
	 * @return Set<String> of all the packages
	 */
	public Set<String> getAllPackages() {
		return graph.getAllVertices();
	}

	/**
	 * Given a package name, returns a list of packages in a
	 * valid installation order.  
	 * 
	 * Valid installation order means that each package is listed 
	 * before any packages that depend upon that package.
	 * 
	 * @return List<String>, order in which the packages have to be installed
	 * 
	 * @throws CycleException if you encounter a cycle in the graph while finding
	 * the installation order for a particular package. Tip: Cycles in some other
	 * part of the graph that do not affect the installation order for the 
	 * specified package, should not throw this exception.
	 * 
	 * @throws PackageNotFoundException if the package passed does not exist in the 
	 * dependency graph.
	 */
	public List<String> getInstallationOrder(String pkg) throws CycleException, PackageNotFoundException {
		// check if the package exists in the graph
		if (!getAllPackages().contains(pkg))
			throw new PackageNotFoundException();
		// the helper stack for depth-first search
		Stack<String> stack = new Stack<String>();
		// the helper list indicating visited vertices
		List<String> visited = new ArrayList<String>();
		// the list storing the installation order
		List <String> order = new ArrayList<String>();

		stack.push(pkg);
		visited.add(pkg);

		outerloop:
			while (!stack.isEmpty()) {
				String current = stack.peek();
				List<String> successors= graph.getAdjacentVerticesOf(current);
				// look for an unvisited successor of current vertex
				for (int i = 0; i < successors.size(); i++) {
					String temp = successors.get(i);
					if (!visited.contains(temp)) {
						stack.push(temp);
						visited.add(temp);
						continue outerloop;
					}
					// If the vertex has been visited and it is in the stack, it means that there is a cycle
					else if (stack.contains(temp)) {
						throw new CycleException();
					}
				}
				// if all successors are visited, the vertex would be popped out of the stack and stored
				order.add(stack.pop());
			}
		return order;
	}


	/**
	 * Given two packages - one to be installed and the other installed, 
	 * return a List of the packages that need to be newly installed. 
	 * 
	 * For example, refer to shared_dependecies.json - toInstall("A","B") 
	 * If package A needs to be installed and packageB is already installed, 
	 * return the list ["A", "C"] since D will have been installed when 
	 * B was previously installed.
	 * 
	 * @return List<String>, packages that need to be newly installed.
	 * 
	 * @throws CycleException if you encounter a cycle in the graph while finding
	 * the dependencies of the given packages. If there is a cycle in some other
	 * part of the graph that doesn't affect the parsing of these dependencies, 
	 * cycle exception should not be thrown.
	 * 
	 * @throws PackageNotFoundException if any of the packages passed 
	 * do not exist in the dependency graph.
	 */
	public List<String> toInstall(String newPkg, String installedPkg) throws CycleException, PackageNotFoundException {
		// check if the package exists in the graph
		if (!(getAllPackages().contains(newPkg) && getAllPackages().contains(installedPkg)))
			throw new PackageNotFoundException();
		// the helper stack for depth-first search
		Stack<String> stack = new Stack<String>();
		// the helper list indicating visited vertices
		List<String> visited = new ArrayList<String>();
		// the list storing the installation order
		List <String> toInstall = new ArrayList<String>();

		visited.add(installedPkg);
		stack.push(installedPkg);

		// Put all dependencies of installed package into the list of visited vertices
		while (!stack.isEmpty()) {
			String current = stack.pop();
			List<String> successors= graph.getAdjacentVerticesOf(current);
			// check all the unvisited successors
			for (int i = 0; i < successors.size(); i++) {
				if (!visited.contains(successors.get(i))) {
					stack.push(successors.get(i));
					visited.add(successors.get(i));
				} 
			}
		}

		// the remaining dependencies that are not in the visited list would be packages that need to 
		// be installed, and the order would be acquire through DFS
		DFS(newPkg, visited, stack, toInstall);

		return toInstall;
	}

	/**
	 * Return a valid global installation order of all the packages in the 
	 * dependency graph.
	 * 
	 * assumes: no package has been installed and you are required to install 
	 * all the packages
	 * 
	 * returns a valid installation order that will not violate any dependencies
	 * 
	 * @return List<String>, order in which all the packages have to be installed
	 * @throws CycleException if you encounter a cycle in the graph
	 */
	public List<String> getInstallationOrderForAllPackages() throws CycleException {
		// the helper stack for depth-first search
		Stack<String> stack = new Stack<String>();
		// the helper list indicating visited vertices
		List<String> visited = new ArrayList<String>();
		// the list storing the installation order
		List <String> order = new ArrayList<String>();
		// the list storing all vertices
		List<String> vertices = new ArrayList<String>(getAllPackages());
		vertices.sort(String.CASE_INSENSITIVE_ORDER);

		// DFS to detect cycles
		for (String temp : vertices) {
			Stack<String> DFSStack = new Stack<String>();
			List<String> DFSVisited = new ArrayList<String>();
			List<String> DFSOrder = new ArrayList<String>();
			DFS(temp, DFSVisited, DFSStack, DFSOrder);
		}

		// find all vertices with no predecessors to begin Topological Ordering
		List<List<String>> allAdjacency = new ArrayList<List<String>>();
		//store the adjacency list of every vertex in a list
		for (int i = 0; i < vertices.size(); i++) {
			allAdjacency.add(graph.getAdjacentVerticesOf(vertices.get(i)));
		}
		outerloop:
			for (int i = 0; i < vertices.size(); i++) {
				String vertex = vertices.get(i);
				// for every vertex in the graph, check all adjacency lists and see if it is present; and if
				// it is not in any of the lists, this vertex does not have predecessors
				for (int j = 0; j < allAdjacency.size(); j++) {
					if (allAdjacency.get(j).contains(vertex)) {
						continue outerloop;
					}
				}
				stack.push(vertex);
				visited.add(vertex);
			}

		// Topological Ordering begins
		outerloop:
			while (!stack.isEmpty()) {
				List<String> successors= graph.getAdjacentVerticesOf(stack.peek());
				// look for an unvisited successor of current vertex
				for (int i = 0; i < successors.size(); i++) {
					String temp = successors.get(i);
					if (!visited.contains(temp)) {
						stack.push(temp);
						visited.add(temp);
						continue outerloop;
					}
				}
				// if all successors are visited, the vertex would be popped out of the stack and stored
				order.add(stack.pop());
			}
			return order;
	}

	/**
	 * This helper method uses depth-first search to acquire traversal order and detect cycle in the graph
	 * @param vertex - current vertex
	 * @param visited - the list of visited vertex
	 * @param stack - the stack to help depth-first traversal
	 * @param order - the list containing traversal order
	 */
	private void DFS(String vertex, List<String> visited, Stack<String> stack, List<String> order) throws CycleException {
		// vertices in the stack are vertices in current path that have been visited, so if the 
		// current vertex, as a successor of its predecessor, is already in the path, then there
		// is a cycle
		if (stack.contains(vertex))
			throw new CycleException();
		if (!visited.contains(vertex)) {
			// If current vertex has not been visited, it would be the next vertex in the traversal of 
			// current path, so it should be marked as visited and pushed to the stack
			stack.push(vertex);
			visited.add(vertex);
			order.add(vertex);
		} else {
			// if current vertex has been visited but not in the stack, it has to be in a path that has 
			// already been traversed, so it would not cause a cycle and represent the end of traversal
			// of current path
			return;
		}

		// Retrieve all the successors of current vertex and check if any of them would cause a cycle
		List<String> successors= graph.getAdjacentVerticesOf(stack.peek());
		for (int i = 0; i < successors.size(); i++) {
			DFS(successors.get(i), visited, stack, order);
		}

		// After all its successors in current path have been traversed, back trace should begin by 
		// removing current vertex out of stack, and no cycle would be caused by current vertex
		stack.pop();
	}

	/**
	 * Find and return the name of the package with the maximum number of dependencies.
	 * 
	 * Tip: it's not just the number of dependencies given in the json file.  
	 * The number of dependencies includes the dependencies of its dependencies.  
	 * But, if a package is listed in multiple places, it is only counted once.
	 * 
	 * Example: if A depends on B and C, and B depends on C, and C depends on D.  
	 * Then,  A has 3 dependencies - B,C and D.
	 * 
	 * @return String, name of the package with most dependencies.
	 * @throws CycleException if you encounter a cycle in the graph
	 */
	public String getPackageWithMaxDependencies() throws CycleException {
		List<String> allPackages = new ArrayList<String>(getAllPackages());
		String max = "";
		int val = -1;
		// for every vertex, gets its installation order, and the size of its order
		// would be its number of dependencies
		for (int i = 0; i < allPackages.size(); i++) {
			try {
				String current = allPackages.get(i);
				int tempVal = getInstallationOrder(current).size();
				// compare the size to the currently largest size, update it it current size is larger
				if (tempVal > val) {
					val = tempVal;
					max = current;
				}
			} catch (PackageNotFoundException e) {
				e.printStackTrace();
			}
		}
		return max;
	}

	public static void main (String [] args) {
		System.out.println("PackageManager.main()");
	}

}
