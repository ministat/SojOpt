This repo demonstrates the Re2J benefit for SojNvl:

The following table shows the performance benefit for different input: 1k.txt, 2k.txt, 4k.txt, and 8k.txt. The acceleration is ~2X. It is collected with 4 threads.

|ThroughPut(bytes/s)|1k|2k|4k|8k|
|-------------------|--|--|--|--|
|JDK regex |48.46|53.72|52.3|53.08|
|Re2J regex|129  |128.6|133 |129.6|
|Acce%     |2.66 |2.39 |2.54|2.44 |

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

The throughput of Re2 is about ~2X of JDK regex.

Note:
JdkSojNvlImpl is the JDK regex, Re2JNvlImpl is google's Re2J.
