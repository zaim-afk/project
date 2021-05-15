import java.io.*;
import java.util.*;
 
// Nahian Hasan Zaim (22849901)
// Wai Yee Goh (22769803)

public class MyProject implements Project {

    
    public boolean allDevicesConnected(int[][] adjlist) {
        // TODO

        boolean visited[] = new boolean[adjlist.length];

        LinkedList<Integer> queue = new LinkedList<Integer>();

        visited[0] = true;
        queue.add(0);

        while (queue.size() != 0) {
            int u = queue.poll();

            for (int col = 0; col < adjlist[u].length; col++) {
                int v = adjlist[u][col];
                
                if(!visited[v]){
                    visited[v] = true;
                    queue.add(v);
                }
            }
        }

        for(boolean b : visited) if(!b) return false;
            return true;

    }

    public int numPaths(int[][] adjlist, int src, int dst) {
        
        int numPaths = 0;
        if (src == dst) return 1;
  
        boolean visited[] = new boolean[adjlist.length];
        LinkedList<Integer> queue = new LinkedList<Integer>();
  
        visited[src] = true;
        queue.add(src);
  
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


    public int[] closestInSubnet(int[][] adjlist, short[][] addrs, int src, short[][] queries) {
       
        int[] hops = new int[queries.length];
        for (int i = 0; i < queries.length; i++){
            short[] query = queries[i];
            
            if(inSameSubnet(addrs[src], query)) {
                hops[i] = 0;
                continue;
            }

            boolean visited[] = new boolean[adjlist.length];

            int level[] = new int[adjlist.length];

            LinkedList<Integer> queue = new LinkedList<Integer>();

            visited[src] = true;
            level[src] = 0;
            queue.add(src);

            while (queue.size() != 0) {
                int u = queue.poll();
                    
                for (int col = 0; col < adjlist[u].length; col++) {
                    int v = adjlist[u][col];
                    
                    if(!visited[v]){
                        
                        level[v] = level[u] + 1;
                        visited[v] = true;
                        queue.add(v);

                        if(inSameSubnet(addrs[v], query)){
                            hops[i] = level[v];
                            queue.clear();
                            break;
                        }
                    }
                }
            }
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

    
    private boolean bfsDinic(int adjlist[][], int src, int dst, int[] dist, int resGraph[][]){ 
		int n = adjlist.length;
		Arrays.fill(dist,-1);
		dist[src]=0;
		LinkedList<Integer> queue = new LinkedList<Integer>();
        	queue.add(src);

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



    public int maxDownloadSpeed(int[][] adjlist, int[][] speeds, int src, int dst) {
        if (src == dst) return -1;
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
            
        while (bfsDinic(adjlist, src, dst, dist, resGraph)){
            int ptr[] = new int[n];
            while (true){
                int path_flow = flow(adjlist, ptr, dist, src, dst, Integer.MAX_VALUE, resGraph);
                if (path_flow==0) break;
                max_flow += path_flow;
            }
        }
        return max_flow;
        }


}
