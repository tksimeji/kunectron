# Kunectron

A Minecraft GUI framework

![Version](https://img.shields.io/badge/version-1.0.0--beta.8-blue?style=flat-square)
![Adapter](https://img.shields.io/badge/adapter-1.21.1--1.21.5-blue?style=flat-square)
![Licence](https://img.shields.io/badge/licence-MIT-blue?style=flat-square)

> [!WARNING]
> Currently in beta stage,
> unexpected issues may occur.

## Get started

### Dependency

Kunectron is published on Maven Central.
Add a dependency to your build tool of choice.

#### Groovy (Kotlin DSL)

```kotlin
dependencies {
    compileOnly("com.tksimeji:kunectron:x.y.z")
}
```

#### Groovy (Groovy DSL)

```groovy
dependencies {
    compileOnly 'com.tksimeji:kunectron:x.y.z'
}
```

#### Maven

```xml
<dependency>
    <groupId>com.tksimeji</groupId>
    <artifactId>kunectron</artifactId>
    <version>x.y.z</version>
    <scope>provided</scope>
</dependency>
```

### Plugin YML

Define a dependency on Kunectron in your Plugin YML.

#### Bukkit Plugin YML

```yaml
depend:
  - kunectron
```

#### Paper Plugin YML

```yaml
dependencies:
  server:
    kunectron:
      load: AFTER
```