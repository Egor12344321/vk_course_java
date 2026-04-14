package ru.vk.education.job.service;

import java.io.*;
import java.util.*;

public class FileService {
    private static final String COMMANDS_FILE = "commands.txt";

    public void saveCommand(String command) {
        try (FileWriter fw = new FileWriter(COMMANDS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(command);
        } catch (IOException e) {
        }
    }

    public List<String> getCommandHistory() {
        List<String> commands = new ArrayList<>();
        File file = new File(COMMANDS_FILE);

        if (!file.exists()) {
            return commands;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                commands.add(line);
            }
        } catch (IOException e) {
        }

        return commands;
    }
}