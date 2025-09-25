# Jenkins Multibranch Pipeline Exercise

## Overview
This exercise will guide you through creating a Jenkins multibranch pipeline that automatically builds and deploys different branches of your application with branch-specific configurations. You'll learn how to handle feature branches, development, and main branches differently.

## Repository URL

**GitHub Repository**: https://github.com/neerajk555/jenkins-multibranch-demo

Clone this repository to get started:
```bash
git clone https://github.com/neerajk555/jenkins-multibranch-demo.git
cd jenkins-multibranch-demo
```

## Prerequisites
- Java 17 or higher installed
- Maven 3.6+ installed
- Git installed
- Jenkins server with Pipeline plugins
- GitHub account
- Basic understanding of Git branching

## Learning Objectives
By the end of this exercise, you will:
- Understand multibranch pipeline concepts
- Create branch-specific build configurations
- Set up automatic branch discovery
- Implement different deployment strategies per branch
- Configure branch-based build triggers

---

## Part 1: Local Setup and Testing

### Step 1: Clone and Test Locally
```bash
# Clone the repository
git clone https://github.com/neerajk555/jenkins-multibranch-demo.git
cd jenkins-multibranch-demo

# Test the application locally
mvn clean test
mvn package

# Run the application
java -jar target/simple-java-app-1.0-SNAPSHOT.jar
```

Expected output:
```
Hello World from Jenkins Multibranch Pipeline!
Build completed at: 2025-09-25 10:30:45
Application ready for Jenkins multibranch pipeline testing!
```

---

## Part 2: Create Branch Structure

### Step 1: Create Development Branch
```bash
# Create and switch to develop branch
git checkout -b develop

# Make a small change to indicate this is develop branch
echo "# Development Branch" >> README-dev.md
git add README-dev.md
git commit -m "Add development branch indicator"
git push -u origin develop
```

### Step 2: Create Feature Branch
```bash
# Create feature branch from develop
git checkout develop
git checkout -b feature/add-calculator

# Add a simple calculator class
mkdir -p src/main/java/com/example/app/utils
```

Create `src/main/java/com/example/app/utils/Calculator.java`:
```java
package com.example.app.utils;

public class Calculator {
    
    public double add(double a, double b) {
        return a + b;
    }
    
    public double subtract(double a, double b) {
        return a - b;
    }
    
    public double multiply(double a, double b) {
        return a * b;
    }
    
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        return a / b;
    }
}
```

Create test for the calculator `src/test/java/com/example/app/utils/CalculatorTest.java`:
```java
package com.example.app.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    
    private Calculator calculator;
    
    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }
    
    @Test
    void testAdd() {
        assertEquals(5.0, calculator.add(2.0, 3.0));
        assertEquals(0.0, calculator.add(-1.0, 1.0));
    }
    
    @Test
    void testSubtract() {
        assertEquals(1.0, calculator.subtract(3.0, 2.0));
        assertEquals(-2.0, calculator.subtract(-1.0, 1.0));
    }
    
    @Test
    void testMultiply() {
        assertEquals(6.0, calculator.multiply(2.0, 3.0));
        assertEquals(0.0, calculator.multiply(0.0, 5.0));
    }
    
    @Test
    void testDivide() {
        assertEquals(2.0, calculator.divide(6.0, 3.0));
        assertEquals(0.5, calculator.divide(1.0, 2.0));
        
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(5.0, 0.0);
        });
    }
}
```

Commit and push the feature:
```bash
git add .
git commit -m "Add calculator utility class with tests"
git push -u origin feature/add-calculator
```

---

## Part 3: Configure Multibranch Pipeline in Jenkins

### Step 1: Create Multibranch Pipeline Job
1. From Jenkins Dashboard, click "New Item"
2. Enter name: `jenkins-multibranch-demo`
3. Select "Multibranch Pipeline" and click OK

### Step 2: Configure Branch Sources
In the configuration page:

**Branch Sources:**
- Click "Add source" → Git
- Project Repository: `https://github.com/neerajk555/jenkins-multibranch-demo.git`

**Build Configuration:**
- Mode: by Jenkinsfile
- Script Path: `Jenkinsfile`

**Scan Multibranch Pipeline Triggers:**
- Check "Periodically if not otherwise run"
- Interval: 1 minute (for testing)

**Branch Discovery:**
- Discover branches: All branches
- Discover pull requests from origin: Merging the pull request with current target branch revision

### Step 3: Advanced Configuration (Optional)
**Property Strategy:**
- All branches get the same properties

**Orphaned Item Strategy:**
- Discard old items: Check
- Days to keep old items: 7
- Max # of old items to keep: 10

Click "Save"

---

## Part 4: Test the Multibranch Pipeline

### Step 1: Initial Scan
1. Jenkins will automatically scan your repository
2. You should see builds triggered for:
   - `main` branch (production deployment)
   - `develop` branch (staging deployment)  
   - `feature/add-calculator` branch (development, no deployment)

### Step 2: Verify Branch-Specific Behavior
Check each branch build and verify:

**Main Branch:**
- Environment: prod
- Deploy enabled: true
- Runs code quality checks
- Creates production artifacts

**Develop Branch:**
- Environment: staging
- Deploy enabled: true
- Runs code quality checks
- Creates staging artifacts

**Feature Branch:**
- Environment: dev
- Deploy enabled: false
- Skips code quality checks
- Creates development artifacts

### Step 3: Test Pull Request Workflow
1. Create a pull request from `feature/add-calculator` to `develop`
2. Jenkins should automatically build the PR
3. Merge the PR and verify `develop` branch rebuilds
4. Create PR from `develop` to `main` for production release

---

## Expected Results

When your multibranch pipeline is working correctly:

### Branch Discovery
- Jenkins automatically discovers new branches
- Each branch gets its own pipeline instance
- Old/deleted branches are cleaned up automatically

### Build Differentiation
- **Main**: Full pipeline with production deployment
- **Develop**: Full pipeline with staging deployment
- **Feature branches**: Build and test only, no deployment
- **Pull requests**: Validation builds

### Console Output Examples
```
[main] Building branch: main
[main] Environment: prod
[main] Deploy enabled: true
[main] ✅ Build successful for main (prod)
[main] 🚀 Production deployment completed successfully!

[develop] Building branch: develop  
[develop] Environment: staging
[develop] Deploy enabled: true
[develop] ✅ Build successful for develop (staging)

[feature/add-calculator] Building branch: feature/add-calculator
[feature/add-calculator] Environment: dev
[feature/add-calculator] Deploy enabled: false
[feature/add-calculator] ✅ Build successful for feature/add-calculator (dev)
```

---

## Troubleshooting

### Common Issues

**Issue 1: No branches discovered**
- Check repository URL and credentials
- Verify branch discovery settings
- Manually trigger "Scan Multibranch Pipeline Now"

**Issue 2: Builds not triggered automatically**
- Verify webhook configuration in GitHub
- Check scan trigger settings
- Ensure branch patterns match your branches

**Issue 3: Environment variables not set correctly**
- Check script syntax in Jenkinsfile
- Verify branch name matching logic
- Add debug echo statements

**Issue 4: Configuration files not found**
- Ensure config files are committed to all branches
- Add existence checks in pipeline script
- Use fallback configurations

### Verification Checklist
1. ✅ All branches appear in Jenkins UI
2. ✅ Each branch builds with correct environment
3. ✅ Deployment logic works per branch type
4. ✅ Pull requests trigger builds
5. ✅ Artifacts are branch-specific
6. ✅ Old branches are cleaned up

---

## Next Steps

Once your multibranch pipeline is working:

1. **GitFlow Integration**: Implement full GitFlow branching model
2. **Environment Promotion**: Add manual promotion between environments
3. **Approval Gates**: Add manual approval for production deployments
4. **Monitoring**: Integrate with monitoring and alerting systems
5. **Advanced Deployments**: Implement blue-green or canary deployments

This exercise demonstrates the power of multibranch pipelines for managing complex deployment workflows across different environments and development stages.

## Support

If you encounter any issues:
1. Check the troubleshooting section above
2. Review Jenkins console logs for detailed error messages
3. Verify your local Maven build works before debugging Jenkins
4. Ensure all required Jenkins plugins are installed

Happy learning! 🚀