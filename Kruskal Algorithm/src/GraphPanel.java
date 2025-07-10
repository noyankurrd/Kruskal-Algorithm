import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Optional;

public class GraphPanel extends Canvas {
    private final ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private boolean addingNode = false;
    private boolean addingEdge = false;
    private Node firstSelected = null;
    private char currentLabel = 'A';
    private Node draggedNode = null;
    private double dragOffsetX, dragOffsetY;
    private boolean wasDragged = false;
    private Edge selectedEdge = null;
    private static GraphPanel instance;
    private Runnable onSelectionChanged = null;
    private boolean draggingNode = false;
    private final ArrayList<Character> recycledLabels = new ArrayList<>();

    // constructor
    public GraphPanel(double width, double height) {
        super(width, height);
        instance = this;

        // mouse functions for when button is pressed (dragging the node)
        setOnMousePressed(e -> {
            wasDragged = false;
            draggedNode = getNodeAt(e.getX(), e.getY());
            if (draggedNode != null) {
                dragOffsetX = e.getX() - draggedNode.getX();
                dragOffsetY = e.getY() - draggedNode.getY();
                draggingNode = true;
            } else {
                draggingNode = false;
            }
        });

        // mouse functions for when mouse is dragged when pressed (dragging the node)
        setOnMouseDragged(e -> {
            wasDragged = true;
            if (draggedNode != null) {
                draggedNode.setX(e.getX() - dragOffsetX);
                draggedNode.setY(e.getY() - dragOffsetY);
                draw();
            }
        });

        // mouse functions for when mouse is released (dragging the node and handleClick func)
        setOnMouseReleased(e -> {
            draggingNode = false;
            draggedNode = null;
            if (!wasDragged) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    // for adding/selecting nodes/edges
    private void handleClick(double x, double y) {
        selectedEdge = getEdgeAt(x, y); // assign an edge to the variable
        if (selectedEdge != null) {
            if (onSelectionChanged != null) onSelectionChanged.run();
            draw();
            return;
        }

        Node clicked = getNodeAt(x, y); // assign a node to the variable
        if (addingNode) {
            if (clicked == null && currentLabel <= 'Z') {
                char label;
                if (!recycledLabels.isEmpty()) {
                    label = recycledLabels.removeFirst(); // checks if a label is available and extract it
                } else {
                    label = currentLabel++;
                }
                nodes.add(new Node(x, y, String.valueOf(label))); // append it into the list and toggle the button to off
                addingNode = false;
                draw(); // refresh the canvas
            }
            return;
        }

        if (addingEdge && clicked != null) {
            if (firstSelected == null) {
                clearNodeSelection();
                clicked.setColor(Color.RED);
                firstSelected = clicked;
            } else if (firstSelected != clicked) {
                showWeightDialog(firstSelected, clicked);
                clearNodeSelection();
                firstSelected = null;
                addingEdge = false;
            } else {
                clicked.setColor(Color.BLUE);
                firstSelected = null;
                addingEdge = false;
            }
            draw();
            return;
        }

        clearNodeSelection();
        selectedEdge = null;

        if (clicked != null) clicked.setColor(Color.RED);
        if (onSelectionChanged != null) onSelectionChanged.run();
        draw();
    }

    // for finding out which node is clicked
    private Node getNodeAt(double x, double y) {
        for (Node node : nodes) {
            if (node.contains(x, y)) return node;
        }
        return null;
    }

    // for finding out which node is clicked
    private Edge getEdgeAt(double x, double y) {
        for (Edge edge : edges) {
            if (edge.containsPoint(x, y)) return edge;
        }
        return null;
    }

    // to construct a popup window for weightage
    private void showWeightDialog(Node from, Node to) {
        TextInputDialog dialog = new TextInputDialog("1.0");
        dialog.setTitle("Edge Weight");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter edge weight:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(weightText -> {
            try {
                float weight = Float.parseFloat(weightText.trim()); // extract the input from textfield and convert it to float
                edges.add(new Edge(from, to,(int) weight)); // add a new edge when textfield has a value
            } catch (NumberFormatException ignored) {} // if a value isn't an integer, ignore it and don't add an edge
        });
    }

    // to delete the selected node and all the edges connected to it
    public void deleteSelectedNode() {
        ArrayList<Node> toRemove = new ArrayList<>(); // create an array to hold the node
        for (Node node : nodes) {
            if (node.getColor().equals(Color.RED)) {
                toRemove.add(node);
            }
        }
        edges.removeIf(edge -> toRemove.contains(edge.from) || toRemove.contains(edge.to)); // remove all the edges if the selected node is in that edge
        for (Node node : toRemove) {
            recycledLabels.add(node.getLabel().charAt(0)); // Save label for reuse
        }
        nodes.removeAll(toRemove); // remove the node

        if (onSelectionChanged != null) onSelectionChanged.run();
        draw();
    }

    // to delete the selected edge
    public void deleteSelectedEdge() {
        if (selectedEdge != null) {
            edges.remove(selectedEdge);
            selectedEdge = null;
            if (onSelectionChanged != null) onSelectionChanged.run();
            draw();
        }
    }

    public void enableAddNodeMode() {
        addingNode = true;
        addingEdge = false;
    }

    public void enableAddEdgeMode() {
        addingEdge = true;
        addingNode = false;
        firstSelected = null;
    }

    // for drawing the canvas
    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        for (Edge edge : edges) edge.draw(gc);
        for (Node node : nodes) node.draw(gc);
    }

    // to clear the canvas
    public void clear() {
        nodes.clear();
        edges.clear();
        currentLabel = 'A';
        selectedEdge = null;
        if (onSelectionChanged != null) onSelectionChanged.run();
        draw();
    }

    // for simulation
    public void simulate() {
        ArrayList<Edge> sortedEdges = new ArrayList<>(edges); // create an array for edges
        sortedEdges.sort((a, b) -> Float.compare(a.weightage, b.weightage)); // sort it out according their weightage
        UnionFind uf = new UnionFind(); // create a new unionfind object

        // start a new thread that runs the simulation
        new Thread(() -> {
            ArrayList<Edge> mstEdges = new ArrayList<>(); // MST edges
            for (Edge edge : sortedEdges) {
                String fromLabel = edge.from.label;
                String toLabel = edge.to.label;

                // check if both nodes are connected (having same parent)
                if (!uf.connected(fromLabel, toLabel)) {
                    uf.union(fromLabel, toLabel); // if not then merge them under the same parent (make one node the parent of both)
                    edge.inMST = true; // change the state for coloring
                    mstEdges.add(edge);
                    javafx.application.Platform.runLater(this::draw);
                    try {
                        Thread.sleep(800); // refresh the screen every 0.8 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            edges = mstEdges; // change the edges to mst
            javafx.application.Platform.runLater(this::draw);

            // pop up window for showing all the edges in MST
            StringBuilder message = new StringBuilder("Minimum Spanning Tree Edges:\n");
            for (Edge edge : mstEdges) {
                message.append(edge.from.label)
                        .append(" - ")
                        .append(edge.to.label)
                        .append(" (")
                        .append(edge.weightage)
                        .append(")\n");
            }

            String finalMessage = message.toString();
            javafx.application.Platform.runLater(() -> {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Kruskal's Algorithm Complete");
                alert.setHeaderText("MST Result");
                alert.setContentText(finalMessage);
                alert.showAndWait();
            });

        }).start();
    }

    public boolean isDraggingNode() {return draggingNode;}
    public static Edge getSelectedEdgeStatic() {
        return instance != null ? instance.selectedEdge : null;
    }
    public void setOnSelectionChanged(Runnable callback) {
        this.onSelectionChanged = callback;
    }
    public boolean hasSelection() {return selectedEdge != null || nodes.stream().anyMatch(n -> n.getColor().equals(Color.RED));}
    private void clearNodeSelection() { for (Node node : nodes) node.setColor(Color.CYAN); }
}


class UnionFind {
    private final java.util.HashMap<String, String> parent; // hashmap for parent

    // constructor
    public UnionFind() {
        parent = new java.util.HashMap<>();
    }

    // find the parent of a node
    public String find(String x) {
        if (!parent.containsKey(x)) {
            parent.put(x, x);
        }
        if (!parent.get(x).equals(x)) {
            parent.put(x, find(parent.get(x)));
        }
        return parent.get(x);
    }

    // merge two nodes (making their parent same)
    public void union(String x, String y) {
        String rootX = find(x);
        String rootY = find(y);
        if (!rootX.equals(rootY)) {
            parent.put(rootY, rootX);
        }
    }

    public boolean connected(String x, String y) {
        return find(x).equals(find(y));
    }
}