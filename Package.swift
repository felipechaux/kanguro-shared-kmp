// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "SharedKMP",
    platforms: [
        .iOS(.v16)
    ],
    products: [
        .library(
            name: "SharedKMP",
            targets: ["SharedKMP"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "SharedKMP",
            url: "https://github.com/felipechaux/kanguro-shared-kmp/releases/download/v1.0.1/SharedKMP.xcframework.zip",
            checksum: "525cb802dc3abfc25b85827b16cbbb57e331f9ff3c50abf05a6f73f901be5eb9"
        )
    ]
)