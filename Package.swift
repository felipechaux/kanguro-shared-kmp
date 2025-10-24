// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "KanguroShared",
    platforms: [
        .iOS(.v16)
    ],
    products: [
        .library(
            name: "KanguroShared",
            targets: ["KanguroShared"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "KanguroShared",
            url: "https://github.com/kanguroseguro/kanguro-shared-kmp/releases/download/v1.0.0/KanguroShared.xcframework.zip",
            checksum: "placeholder-checksum-will-be-updated-by-ci"
        )
    ]
)