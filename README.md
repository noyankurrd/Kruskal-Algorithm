# Kruskal's Algorithm Visualizer

This is a JavaFX desktop application that allows you to create and simulate Kruskal's algorithm to find a Minimum Spanning Tree (MST) on an interactive graph. You can add nodes and weighted edges visually, then watch Kruskal’s algorithm construct the MST step-by-step.

---

💻 Requirements

- Java 17 or newer (install from https://adoptium.net/)
- No need to install JavaFX separately — it's included in the lib/ folder

---

📦 Folder Structure

After cloning this repo, your folder will look like this:

Kruskal_Algorithm/
├── Kruskal Algorithm.jar       ← main application JAR
├── lib/                        ← JavaFX libraries (included!)
│   ├── javafx.controls.jar
│   ├── javafx.fxml.jar
│   └── (other JavaFX modules)
├── run.bat                     ← double-click to launch
├── README.md                   ← this file

---

🚀 How to Run the App (CMD or PowerShell)

From the folder where the .jar is located:

java --module-path "lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=ALL-UNNAMED -jar "Kruskal Algorithm.jar"

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

✅ You must run the .jar using --module-path pointing to the correct lib/ directory and add the required modules like shown above.

---

🧠 Educational Purpose

This project is great for:

- Understanding Minimum Spanning Trees
- Seeing Kruskal’s Algorithm in action
- Exploring real-time graph interaction with JavaFX
- Practicing event-driven GUI design and animation

---

📝 License

This project is licensed under the MIT License — feel free to use, modify, and share.

---

👤 Author

Noyan Ali, Sabeeha Mir, Murtaza Jamal
\n Feel free to reach out for feedback or contributions.
