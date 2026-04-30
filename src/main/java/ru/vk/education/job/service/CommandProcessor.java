package ru.vk.education.job.service;

import org.springframework.stereotype.Component;
import ru.vk.education.job.model.entity.Job;
import ru.vk.education.job.model.entity.User;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class CommandProcessor {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final FileService fileService;
    private final BestJobFinder bestJobFinder;
    private ScheduledExecutorService scheduler;

    public CommandProcessor(UserRepository userRepository,
                            JobRepository jobRepository,
                            FileService fileService,
                            BestJobFinder bestJobFinder) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.fileService = fileService;
        this.bestJobFinder = bestJobFinder;
    }

    public void start() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(bestJobFinder, 0, 60, TimeUnit.SECONDS);

        List<String> savedCommands = fileService.getCommandHistory();
        for (String command : savedCommands) {
            String[] parts = command.split(" ");
            if (parts.length > 0) {
                String cmd = parts[0];
                if (cmd.equals("user") || cmd.equals("job")) {
                    processCommand(command, false);
                }
            }
        }

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("exit")) {
                break;
            }
            processCommand(line, false);
            fileService.saveCommand(line);
        }

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }

        System.exit(0);
    }

    private void processCommand(String line, boolean isFromFile) {
        String[] parts = line.split(" ");
        if (parts.length == 0) return;

        String command = parts[0];

        switch (command) {
            case "user":
                handleUser(parts);
                break;
            case "user-list":
                handleUserList();
                break;
            case "job":
                handleJob(parts);
                break;
            case "job-list":
                handleJobList();
                break;
            case "suggest":
                handleSuggest(parts);
                break;
            case "history":
                handleHistory();
                break;
            case "stat":
                handleStat(parts);
                break;
            default:
                return;
        }
    }

    private void handleUser(String[] parts) {
        if (parts.length < 2) return;

        String name = parts[1];
        if (userRepository.contains(name)) return;

        Set<String> skills = new HashSet<>();
        int exp = 0;

        for (int i = 2; i < parts.length; i++) {
            String arg = parts[i];
            if (arg.startsWith("--skills=")) {
                String skillsStr = arg.substring("--skills=".length());
                String[] skillArray = skillsStr.split(",");
                for (String skill : skillArray) {
                    if (!skill.trim().isEmpty()) {
                        skills.add(skill.trim());
                    }
                }
            } else if (arg.startsWith("--exp=")) {
                exp = Integer.parseInt(arg.substring("--exp=".length()));
            }
        }
        userRepository.add(new User(name, skills, exp));
    }

    private void handleUserList() {
        for (User user : userRepository.getAllSorted()) {
            System.out.println(user.toString());
        }
    }

    private void handleJob(String[] parts) {
        if (parts.length < 2) return;

        String title = parts[1];
        if (jobRepository.contains(title)) return;

        String company = "";
        Set<String> tags = new HashSet<>();
        int exp = 0;

        for (int i = 2; i < parts.length; i++) {
            String arg = parts[i];
            if (arg.startsWith("--company=")) {
                company = arg.substring("--company=".length());
            } else if (arg.startsWith("--tags=")) {
                String tagsStr = arg.substring("--tags=".length());
                String[] tagArray = tagsStr.split(",");
                for (String tag : tagArray) {
                    if (!tag.trim().isEmpty()) {
                        tags.add(tag.trim());
                    }
                }
            } else if (arg.startsWith("--exp=")) {
                exp = Integer.parseInt(arg.substring("--exp=".length()));
            }
        }

        jobRepository.add(new Job(title, company, tags, exp));
    }

    private void handleJobList() {
        for (Job job : jobRepository.getAllSorted()) {
            System.out.println(job.toString());
        }
    }

    private void handleSuggest(String[] parts) {
        if (parts.length < 2) return;

        String username = parts[1];
        User user = userRepository.findByName(username);

        if (user == null) return;

        List<Job> suggestions = user.findSuitableJobs(jobRepository.getAll(), 2);

        for (Job job : suggestions) {
            System.out.println(job.toString());
        }
    }

    private void handleHistory() {
        List<String> history = fileService.getCommandHistory();
        for (String command : history) {
            System.out.println(command);
        }
    }

    private void handleStat(String[] parts) {
        if (parts.length < 3) {
            return;
        }

        String option = parts[1];
        int value = Integer.parseInt(parts[2]);

        switch (option) {
            case "--exp":
                jobRepository.getAllSorted().stream()
                        .filter(job -> job.getRequiredExp() >= value)
                        .forEach(System.out::println);
                break;

            case "--match":
                userRepository.getAllSorted().stream()
                        .filter(user -> user.getMatchCount(jobRepository.getAll()) >= value)
                        .forEach(System.out::println);
                break;

            case "--top-skills":
                userRepository.getTopSkills(value).stream()
                        .sorted()
                        .forEach(System.out::println);
                break;

            default:
                return;
        }
    }
}