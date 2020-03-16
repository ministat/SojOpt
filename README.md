Build:
```
mvn clean package
```

Run:

```
java -jar target/SojUdf-1.0-SNAPSHOT-jar-with-dependencies.jar 
```

JdkSojNvlImpl is the JDK regex, Re2JNvlImpl is google's Re2J. The perf benenfit is 30%~50%.
