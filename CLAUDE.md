# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

`meshware-common` is a Java utility library for the Meshware/joyrpc ecosystem. It provides reusable infrastructure: time wheel scheduling, event bus, retry policies, concurrency primitives, and reflection utilities. Published as `io.meshware:common` to Maven Central.

## Build Commands

```bash
./gradlew build          # compile + test + jar
./gradlew test           # run tests only (JUnit 5 Jupiter)
./gradlew publish        # publish to Sonatype OSSRH (requires MAVEN_USERNAME/MAVEN_PASSWORD)
```

Java 8+ required. CI uses JDK 11 Temurin.

## Architecture

Single-module Gradle project. All source under `io.meshware.common`:

- **`timer/`** — Hierarchical time wheel scheduler. `Timer` owns a `TimeWheel` (multi-level hash-wheel with `DelayQueue`-based slot expiration), a boss thread, and a worker pool. Supports `add()` (absolute) and `delay()` (relative). Default singleton via `Timer.timer()`.
- **`event/`** — Event bus using joyrpc SPI (`@Extensible`/`@Extension`). `EventBus` creates `Publisher` instances by group/name. `JEventBus` (in `event/jbus/`) is the concrete impl: each publisher has a single `Dispatcher` daemon thread polling a `LinkedBlockingQueue`.
- **`concurrent/`** — `Daemon` (daemon thread lifecycle), `NamedThreadFactory`, `Waiter` (Mutex/Sleep variants), `ExecutorServiceFactory`.
- **`retry/`** — `RetryPolicy` interface with `RetryNTimes` and `RetryLoops` execution utility.
- **`util/`** — `ClassUtils` (cached reflection with inner metadata classes ~2000 lines), `Futures` (CompletableFuture chaining/timeout), `Close`, `Shutdown`, `Resource`, `SuperIterator`.
- **`entity/`** — `AsyncResult`, `CommonResponse`.
- **`exception/`** — `CreationException`, `MethodOverloadException`, `ReflectionException`, `SystemException`.

## Key Patterns

- **SPI/Extension**: joyrpc `@Extensible` on interfaces, `@Extension` on implementations. Extension loading is name-based.
- **Concurrency**: Heavy use of `ConcurrentHashMap`, `AtomicLong`, `AtomicIntegerFieldUpdater`, double-checked locking for lazy init.
- **Lombok**: `@Slf4j`, `@Getter`/`@Setter` used throughout — Lombok annotation processing is configured in build.gradle.
- **No tests currently**: `src/test/java` exists but is empty. JUnit 5 is configured as the test framework.

## Dependencies

- `io.joyrpc:joyrpc-extension-core:1.4.7-RELEASE` — SPI framework (`@Extensible`, `@Extension`, `URLOption`)
- `org.projectlombok:lombok:1.18.24`
- `org.slf4j:slf4j-api:2.0.6`
