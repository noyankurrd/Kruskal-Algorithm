import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Group;
import java.util.List;

public class GraphVisualizer extends Application {

    // Default Button style
    private static final String BUTTON_STYLE =
            "-fx-background-color: #333333; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 14px; " +
                    "-fx-background-radius: 20px; " +
                    "-fx-padding: 10px;";

    // Pressed Button style
    private static final String BUTTON_STYLE_PRESSED =
            "-fx-background-color: #5566ff; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 14px; " +
                    "-fx-background-radius: 20px; " +
                    "-fx-padding: 10px;";

    @Override
    public void start(Stage primaryStage) {
        GraphPanel graphPanel = new GraphPanel(2000, 2000); // creates a new object of Graph panel

        Group zoomGroup = new Group(graphPanel); // add the Graph panel in a group for moving the panel
        Pane zoomPane = new Pane(zoomGroup);

        zoomPane.setPrefSize(800, 600); // set the pane size to 800x600
        zoomPane.setStyle("-fx-background-color: black;"); // set the background to black

        // for translation of the panel
        final double[] dragDelta = new double[2];
        zoomPane.setOnMousePressed(e -> {
            dragDelta[0] = e.getSceneX() - zoomGroup.getTranslateX();
            dragDelta[1] = e.getSceneY() - zoomGroup.getTranslateY();
        });

        zoomPane.setOnMouseDragged(e -> {
            if (!graphPanel.isDraggingNode()) {
                zoomGroup.setTranslateX(e.getSceneX() - dragDelta[0]);
                zoomGroup.setTranslateY(e.getSceneY() - dragDelta[1]);
            }
        });

        // Buttons
        VBox buttonBar = new VBox(18); // button panel with 18 pixels space between the boxes
        buttonBar.setPadding(new Insets(20)); // padding of 20 pixels form left
        buttonBar.setAlignment(Pos.CENTER); // positioned centered
        buttonBar.setStyle("-fx-background-color: white; -fx-background-radius: 25px;"); // white background with 25 pixel rounded corner radius
        buttonBar.setPrefSize(192, 275); // default size
        buttonBar.setMinSize(192, 275); // to make it constant
        buttonBar.setMaxSize(192, 275);

        // creating buttons
        Button nodeBtn = createButton("Nodes");
        Button edgeBtn = createButton("Edge");
        Button simulateBtn = createButton("Simulate");
        Button clearBtn = createButton("Clear");

        List<Button> buttons = List.of(nodeBtn, edgeBtn, simulateBtn, clearBtn);
        for (Button button : buttons) {
            button.setPrefSize(134, 45); // set their size to constant
        }

        nodeBtn.setOnAction(_ -> {graphPanel.enableAddNodeMode();}); // add node function to the button

        edgeBtn.setOnAction(_ -> {graphPanel.enableAddEdgeMode();}); // add edge function to the button

        simulateBtn.setOnAction(_ -> {graphPanel.simulate();}); // add simulate function to the button

        clearBtn.setOnAction(_ -> {graphPanel.clear();}); // add clear function to the button

        // Setup clear/delete toggle behavior
        updateClearDeleteButton(graphPanel, clearBtn);

        buttonBar.getChildren().addAll(nodeBtn, edgeBtn, simulateBtn, clearBtn); // add all the buttons to button bar

        StackPane root = new StackPane(zoomPane);
        StackPane.setAlignment(buttonBar, Pos.TOP_RIGHT);
        StackPane.setMargin(buttonBar, new Insets(50, 40, 0, 0));
        root.getChildren().add(buttonBar); // add the button bar to stackpane and display it on top right



        Scene scene = new Scene(root, 820, 640);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Kruskal's Algorithm");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // to create button
    private Button createButton(String name) {
        Button btn = new Button(name);
        btn.setStyle(BUTTON_STYLE);
        btn.setPrefWidth(100);

        // Add visual cue via mouse pressed/released
        btn.setOnMousePressed(_ -> {
            btn.setStyle(BUTTON_STYLE_PRESSED);
        });

        btn.setOnMouseReleased(_ -> {
            btn.setStyle(BUTTON_STYLE); // restore on release
        });

        return btn;
    }

    // to change clear to delete button
    private void updateClearDeleteButton(GraphPanel graphPanel, Button clearButton) {
        Runnable onSelectionChanged = () -> {
            if (graphPanel.hasSelection()) {
                clearButton.setText("Delete");
                clearButton.setOnAction(_ -> {
                    graphPanel.deleteSelectedEdge();
                    graphPanel.deleteSelectedNode();
                    clearButton.setText("Clear");
                    clearButton.setOnAction(_ -> graphPanel.clear());
                });
            } else {
                clearButton.setText("Clear");
                clearButton.setOnAction(_ -> graphPanel.clear());
            }
        };

        graphPanel.setOnSelectionChanged(onSelectionChanged);
        onSelectionChanged.run(); // initialize button state
    }

    public static void main(String[] args) {
        launch(args);
    }
}
