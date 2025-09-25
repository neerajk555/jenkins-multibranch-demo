package com.example.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple Java Application for Jenkins Multibranch Pipeline Demo
 */
public class App {
    
    public static void main(String[] args) {
        App app = new App();
        System.out.println(app.getGreeting());
        System.out.println("Build completed at: " + app.getCurrentTimestamp());
        System.out.println("Application ready for Jenkins multibranch pipeline testing!");
    }
    
    public String getGreeting() {
        return "Hello World from Jenkins Multibranch Pipeline!";
    }
    
    public String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
    
    public int add(int a, int b) {
        return a + b;
    }
    
    public boolean isEven(int number) {
        return number % 2 == 0;
    }
    
    public String getEnvironmentInfo() {
        return "Running in: " + System.getProperty("java.version");
    }
}