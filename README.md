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

## ⚙️ Configuration

### config.properties

```properties
# Browser Configuration
browser=chrome
base.url=https://automationexercise.com/

# Test Settings
explicit.wait.timeout=10
headless.mode=false
screenshot.on.failure=true

# Report Settings
report.path=test-output/ExtentReport.html
```

### testng.xml

```xml
<suite name="AutomationExercise Suite" verbose="2">
    <listeners>
        <listener class-name="org.example.listeners.TestListener"/>
    </listeners>

    <test name="Smoke Tests" preserve-order="true">
        <parameter name="browser" value="chrome"/>
        <groups>
            <run>
                <include name="smoke"/>
            </run>
        </groups>
        <classes>
            <class name="org.example.tests.SignupLoginTests"/>
            <class name="org.example.tests.HomeTests"/>
        </classes>
    </test>
</suite>
```

## 🏃‍♂️ Running Tests

### Quick Start

1. **Clone and setup**:
   ```bash
   git clone <repository-url>
   cd automationExercise
   mvn clean compile
   ```

2. **Run all tests**:
   ```bash
   mvn test
   ```

3. **Run specific test groups**:
   ```bash
   # Smoke tests only
   mvn test -Dgroups=smoke

   # Regression tests only
   mvn test -Dgroups=regression
   ```

4. **Run with specific browser**:
   ```bash
   mvn test -Dbrowser=chrome
   mvn test -Dbrowser=edge
   mvn test -Dbrowser=firefox
   ```

### Using the Batch Script (Windows)

```bash
# Run tests and generate/open Allure report
run-tests.bat
```

### Manual Allure Report Generation

```bash
# Generate Allure report
allure generate allure-results --clean --output allure-report

# Open report in browser
allure open allure-report

# Serve report on specific port
allure open allure-report --port 8080
```

## 📊 Allure Reporting Features

### Enhanced Labeling System

The framework automatically applies comprehensive Allure labels:

- **Suite**: "UI Tests" (fixed)
- **SubSuite**: Based on test groups (Smoke Tests, Regression Tests, Functional Tests)
- **Feature**: Test class name (e.g., "SignupLoginTests")
- **Tags**: All test groups applied to the test method

### Environment Information

Automatically generated `allure-results/environment.properties`:

```properties
Environment=QA
Framework=TestNG
Browser=Chrome
Base.URL=https://automationexercise.com/
Tester=YourUsername
OS=Windows 11
Java.Version=17.0.8
Headless.Mode=false
```

### Test Attachments

- **Screenshots**: Automatically captured on test failure (PNG format)
- **Test Logs**: Detailed execution logs for all test lifecycle events
- **Failure Details**: Complete exception information with stack traces
- **Execution Metadata**: Test duration, thread information, timestamps

## 🛠️ Key Components

### TestListener (Enhanced)

```java
public class TestListener implements ITestListener {
    // Comprehensive Allure integration
    // Automatic labeling based on groups
    // Screenshot attachment on failure
    // Detailed logging and attachments
}
```

### AllureEnvironmentUtils

```java
public class AllureEnvironmentUtils {
    // Generates environment.properties
    // Includes browser, framework, system info
    // Called automatically before test suite
}
```

### ScreenshotUtils

```java
public class ScreenshotUtils {
    // takeScreenshot() - Save to file with timestamp
    // takeScreenshotBytes() - Return byte[] for Allure
}
```

### DriverFactory (Thread-Safe)

```java
public class DriverFactory {
    // ThreadLocal WebDriver management
    // Automatic driver setup and cleanup
    // Cross-browser support
}
```

## 🧪 Writing Tests

### Basic Test Structure

```java
public class ExampleTests extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    public void exampleTest() {
        // Test implementation
        HomePage homePage = new HomePage();
        homePage.verifyHomePageLoaded();

        // Assertions
        Assert.assertTrue(homePage.isLogoDisplayed(), "Logo should be displayed");
    }
}
```

### Test Groups

- `smoke`: Critical functionality tests
- `regression`: Full test suite
- `functional`: Feature-specific tests

### Custom Groups

```java
@Test(groups = {"smoke", "priority:high", "module:authentication"})
public void loginTest() {
    // Test implementation
}
```

## 🔧 Maintenance & Cleanup

### Automatic Cleanup

The framework automatically:
- Clears old `allure-results/` before each test run
- Generates fresh environment properties
- Manages WebDriver instances per thread

### Manual Cleanup

```bash
# Clean Maven build
mvn clean

# Remove all generated reports
rm -rf allure-results/ allure-report/ test-output/

# Full reset
mvn clean && rm -rf allure-results/ allure-report/ test-output/
```

## 📈 Best Practices

### Test Organization
- Use descriptive test method names
- Apply appropriate test groups
- Include severity levels for critical tests
- Keep tests independent and isolated

### Page Object Model
- Separate page logic from test logic
- Use meaningful method names
- Implement proper waits and assertions
- Handle dynamic elements gracefully

### Configuration Management
- Use properties files for environment-specific settings
- Avoid hardcoding values in tests
- Parameterize browsers and test data

### Error Handling
- Let TestListener handle screenshot capture
- Use appropriate assertion messages
- Log important test steps
- Handle expected exceptions gracefully

## 🐛 Troubleshooting

### Common Issues

1. **WebDriver not found**:
   ```bash
   # Update WebDriverManager
   mvn dependency:purge-local-repository
   ```

2. **Allure report not generating**:
   ```bash
   # Install Allure CLI
   npm install -g allure-commandline

   # Check Allure version
   allure --version
   ```

3. **Tests failing due to timing**:
   - Increase `explicit.wait.timeout` in config.properties
   - Check for dynamic content loading

4. **Screenshot issues**:
   - Ensure browser window is not minimized
   - Check `screenshot.on.failure` setting

### Debug Mode

```bash
# Run with verbose output
mvn test -Dtestng.verbose=2

# Run single test class
mvn test -Dtest=SignupLoginTests

# Run single test method
mvn test -Dtest=SignupLoginTests#TC01_RegisterUser
```

## 📚 Additional Resources

- [TestNG Documentation](https://testng.org/doc/)
- [Selenium WebDriver](https://www.selenium.dev/documentation/)
- [Allure Framework](https://docs.qameta.io/allure/)
- [Page Object Model](https://martinfowler.com/bliki/PageObject.html)

## 🤝 Contributing

1. Follow the established coding standards
2. Add appropriate test groups and severity levels
3. Update documentation for new features
4. Ensure all tests pass before committing

---

**Framework Version**: 1.0.0
**TestNG Version**: 7.11.0
**Selenium Version**: 4.15.0
**Allure Version**: 2.24.0
