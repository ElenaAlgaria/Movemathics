<br />
<p align="center">
    <img src="src/main/resources/images/logo.png" alt="Logo" width="100" height="140">

  <h3 align="center"><strong>Bewegungslernspiel</strong></h3>

  <p align="center">
    FHNW - Projekt 1/2 <br />
    Studiengang ICompetence / Informatik 
    <br />
    <a href="https://www.cs.technik.fhnw.ch/confluence20/display/VT122007/IP12-20vt_Bewegungslernspiel"><strong>Confluence Dokumentation</strong></a>
    <br />
    <br />
    <!-- TODO Projekt Video einfügen -->
    <a href="https://github.com/othneildrew/Best-README-Template">View Demo</a>
    
  </p>
</p>


# MoveMathics

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>
 
#
 &nbsp;

<!-- About the Project -->
# About the Project

## Built With

* [JavaFX](https://openjfx.io)
* [Maven](https://maven.apache.org)
* [OpenCV](https://opencv.org)
* [Mqtt](https://www.hivemq.com)
* [Log4j](https://logging.apache.org/log4j/2.x)

&nbsp;
<!-- TABLE OF CONTENTS -->
# Getting Started

## Installation
To setup the project from scratch, you can use the [install script](https://gitlab.fhnw.ch/ip12-20vt/ip12-20vt_bewegungslernspiel/movemathics-installation) on GitLab.
After installing all dependencies, please follow the steps below.

1. Clone the repo
   ```sh
   git clone https://gitlab.fhnw.ch/ip12-20vt/ip12-20vt_bewegungslernspiel/movemathics.git
   ```
2. Create executable jar file
   ```sh
   mvn clean install
   ```
3. Run the application
   ```sh
   java -jar target/movemathics-1.0-SNAPSHOT.jar
   ```
For further installation instructions please refer the [Documentation](https://www.cs.technik.fhnw.ch/confluence20/display/VT122007/IP12-20vt_Bewegungslernspiel)

<!-- USAGE EXAMPLES -->
&nbsp;
# Usage

_For more examples, please refer to the [Documentation](https://www.cs.technik.fhnw.ch/confluence20/display/VT122007/IP12-20vt_Bewegungslernspiel)_


<!-- ROADMAP -->
&nbsp;
# Roadmap

See the [open issues](https://gitlab.fhnw.ch/ip12-20vt/ip12-20vt_bewegungslernspiel/movemathics/-/issues) for a list of proposed features (and known issues).


Project Link: [GitLab](https://gitlab.fhnw.ch/ip12-20vt/ip12-20vt_bewegungslernspiel/movemathics)


<!-- ACKNOWLEDGEMENTS -->
&nbsp;
# Acknowledgements

## Configuration
Use the [Color Filter](https://gitlab.fhnw.ch/bewegungslernspiel/opencv-color-filter) project to calibrate HSV values for the color detection.

Default threshold values (green) in PictureRecognition.java
```java
    Scalar lowGreen = new Scalar(47, 70, 52);
    Scalar upGreen = new Scalar(82, 212, 193);

```
## Java Doc
Generate documentation:
   ```sh
   mvn site
   ```

&nbsp;
# Troubleshooting
Use the log-files in the project log folder for troubleshooting.


&nbsp;
# Maintainers

* Geremia Waler
* Yanick Altwegg
* Raphael Sutter
* Elena Algaria 
* Zoe Vocat
* Schlatter Kevin
* Wyss Tobias
* Andri Wild