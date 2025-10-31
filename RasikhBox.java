import java.io.*;
import java.util.*;

public class RasikhBox {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int m = Integer.parseInt(st.nextToken());
        int n = Integer.parseInt(st.nextToken());

        int[] c = new int[n];
        for (int i = 0; i < m; ++i) {
            String line = br.readLine().replaceAll("\\s+", ""); // remove spaces
            for (int j = 0; j < n; ++j) {
                if (line.charAt(j) == '*') c[j]++;
            }
        }

        int k = Integer.parseInt(br.readLine().trim());

        for (int t = 0; t < k; ++t) {
            String d = br.readLine().trim();

            int[] f = new int[m + 1];
            for (int v : c) {
                if (v >= 0 && v <= m) f[v]++;
            }

            int[] sfx = new int[m + 2];
            int cur = 0;
            for (int x = m; x >= 0; --x) {
                cur += f[x];
                sfx[x] = cur;
            }

            int nm = n;
            int nn = m;
            int[] nc = new int[nn];

            if (d.equals("right")) {
                for (int col = 0; col < nn; ++col) {
                    nc[col] = sfx[col + 1];
                }
            } else { // direction == "left"
                for (int col = 0; col < nn; ++col) {
                    nc[col] = sfx[m - col];
                }
            }

            m = nm;
            n = nn;
            c = nc;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                char ch = (i >= m - c[j]) ? '*' : '.';
                sb.append(ch);
                if (j + 1 < n) sb.append(' ');
            }
            if (i + 1 < m) sb.append('\n');
        }

        System.out.print(sb.toString());
    }
}
