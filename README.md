<br>
<p align="center">
<img src="http://img.benkeoghcgd.co.uk/Spigot/AxiusCoreHeader.png">
</p>
<br>
<p align="center">
    <a href="https://github.com/BenKeoghCGD/AxiusCore/issues"><img src="https://img.shields.io/github/issues/BenKeoghCGD/AxiusCore?style=for-the-badge"/></a>
    <img src="https://img.shields.io/github/forks/BenKeoghCGD/AxiusCore?style=for-the-badge"/>
    <img src="https://img.shields.io/github/stars/BenKeoghCGD/AxiusCore?style=for-the-badge"/>
    <img src="https://img.shields.io/github/license/BenKeoghCGD/AxiusCore?style=for-the-badge"/>
</p>
<p align="center">
    <a href="https://www.spigotmc.org/resources/axiuscore.102852/"><img src="https://pluginbadges.glitch.me/api/v1/dl/Spigot-%23ff9100.svg?spigot=axiuscore.102852&style=for-the-badge"/></a>
    <img src="https://img.shields.io/maven-metadata/v?label=MAVEN&metadataUrl=http%3A%2F%2Fapi.benkeoghcgd.co.uk%2Fmaven2%2Fuk%2Fco%2Fbenkeoghcgd%2Fapi%2FAxiusCore%2Fmaven-metadata.xml&style=for-the-badge"/>
    <img src="https://img.shields.io/spiget/version/102852?label=LATEST&style=for-the-badge"/>
</p>

# Welcome to AxiusCore
Hey! thanks for being interested in using AxiusCore. As of now, the core is in its early development, and therefore lacks
many of the intended launch features of the project.

## Features
- Data handling, with files stored as .yml files for easy configuration;
- Custom plugin framework, which breaks down the core functionality to 4 functions;
- Custom command handling / registration. Eliminating the need to add commands and permissions to the plugin.yml;
- GUI generation and manipulation classes;
- Player head auto-cache, used in unison with GUIs to provide next-level customisation;

## Installation
Navigate to the [AxiusCore spigot page](https://www.spigotmc.org/resources/axiuscore.102852/), or the [releases page on GitHub](https://github.com/BenKeoghCGD/AxiusCore/releases), and download the latest stable build of AxiusCore.
The core will be downloaded as a `.jar` file, and must be placed in the `plugins` folder of your server.

### Maven Repository
If you wish to use AxiusCore as the base of your plugin, add my maven repository to your project;

```xml
<repositories>
    <!-- This adds the AxiusCore Maven repository to the build -->
    <repository>
        <id>AxiusCore</id>
        <url>http://api.benkeoghcgd.co.uk/maven2/</url>
    </repository>
</repositories>

<!-- ... -->

<dependencies>
<!-- This adds the AxiusCore artifact to the build -->
    <dependency>
        <groupId>uk.co.benkeoghcgd.api</groupId>
        <artifactId>AxiusCore</artifactId>
        <version>${project.version}</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Non-Maven
What are you doing! maven

### Prerequisites
Make sure you're using `Java 17` and load your server as usual. Check console for any errors before developing with the downloaded build.
If all steps have been followed correctly, your server should boot up normally; This is when you can begin development.