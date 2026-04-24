# Automation Exercise - Enhanced TestNG Framework

A production-ready Selenium WebDriver automation framework with comprehensive Allure reporting and TestNG integration.

## 🚀 Features

- **Enhanced Allure Reporting**: Complete integration with detailed test labeling, screenshots, and environment information
- **TestNG Integration**: Robust test lifecycle management with custom listeners
- **Cross-Browser Support**: Chrome, Edge, and Firefox with automatic driver management
- **Thread-Safe Execution**: Parallel test execution support
- **Comprehensive Logging**: Log4j integration with detailed test execution logs
- **Screenshot Management**: Automatic failure screenshots with organized storage
- **Configuration Management**: Flexible properties-based configuration
- **Page Object Model**: Clean separation of test logic and page interactions

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Chrome/Edge/Firefox browser
- Allure CLI (for report generation)

### Installing Allure CLI

```bash
# Using npm
npm install -g allure-commandline

# Or using scoop (Windows)
scoop install allure

# Or download from https://github.com/allure-framework/allure2/releases
```

## 🏗️ Project Structure

```
automationExercise/
├── src/
│   ├── main/java/org/example/
│   │   ├── base/           # Base test classes
│   │   ├── driver/         # WebDriver management
│   │   ├── pages/          # Page Object Model classes
│   │   └── utils/          # Utility classes
│   └── test/java/org/example/
│       ├── base/           # Test base classes
│       ├── listeners/      # TestNG listeners
│       └── tests/          # Test classes
├── src/test/resources/      # Test configuration and data
├── allure-results/          # Allure test results (auto-generated)
├── allure-report/           # Generated Allure reports (auto-generated)
└── pom.xml                 # Maven configuration
```

