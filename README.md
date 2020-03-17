Build:
```
mvn clean package
```

Run:

```
cd data
tar zxvf 8k.tgz
cd ..
java -jar target/SojUdfPerf-1.0-SNAPSHOT-jar-with-dependencies.jar -p data/pat.txt -s data/8k.txt
```

The sample output:

```
org.example.JdkSojNvlImpl warm up takes 7659 (ms)
org.example.JdkSojNvlImpl warm up takes 7659 (ms)
org.example.JdkSojNvlImpl warm up takes 7659 (ms)
org.example.JdkSojNvlImpl warm up takes 7658 (ms)

========Printing the results of org.example.JdkSojNvlImpl ======
	0thd throughput: 6.9577E7 bytes/s
	1thd throughput: 6.9577E7 bytes/s
	2thd throughput: 6.9577E7 bytes/s
	3thd throughput: 6.9577E7 bytes/s
Overall throughput: 6.9577E7 bytes/s
Overall duration: 30972(ms) total strings 2154941440 bytes
org.example.Re2JNvlImpl warm up takes 3544 (ms)
org.example.Re2JNvlImpl warm up takes 3544 (ms)
org.example.Re2JNvlImpl warm up takes 3555 (ms)
org.example.Re2JNvlImpl warm up takes 3555 (ms)

========Printing the results of org.example.Re2JNvlImpl ======
	0thd throughput: 1.69307E8 bytes/s
	1thd throughput: 1.6968E8 bytes/s
	2thd throughput: 1.6968E8 bytes/s
	3thd throughput: 1.69307E8 bytes/s
Overall throughput: 1.69493E8 bytes/s
Overall duration: 12714(ms) total strings 2154941440 bytes
```

The throughput of Re2 is about 2X~4X of JDK regex.

Note:
JdkSojNvlImpl is the JDK regex, Re2JNvlImpl is google's Re2J.
