# Kruskal's Algorithm Visualizer

This is a JavaFX desktop application that allows you to create and simulate Kruskal's algorithm to find a Minimum Spanning Tree (MST) on an interactive graph. You can add nodes and weighted edges visually, then watch Kruskal’s algorithm construct the MST step-by-step.

---

💻 Requirements

- Java 17 or newer (install from https://adoptium.net/)
- No need to install JavaFX separately — it's already included in the `Library/` folder

---

📦 Folder Structure

After downloading or setting up the project, your folder should look like this:

Project 2/
├── Kruskal_Algorithm/
│ ├── Kruskal Algorithm.jar ← Main application JAR
│ ├── run.bat ← Double-click to launch
│ ├── README.md ← This file
├── Library/
│ └── javafx-sdk-24.0.1/
│ └── lib/ ← JavaFX libraries (already included!)
│ ├── javafx.controls.jar
│ ├── javafx.fxml.jar
│ └── (other JavaFX modules)

---

🚀 How to Run the App

### ✅ Recommended: Double-click `run.bat`

Located inside the `Executable file` folder.

### 🔧 Or run manually from CMD/PowerShell:

From inside the `Executable file` folder:

java --module-path "..\Library\javafx-sdk-24.0.1\lib" ^
     --add-modules javafx.controls,javafx.fxml ^
     --enable-native-access=ALL-UNNAMED ^
     -jar "Kruskal Algorithm.jar"

---

📌 Controls and Usage

| Button     | Function                                                                 |
|------------|--------------------------------------------------------------------------|
| Nodes      | Click anywhere on the canvas to add nodes (labeled A–Z).                |
| Edge       | Click two nodes to connect them → enter edge weight.                    |
| Simulate   | Visually builds MST using Kruskal’s Algorithm step-by-step.             |
| Delete     | If a node/edge is selected (red), this deletes it.                      |
| Clear      | If nothing is selected, this removes all nodes and edges.               |

- Drag Nodes by clicking and moving them.
- Pan the Canvas by dragging empty space.

---

⚙️ Behind the Scenes

- Canvas-based drawing (JavaFX)
- Kruskal's Algorithm with Union-Find structure
- Thread-based simulation with delays
- Clean, animated UI using JavaFX components

---

🧪 Troubleshooting

❌ Error: QuantumRenderer: no suitable pipeline found  
This means JavaFX native libraries weren’t loaded.

✅ Fix: You must run the `.jar` with `--module-path` pointing to the correct `lib/` directory and include `--add-modules` as shown above.

---

🧠 Educational Purpose

This project is great for:

- Understanding Minimum Spanning Trees
- Seeing Kruskal’s Algorithm in action
- Exploring real-time graph interaction with JavaFX
- Practicing event-driven GUI design and animation

---

👤 Author

Noyan Ali, Sabeeha Mir, Murtaza Jamal
