
## Setup

`gradlew genVSCodeRuns`


## Build

To build the mod into `build/libs`

```
gradlew build
```

To start Minecraft with a test build:

```
gradlew runClient
gradlew runServer
```


## Troubleshooting

Make sure `%JAVA_HOME%` points to JDK 8 (x64), and any other versions of Java are disabled.

Sometimes the gradle daemon needs to be stopped, e.g. in order to delete the cache:

```
gradlew --stop
```

Refreshing dependencies can also sometimes help:

```
gradlew --refresh-dependencies
```

