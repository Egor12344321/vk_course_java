package ru.vk.education.job.service;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.vk.education.job.service.CommandProcessor;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private final CommandProcessor commandProcessor;

    public ConsoleRunner(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    @Override
    public void run(String... args) {
        commandProcessor.start();
    }
}