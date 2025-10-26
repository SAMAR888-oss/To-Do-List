import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * ToDoListApp
 * Java Swing To-Do List Project
 * Features:
 * - Add tasks
 * - Remove selected tasks
 * - Mark tasks as done
 * - Save/Load tasks from file
 */
public class ToDoListApp extends JFrame {
    private DefaultListModel<String> taskModel;
    private JList<String> taskList;
    private JTextField taskField;
    private JButton addButton, removeButton, doneButton, saveButton, loadButton;

    private static final String FILE_NAME = "tasks.txt";

    public ToDoListApp() {
        super("📝 To-Do List");

        // Initialize model and list
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);

        // Input field and buttons
        taskField = new JTextField();
        addButton = new JButton("Add Task");
        removeButton = new JButton("Remove Task");
        doneButton = new JButton("Mark Done");
        saveButton = new JButton("Save Tasks");
        loadButton = new JButton("Load Tasks");

        // Top panel for input
        JPanel topPanel = new JPanel(new BorderLayout(5,5));
        topPanel.add(taskField, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        // Bottom panel for action buttons
        JPanel bottomPanel = new JPanel(new GridLayout(1,4,5,5));
        bottomPanel.add(removeButton);
        bottomPanel.add(doneButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(loadButton);

        // Layout setup
        setLayout(new BorderLayout(10,10));
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action listeners
        addButton.addActionListener(e -> addTask());
        taskField.addActionListener(e -> addTask());
        removeButton.addActionListener(e -> removeTask());
        doneButton.addActionListener(e -> markDone());
        saveButton.addActionListener(e -> saveTasks());
        loadButton.addActionListener(e -> loadTasks());

        // Window settings
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        // Load tasks automatically
        loadTasks();
    }

    private void addTask() {
        String task = taskField.getText().trim();
        if (!task.isEmpty()) {
            taskModel.addElement(task);
            taskField.setText("");
        }
    }

    private void removeTask() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            taskModel.remove(index);
        }
    }

    private void markDone() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            String task = taskModel.get(index);
            if (!task.startsWith("✔ ")) {
                taskModel.set(index, "✔ " + task);
            }
        }
    }

    private void saveTasks() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskModel.size(); i++) {
                pw.println(taskModel.get(i));
            }
            JOptionPane.showMessageDialog(this, "Tasks saved successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving tasks: " + ex.getMessage());
        }
    }

    private void loadTasks() {
        taskModel.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                taskModel.addElement(line);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListApp::new);
    }
}
