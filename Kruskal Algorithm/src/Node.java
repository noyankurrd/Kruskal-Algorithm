import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Node {
    double x, y;
    String label;
    int radius = 25;
    private Color color;

    // constructor
    public Node(double x, double y, String label) {
        this.x = x;
        this.y = y;
        this.label = label;
        this.color = Color.CYAN; // default color
    }

    // draw function
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(x - 14, y - 11, radius, radius);

        gc.setStroke(Color.BLACK);
        gc.strokeOval(x - 14, y - 11, radius, radius);

        gc.setFill(Color.BLACK);
        gc.fillText(label, x - 6, y + 6);
    }

    // check if a point overlaps the node
    public boolean contains(double mouseX, double mouseY) {
        double dx = mouseX - x;
        double dy = mouseY - y;
        return Math.sqrt(dx * dx + dy * dy) <= radius;
    }

    public void setColor(Color color) { this.color = color; }
    public Color getColor() { return color; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public double getX() { return x; }
    public double getY() { return y; }
    public String getLabel() { return label; }
}