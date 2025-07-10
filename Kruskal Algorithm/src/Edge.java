import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Edge {
    Node from, to;
    int weightage;
    boolean inMST = false;
    private double boxX, boxY; // Top-left of the weight box for selection

    // Constructor
    public Edge(Node from, Node to, int weightage) {
        this.from = from;
        this.to = to;
        this.weightage = weightage;
    }

    // draw function
    public void draw(GraphicsContext gc) {
        double x1 = from.getX(); // node 1 x pos
        double y1 = from.getY(); // node 1 y pos
        double x2 = to.getX(); // node 2 x pos
        double y2 = to.getY(); // node 2 y pos

        gc.setLineWidth(this == GraphPanel.getSelectedEdgeStatic() ? 4 : 3); // change the line stroke when selected
        gc.setStroke(this == GraphPanel.getSelectedEdgeStatic() ? Color.RED : (inMST ? Color.GREEN : Color.GRAY)); // change the color when selected
        gc.strokeLine(x1, y1, x2, y2); // create a line

        double midX = (x1 + x2) / 2; // find the middle x point
        double midY = (y1 + y2) / 2; // find the middle y point
        String weight = String.format("%d", weightage); // change the weightage to string

        boxX = midX - 10;
        boxY = midY - 10;

        gc.setFill(Color.WHITE);
        gc.fillRect(boxX, boxY, 25, 25);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(boxX, boxY, 25, 25);

        if (this == GraphPanel.getSelectedEdgeStatic()) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeRect(boxX, boxY, 25, 25);
        }

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 12));
        Text text = new Text(weight);
        text.setFont(gc.getFont());
        double textWidth = text.getLayoutBounds().getWidth();
        gc.fillText(weight, midX - textWidth / 2 + 1, midY + 5);
    }

    // check if a point overlaps the edge
    public boolean containsPoint(double px, double py) {
        return px >= boxX && px <= boxX + 25 &&
                py >= boxY && py <= boxY + 25;
    }
}