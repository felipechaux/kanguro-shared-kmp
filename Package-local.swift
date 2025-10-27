// swift-tools-version: 5.9
// Alternative Package.swift for local development
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
            path: "./shared/build/XCFrameworks/debug/SharedKMP.xcframework"
        )
    ]
)