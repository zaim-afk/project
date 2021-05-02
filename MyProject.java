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
        // TODO
        return 0;
    }

    public int[] closestInSubnet(int[][] adjlist, short[][] addrs, int src, short[][] queries) {
        // TODO
        return null;
    }

    public int maxDownloadSpeed(int[][] adjlist, int[][] speeds, int src, int dst) {
        // TODO
        return 0;
    }
}
