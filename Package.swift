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
            checksum: "b950b77a3b2a88e93fb53b4a423d2891810fae820f3df4dca0ce867391bbca04"
        )
    ]
)