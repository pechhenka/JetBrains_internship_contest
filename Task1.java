import java.util.Scanner;

public class Task1 {

    private final static int sizeAlphabet = 'z' - 'a' + 1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        Graph g = new Graph(sizeAlphabet);
        String last = sc.next();
        for (int i = 1; i < n; i++) {
            String cur = sc.next();

            int minSize = Math.min(last.length(), cur.length());
            boolean edgeAdded = false;
            for (int j = 0; j < minSize; j++) {
                char c1 = last.charAt(j);
                char c2 = cur.charAt(j);
                if (c1 != c2) {
                    g.addEdge(c2 - 'a', c1 - 'a');
                    edgeAdded = true;
                }
            }

            if (!edgeAdded) {
                if (last.length() > cur.length()) {
                    System.out.println("Impossible");
                    return;
                }
            }

            last = cur;
        }

        if (g.isCyclic()) {
            System.out.println("Impossible");
        } else {
            Integer[] alph = g.topologicalSort();
            for (Integer integer : alph) {
                System.out.print((char) (integer + 'a'));
            }
        }
    }
}