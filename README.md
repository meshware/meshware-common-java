# meshware-common
[![Build](https://github.com/meshware/meshware-common-java/actions/workflows/gradle.yml/badge.svg)](https://github.com/meshware/meshware-common-java/actions/workflows/gradle.yml)
![License](https://img.shields.io/github/license/meshware/meshware-common-java.svg)
[![Maven Central](https://img.shields.io/maven-central/v/io.meshware/common.svg?label=maven%20central)](https://search.maven.org/search?q=g:io.meshware)
[![Percentage of issues still open](http://isitmaintained.com/badge/open/meshware/meshware-common-java.svg)](http://isitmaintained.com/project/meshware/meshware-common-java "Percentage of issues still open")

## Overview

A general-purpose common utility library for the Meshware ecosystem, referenced from [joyrpc](https://github.com/joyrpc/joyrpc). Provides reusable infrastructure components for building high-performance distributed systems.

### Modules

| Module | Package | Description |
|--------|---------|-------------|
| **Time Wheel** | `io.meshware.common.timer` | Hierarchical hash-wheel timer with `DelayQueue`-based slot expiration. Supports one-shot delay and periodic scheduling. |
| **Event Bus** | `io.meshware.common.event` | SPI-based event bus using joyrpc extension mechanism. Supports publish/subscribe with per-group dispatcher threads. |
| **Concurrency** | `io.meshware.common.concurrent` | Daemon thread lifecycle management, named thread factory, timed waiter abstractions, and executor service factory. |
| **Retry** | `io.meshware.common.retry` | Pluggable retry policy framework with configurable retry count and sleep interval. |
| **Utilities** | `io.meshware.common.util` | Cached reflection (`ClassUtils`), `CompletableFuture` helpers (`Futures`), safe close/destroy, shutdown hooks, resource loading. |
| **Entity** | `io.meshware.common.entity` | Async result wrapper and generic response entity. |
| **Exception** | `io.meshware.common.exception` | Domain exceptions: creation, reflection, method overload, and system-level errors. |

## How to Use

### Requirements

- JDK 8+
- Gradle 7+ (project uses Gradle Wrapper)

### Maven Dependency

```xml
<!-- https://mvnrepository.com/artifact/io.meshware/common -->
<dependency>
    <groupId>io.meshware</groupId>
    <artifactId>common</artifactId>
    <version>0.0.2</version>
</dependency>
```

### Gradle Dependency

```groovy
// https://mvnrepository.com/artifact/io.meshware/common
implementation 'io.meshware:common:0.0.2'
```

## Build

```bash
./gradlew build          # compile + test + package
./gradlew test           # run tests only
./gradlew javadocJar     # generate javadoc jar
```

## License

meshware-common is licensed under the [Apache License 2.0](./LICENSE).