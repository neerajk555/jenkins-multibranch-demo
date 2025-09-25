package com.example.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    
    private App app;
    
    @BeforeEach
    void setUp() {
        app = new App();
    }
    
    @Test
    void testGetGreeting() {
        String greeting = app.getGreeting();
        assertNotNull(greeting);
        assertEquals("Hello World from Jenkins Multibranch Pipeline!", greeting);
    }
    
    @Test
    void testAdd() {
        assertEquals(5, app.add(2, 3));
        assertEquals(0, app.add(-1, 1));
        assertEquals(-5, app.add(-2, -3));
    }
    
    @Test
    void testIsEven() {
        assertTrue(app.isEven(2));
        assertTrue(app.isEven(0));
        assertTrue(app.isEven(-4));
        assertFalse(app.isEven(1));
        assertFalse(app.isEven(3));
        assertFalse(app.isEven(-3));
    }
    
    @Test
    void testGetCurrentTimestamp() {
        String timestamp = app.getCurrentTimestamp();
        assertNotNull(timestamp);
        assertTrue(timestamp.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }
    
    @Test
    void testGetEnvironmentInfo() {
        String envInfo = app.getEnvironmentInfo();
        assertNotNull(envInfo);
        assertTrue(envInfo.startsWith("Running in:"));
    }
}