# Kanguro Shared KMP

Kotlin Multiplatform shared module for Kanguro iOS and Android applications.

## 📱 Platforms

- **Android** (API 24+)
- **iOS** (iOS 16.0+)

## 🛠 Architecture

This module provides shared business logic, UI components, and utilities using:

- **Kotlin Multiplatform** for cross-platform code sharing
- **Compose Multiplatform** for shared UI components
- **Ktor** for networking
- **Koin** for dependency injection
- **Moko Resources** for shared resources

## 📦 Integration

### Android Integration

Add to your `gradle.properties`:

```properties
kanguro.shared.version=1.0.0
gpr.user=your-github-username
gpr.key=your-github-token
```

Add to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        // ... other repositories
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/kanguroseguro/kanguro-shared-kmp")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

Add to your `build.gradle.kts`:

```kotlin
dependencies {
    val sharedVersion = project.findProperty("kanguro.shared.version") ?: "1.0.0"
    implementation("com.insurtech.kanguro:shared-kmp:$sharedVersion")
}
```

### iOS Integration

Add to your Xcode project through Swift Package Manager:

1. In Xcode, go to **File → Add Package Dependencies**
2. Enter: `https://github.com/kanguroseguro/kanguro-shared-kmp`
3. Select the version you want to use
4. Add to your target

Or add to your `Package.swift`:

```swift
dependencies: [
    .package(url: "https://github.com/kanguroseguro/kanguro-shared-kmp", from: "1.0.0")
]
```

## 🔄 Versioning

This project follows [Semantic Versioning](https://semver.org/):

- **MAJOR**: Incompatible API changes
- **MINOR**: Backwards-compatible functionality additions
- **PATCH**: Backwards-compatible bug fixes

### Version History

- `1.0.0` - Initial release with core functionality

## 🚀 Development

### Prerequisites

- JDK 17+
- Xcode 15+ (for iOS builds)
- Android SDK

### Building

```bash
# Run tests
./gradlew shared:test

# Build Android
./gradlew shared:assembleRelease

# Build iOS XCFramework
./gradlew shared:assembleXCFramework
```

### Creating a Release

1. Use the version bump script:
   ```bash
   ./scripts/bump-version.sh
   ```

2. Or manually create a release:
   ```bash
   ./scripts/create-release.sh 1.1.0
   ```

This will automatically:
- Update version in `gradle.properties`
- Create and push a git tag
- Trigger CI/CD to build and publish the release

## 🏗 CI/CD

The project uses GitHub Actions for:

- **Continuous Integration**: Build and test on every PR
- **Release Automation**: Automatically build and publish releases
- **XCFramework Distribution**: Generate and upload iOS frameworks
- **Package Publishing**: Publish to GitHub Packages

## 📄 License

Proprietary - Kanguro Seguros

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Add tests if applicable
4. Create a pull request

## 📞 Support

For questions or issues, contact the Kanguro development team.