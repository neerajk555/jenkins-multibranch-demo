# Jenkins Multibranch Pipeline Demo

This repository demonstrates Jenkins multibranch pipeline concepts with a simple Java Maven application.

## Project Structure

- **Main Branch**: Production-ready code with full pipeline
- **Develop Branch**: Staging environment with integration testing
- **Feature Branches**: Development features with basic validation

## Getting Started

1. Follow the [Jenkins Multibranch Pipeline Exercise](https://github.com/neerajk555/jenkins-multibranch-demo/blob/main/docs/jenkins-multibranch-pipeline-exercise.md)
2. Set up your Jenkins multibranch pipeline
3. Create feature branches and test the automated workflows

## Branch Strategy

- `main` → Production deployment (manual approval required)
- `develop` → Staging deployment (automatic)
- `feature/*` → Development builds (no deployment)

## Local Development

```bash
# Clone the repository
git clone https://github.com/neerajk555/jenkins-multibranch-demo.git
cd jenkins-multibranch-demo

# Build and test locally
mvn clean test
mvn package

# Run the application
java -jar target/simple-java-app-1.0-SNAPSHOT.jar
```

## Jenkins Pipeline Features

- ✅ Automatic branch discovery
- ✅ Branch-specific environment configurations
- ✅ Parallel testing (unit tests + code quality)
- ✅ Conditional deployments based on branch type
- ✅ Environment-specific artifact naming
- ✅ Cross-platform support (Windows/Linux)

## Requirements

- Java 17+
- Maven 3.6+
- Jenkins with Pipeline plugins
- Git

For detailed setup instructions, see the exercise documentation in the `docs/` folder.