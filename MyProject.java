import java.util.*;
 
/**  
 * @author Nahian Hasan Zaim (22849901)
 * 
 * 
 *  @author Wai Yee Goh (22769803)
 */

public class MyProject implements Project {

    /**
     * Method to find if all devices are strongly connected
     * @param adjlist the only parameter is the graph in the form of an adjacency list
     * @return true each node can be reached from every other node and false otherwise
     */
    
    public boolean allDevicesConnected(int[][] adjlist) {
        int devices = adjlist.length;

        //Use adjMatrix since it's easier to transpose which is needed for later part of the algorithm
        int[][] adjMatrix = new int[devices][devices];
        int[][] transposedMatrix = new int[devices][devices];
        
        //Two for loops which iterate thorugh the adjlist and create two matrices
        for (int i = 0; i < devices; i++){
            for (int j : adjlist[i]){
                adjMatrix[i][j] = 1;
                transposedMatrix[j][i] = 1;
            }
        }

              

        boolean visited[] = new boolean[devices]; //a boolean array to keep track of nodes which have been visitied, the default value is false for boolean array

        depthFirstSearch(0, visited, adjMatrix); //Do a simple DFS starting from vertex zero

        for(boolean b : visited) if(!b) return false; //If any node is unreachable from 0, return false
        
        Arrays.fill(visited, false); //Reset visited array to all false for another DFS

        depthFirstSearch(0, visited, transposedMatrix); //Do another DFS of reversed graph starting from same vertex
        
        for(boolean b : visited) if(!b) return false; //If any node is unreachable from 0, return false
     
        return true; //If all nodes are reachable both the times, then all devices are strongly connected

    }

    /**
     * Method which does a simple depth first search to the input matrix and determines which vertices are reachable from the starting vertix
     * @param vertex the starting vertex
     * @param visited an array of booleans to store which vertices have been visited
     * @param adjMatrix the graph in the form of an adjacency matrix
     */
    private void depthFirstSearch(int vertex, boolean[] visited, int[][] adjMatrix){
        visited[vertex] = true;

        //for all adjacent vertices, if they were not visited before, perform a DFS on it. 
        for (int col = 0; col < adjMatrix.length; col++) {
            if ((adjMatrix[vertex][col] == 1) && (!visited[col])){
                depthFirstSearch(col, visited, adjMatrix);
            }
        }
        
    }

    public int numPaths(int[][] adjlist, int vertex, int dst) {
        
        int numPaths = 0;
        if (vertex == dst) return 1;
  
        boolean visited[] = new boolean[adjlist.length];
        LinkedList<Integer> queue = new LinkedList<Integer>();
  
        visited[vertex] = true;
        queue.add(vertex);
  
        while (queue.size() != 0) {
            int u = queue.poll();
              for (int col = 0; col < adjlist[u].length; col++) {
                  int v = adjlist[u][col];
                        if(v==dst) numPaths++;
                  if(!visited[v]){
                           visited[v] = true;
                      queue.add(v);
                  }
               }
        }     
  
        return numPaths;
    }

    /**
     * Method to detemine how many hops are required for the source vertex to reach the same subnet as the queries.
     * @param adjlist the graph in the form of adjacency list
     * @param addrs the IP address for each device
     * @param vertex the starting vertex
     * @param queries the array of queries
     * @return hops[], an array of integer which corresponds to the number of hops required for each subnet query
     */
    public int[] closestInSubnet(int[][] adjlist, short[][] addrs, int vertex, short[][] queries) {
       
        //creates the hops array
        int[] hops = new int[queries.length];
        
        //a for loop which runs for each query and determines the number of hops
        for (int i = 0; i < queries.length; i++){
            short[] query = queries[i];
            
            //trivially checks if the source is in the subnet of the query, stores zero for the number of hops if true
            if(inSameSubnet(addrs[vertex], query)) {
                hops[i] = 0;
                continue;
            }

            //creates a boolean array, a queue and an array to store the levels of each vertex for the BFS
            boolean visited[] = new boolean[adjlist.length];

            int level[] = new int[adjlist.length];

            LinkedList<Integer> queue = new LinkedList<Integer>();

            visited[vertex] = true;
            level[vertex] = 0;
            queue.add(vertex);

            while (queue.size() != 0) {
                int u = queue.poll();
                
                //for each vertex, checks whether adjacent vertices are visited; store it's level, mark it as visited.
                //Also checks if each adjacent matrix is in the subnet of query. If true, the BFS terminates and the program procedes to the next iteration of the for loop
                for (int col = 0; col < adjlist[u].length; col++) {
                    int v = adjlist[u][col];
                    
                    if(!visited[v]){
                        
                        level[v] = level[u] + 1;
                        
                        if(inSameSubnet(addrs[v], query)){
                            hops[i] = level[v];
                            queue.clear();
                            break;
                        }
                        
                        visited[v] = true;
                        queue.add(v);
                    }
                }
            }
            //If no vertex is found in the same subnet as query, set the no of hops as the maximum integer value
            if(hops[i] == 0) hops[i] = Integer.MAX_VALUE;
        }
        return hops;
    }

    private boolean inSameSubnet(short[] vertex, short[] query) {
        for (int i = 0; i < query.length; i++) {
            if(query[i] != vertex[i]) return false;
        }
        return true;
    }

    
    private boolean bfsDinic(int adjlist[][], int vertex, int dst, int[] dist, int resGraph[][]){ 
		int n = adjlist.length;
		Arrays.fill(dist,-1);
		dist[vertex]=0;
		LinkedList<Integer> queue = new LinkedList<Integer>();
        	queue.add(vertex);

		while (!queue.isEmpty()){
			int u=queue.poll();
			for (int i=0; i<n; i++){
				for (int j=0; j<adjlist[i].length; j++){
					int v=adjlist[i][j];
					if (dist[v]<0 && resGraph[u][v]>0){
						dist[v] = dist[u]+1;
						queue.add(v);
					}
				}
			}
		}
		return dist[dst] >= 0;
	}

    private int flow(int adjlist[][], int ptr[], int dist[], int u, int dst, int f, int resGraph[][]){
        if (u == dst) return f;
        for(; ptr[u]<adjlist[u].length; ptr[u]++){
            int v = adjlist[u][ptr[u]];
            if(dist[v] == dist[u]+1 && resGraph[u][v]>0){
                int path_flow = flow(adjlist, ptr, dist, v, dst, Math.min(f, resGraph[u][v]), resGraph);
                if (path_flow>0){
                    resGraph[u][v]-=path_flow;
                    resGraph[v][u]+=path_flow;
                    
                    return path_flow;
                }
            }
        }
        return 0;
    }



    public int maxDownloadSpeed(int[][] adjlist, int[][] speeds, int vertex, int dst) {
        if (vertex == dst) return -1;
        int max_flow = 0;
        int n = adjlist.length;

        int[] dist = new int[n];
        int[][] resGraph = new int[n][n];
        for (int i=0; i<n; i++){
            Arrays.fill(resGraph[i],0);
            for (int j=0; j<adjlist[i].length; j++){
                int reachDevice = adjlist[i][j];
                resGraph[i][reachDevice] = speeds[i][j];
            }
        }
            
        while (bfsDinic(adjlist, vertex, dst, dist, resGraph)){
            int ptr[] = new int[n];
            while (true){
                int path_flow = flow(adjlist, ptr, dist, vertex, dst, Integer.MAX_VALUE, resGraph);
                if (path_flow==0) break;
                max_flow += path_flow;
            }
        }
        return max_flow;
        }


}
