# siever

Code and dataset for ASE 22 submission.

## Build:

Java running time: version 1.8.

Specific your java home to the variable `org.gradle.java.home` in file gradle.properties. 

```bash

./gradlew fatjar 

Generated running jar will be build/libs/Siever.jar
```

## Usage:

```
java -cp build/libs/Siever.jar Siever.Main -help

 -a,--after <arg>     Updated Version Number
 -b,--before <arg>    Original Version Number
 -n,--num             Number of run to perform
 -p,--project <arg>   Project Name
```

For example, to run junit project from junit-4.10.jar -> junit-4.11-beta-1.jar, following the order of Table 2 in the paper:
```
java -cp build/libs/Siever.jar Siever.Main -p junit -b 0 -a 1 -n 1

```
