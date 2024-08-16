import java.awt.Cursor;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class SearchAlgorithms extends Thread {

    private Grid grid;
    private JPanel panel;
    private boolean solutionFound = false;

    public SearchAlgorithms(Grid grid, JPanel panel) {
        this.grid = grid;
        this.panel = panel;
    }

    @Override
    public void run() {
        if (MyUtils.solving) {
            MyUtils.breakAlgo = false;
            solutionFound = false;

            switch (MyUtils.algorithm) {
                case 0:
                    bfs(grid.getStart());
                    break;
                case 1:
                    dfs(grid.getStart());
                    break;
                case 2:
                    best(grid.getStart());
                    break;
                case 3:
                    astar(grid.getStart());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid algorithm index");
            }
        }
        finalizeSearch();
    }

    private void finalizeSearch() {
        MyUtils.solving = false;
        if (MyUtils.breakAlgo) {
            grid.initialiseGrid();
        }
        panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        panel.revalidate();
        panel.repaint();
    }

    private void astar(Node start) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(start);
        start.setAlreadyVisited(true);

        while (MyUtils.solving && !solutionFound && !queue.isEmpty()) {
            Node current = queue.poll();
            current.setType(Type.CURRENT);
            updatePanel();

            if (current.equals(grid.getFinish())) {
                extractSolution(current);
                solutionFound = true;
                return;
            } else {
                current.setType(Type.VISITED);
                addNeighborsToQueue(queue, current);
            }
        }
    }

    private void best(Node start) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(start);
        start.setAlreadyVisited(true);

        while (MyUtils.solving && !solutionFound && !queue.isEmpty()) {
            Node current = queue.poll();
            current.setType(Type.CURRENT);
            updatePanel();

            if (current.equals(grid.getFinish())) {
                extractSolution(current);
                solutionFound = true;
                return;
            } else {
                current.setType(Type.VISITED);
                addNeighborsToQueue(queue, current);
            }
        }
    }

    private void addNeighborsToQueue(PriorityQueue<Node> queue, Node current) {
        for (Node child : current.getNeighbors(grid)) {
            if (!child.isAlreadyVisited()) {
                queue.add(child);
                child.setAlreadyVisited(true);
                child.setType(Type.FRONTIER);
            }
        }
    }

    private void dfs(Node start) {
        dfsRecursive(start);
    }

    private void dfsRecursive(Node node) {
        if (!MyUtils.solving || solutionFound) {
            return;
        }
        node.setType(Type.CURRENT);
        node.setAlreadyVisited(true);
        updatePanel();

        if (node.equals(grid.getFinish())) {
            extractSolution(node);
            solutionFound = true;
            return;
        } else {
            node.setType(Type.VISITED);
            for (Node child : node.getNeighbors(grid)) {
                if (!child.isAlreadyVisited()) {
                    dfsRecursive(child);
                }
            }
        }
    }

    private void bfs(Node startingNode) {
        Queue<Node> frontier = new LinkedList<>();
        frontier.add(startingNode);

        while (MyUtils.solving && !frontier.isEmpty() && !solutionFound) {
            Node currentNode = frontier.poll();
            currentNode.setType(Type.CURRENT);
            updatePanel();

            if (currentNode.equals(grid.getFinish())) {
                extractSolution(currentNode);
                solutionFound = true;
            } else {
                currentNode.setType(Type.VISITED);
                addNeighborsToQueue(frontier, currentNode);
            }
        }
    }

    private void addNeighborsToQueue(Queue<Node> queue, Node current) {
        for (Node neighbor : current.getNeighbors(grid)) {
            if (!neighbor.isAlreadyVisited()) {
                queue.add(neighbor);
                neighbor.setType(Type.FRONTIER);
                neighbor.setAlreadyVisited(true);
            }
        }
    }

    private void extractSolution(Node node) {
        if (!MyUtils.solving) {
            return;
        }

        Node parent = node.getParent();
        while (parent != null && !grid.getStart().equals(parent)) {
            parent.setType(Type.PATH);
            updatePanel();
            parent = parent.getParent();
        }
    }

    private void updatePanel() {
        try {
            Thread.sleep(MyUtils.delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        SwingUtilities.invokeLater(() -> {
            panel.revalidate();
            panel.repaint();
        });
    }
}
