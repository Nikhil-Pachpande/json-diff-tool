# JsonDiff
A Java utility package designed to compare two JSON objects and highlight the differences between them. It provides a detailed output of differences, including missing or extra fields, value mismatches, and supports comparison for nested objects and arrays. It has implementation for ANSI escape codes to colorize the output for better readability.
This library package is deployed using GitHub Actions and can be found at ```https://github.com/Nikhil-Pachpande/json-diff/packages/2346835```.

## Features
- Compares two JSON strings and identifies differences.
- Outputs a human-readable diff with color coding for mismatches, missing fields, and extra fields.
- Handles deeply nested JSON structures and arrays.
- Includes methods to check for equality and subset relationships between two JSON objects.
- A sample Main class is included to demonstrate the usage of the JsonDiff class.

## Installation & Usage
- To use the library in your project, add the following dependency in the pom.xml file of your project:
```
<dependency>
  <groupId>io.github</groupId>
  <artifactId>json-diff</artifactId>
  <version>1.1-SNAPSHOT</version>
</dependency>
```
- to build locally, run ```mvn clean install```
