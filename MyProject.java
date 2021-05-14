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
        // TODO
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

    public int maxDownloadSpeed(int[][] adjlist, int[][] speeds, int src, int dst) {
        // TODO
        return 0;
    }

   
}
