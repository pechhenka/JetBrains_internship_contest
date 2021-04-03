import java.util.*;

class Graph {

    private final int countVertex;
    private final ArrayList<ArrayList<Integer>> g;

    Graph(final int countVertex) {
        this.countVertex = countVertex;

        g = new ArrayList<>(countVertex);
        for (int i = 0; i < countVertex; ++i) {
            g.add(new ArrayList<>());
        }
    }

    private boolean isCyclicDfs(final int vertex, final boolean[] used, final boolean[] recStack) {
        if (recStack[vertex]) {
            return true;
        }

        if (used[vertex]) {
            return false;
        }

        used[vertex] = true;

        recStack[vertex] = true;
        List<Integer> children = g.get(vertex);

        for (Integer c : children) {
            if (isCyclicDfs(c, used, recStack)) {
                return true;
            }
        }

        recStack[vertex] = false;

        return false;
    }

    public boolean isCyclic() {
        final boolean[] used = new boolean[countVertex];
        final boolean[] recStack = new boolean[countVertex];

        Arrays.fill(used, false);
        Arrays.fill(recStack, false);

        for (int i = 0; i < countVertex; i++) {
            if (isCyclicDfs(i, used, recStack)) {
                return true;
            }
        }

        return false;
    }

    public void addEdge(final int v, final int w) {
        g.get(v).add(w);
    }

    private void topologicalDfs(final int vertex, final boolean[] used, final Stack<Integer> path) {
        used[vertex] = true;

        ArrayList<Integer> vertexPaths = g.get(vertex);
        for (int to : vertexPaths) {
            if (!used[to]) {
                topologicalDfs(to, used, path);
            }
        }

        path.push(vertex);
    }

    public Integer[] topologicalSort() {
        Stack<Integer> path = new Stack<>();

        boolean[] used = new boolean[countVertex];
        Arrays.fill(used, false);

        for (int i = 0; i < countVertex; i++) {
            if (!used[i]) {
                topologicalDfs(i, used, path);
            }
        }

        return path.toArray(Integer[]::new);
    }
}
