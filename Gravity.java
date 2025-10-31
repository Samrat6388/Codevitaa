import java.io.*;
import java.util.*;

public class Gravity {
    static int kf(int xx, int yy) {
        return xx * 1000 + yy;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null || line.isEmpty()) return;
        int NN = Integer.parseInt(line.trim());

        int[][] ss = new int[NN][4];
        for (int i = 0; i < NN; i++) {
            String[] parts = br.readLine().trim().split(" ");
            for (int j = 0; j < 4; j++) ss[i][j] = Integer.parseInt(parts[j]);
        }

        String[] lastLine = br.readLine().trim().split(" ");
        int sx = Integer.parseInt(lastLine[0]);
        int sy = Integer.parseInt(lastLine[1]);
        long EE = Long.parseLong(lastLine[2]);

        HashMap<Integer, List<Integer>> ad = new HashMap<>();
        HashMap<Integer, Integer> pc = new HashMap<>();
        HashMap<Integer, List<Integer>> xy = new HashMap<>();

        for (int[] sg : ss) {
            int x1 = sg[0], y1 = sg[1], x2 = sg[2], y2 = sg[3];
            int dx = x2 - x1, dy = y2 - y1;
            int LL = Math.max(Math.abs(dx), Math.abs(dy));
            if (LL == 0) continue;

            int sxn = Integer.compare(dx, 0);
            int syn = Integer.compare(dy, 0);
            List<int[]> vv = new ArrayList<>(LL + 1);
            for (int tt = 0; tt <= LL; tt++) {
                int xx = x1 + sxn * tt, yy = y1 + syn * tt;
                vv.add(new int[]{xx, yy});
            }

            for (int[] pp : vv) {
                int kk = kf(pp[0], pp[1]);
                pc.put(kk, pc.getOrDefault(kk, 0) + 1);
                xy.computeIfAbsent(pp[0], k -> new ArrayList<>()).add(pp[1]);
            }

            for (int tt = 0; tt < LL; tt++) {
                int[] a = vv.get(tt);
                int[] b = vv.get(tt + 1);
                int ka = kf(a[0], a[1]), kb = kf(b[0], b[1]);
                ad.computeIfAbsent(ka, k -> new ArrayList<>()).add(kb);
                ad.computeIfAbsent(kb, k -> new ArrayList<>()).add(ka);
            }
        }

        for (Map.Entry<Integer, List<Integer>> kv : xy.entrySet()) {
            List<Integer> vv = kv.getValue();
            Collections.sort(vv);
            vv = new ArrayList<>(new LinkedHashSet<>(vv));
            xy.put(kv.getKey(), vv);
        }

        class Util {
            boolean nextBelow(int xx, int yy, int[] out) {
                List<Integer> vv = xy.get(xx);
                if (vv == null) return false;
                int idx = Collections.binarySearch(vv, yy);
                if (idx < 0) idx = -idx - 1;
                if (idx <= 0) return false;
                out[0] = xx;
                out[1] = vv.get(idx - 1);
                return true;
            }

            boolean isJunction(int xx, int yy) {
                Integer val = pc.get(kf(xx, yy));
                return val != null && val >= 2;
            }

            boolean hasEdge(int x1, int y1, int x2, int y2) {
                List<Integer> lst = ad.get(kf(x1, y1));
                if (lst == null) return false;
                int kk = kf(x2, y2);
                for (int nb : lst) if (nb == kk) return true;
                return false;
            }
        }

        Util util = new Util();
        int cx = sx, cy = sy;

        while (true) {
            if (cy == 0) {
                System.out.print(cx + " " + cy);
                return;
            }

            int[] out = new int[2];
            if (!util.nextBelow(cx, cy, out)) {
                System.out.print(cx + " " + 0);
                return;
            }

            cx = out[0];
            cy = out[1];

            while (true) {
                if (util.isJunction(cx, cy)) {
                    long cost = 1L * cx * cy;
                    if (EE < cost) {
                        System.out.print(cx + " " + cy);
                        return;
                    }
                    EE -= cost;
                }
				int nx = -1, ny = -1, cnt = 0;
                if (util.hasEdge(cx, cy, cx - 1, cy - 1)) {
                    nx = cx - 1;
                    ny = cy - 1;
                    cnt++;
                }
                if (util.hasEdge(cx, cy, cx + 1, cy - 1)) {
                    nx = cx + 1;
                    ny = cy - 1;
                    cnt++;
                }
                if (cnt == 0) break;
                if (EE == 0) {
                    System.out.print(cx + " " + cy);
                    return;
                }
                cx = nx;
                cy = ny;
                EE--;
            }
        }
    }
}