package com.example.kyrssave;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class ToDoListApp extends Application {

    private static final String FILE_NAME = "tasks.txt";

    private ListView<String> listView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Планувальник завдань");

        listView = new ListView<>();
        loadTasksFromFile();

        TextField taskInput = new TextField();
        taskInput.setPromptText("Введіть задачу");

        Button addButton = new Button("Додати");
        addButton.setOnAction(e -> {
            String task = taskInput.getText().trim();
            if (!task.isEmpty()) {
                listView.getItems().add(task);
                taskInput.clear();
                saveTasksToFile();
            }
        });

        Button removeButton = new Button("Видалити вибране");
        removeButton.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                listView.getItems().remove(selected);
                saveTasksToFile();
            }
        });

        Button manualSaveButton = new Button("Зберегти у файл...");
        manualSaveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Зберегти задачі як...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                saveTasksToCustomFile(file);
            }
        });

        Button loadFromFileButton = new Button("Завантажити з файлу...");
        loadFromFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Виберіть файл для завантаження");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                loadTasksFromCustomFile(file);
            }
        });

        HBox inputBox = new HBox(10, taskInput, addButton);
        VBox root = new VBox(10, inputBox, listView, removeButton, manualSaveButton, loadFromFileButton);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_NAME))) {
            for (String task : listView.getItems()) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTasksToCustomFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String task : listView.getItems()) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasksFromFile() {
        Path path = Paths.get(FILE_NAME);
        if (Files.exists(path)) {
            try {
                List<String> lines = Files.readAllLines(path);
                listView.getItems().addAll(lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTasksFromCustomFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            listView.getItems().clear();
            String line;
            while ((line = reader.readLine()) != null) {
                listView.getItems().add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
