This repo demonstrates the FastNvl, Re2J benefit for SojNvl:

The following table shows the performance benefit for different input: 1k.txt, 2k.txt, 4k.txt, 8k.txt, 16k.txt and 32k.txt The acceleration is 8X~22X. It is collected with 4 threads.

For long input strings (ubi table)

|ThroughPut(MB/s)|1k|2k|4k|8k|16k|32k|
|----------------|--|--|--|--|---|---|
|JDK regex       |50|53|52|52|52 |52 |
|Re2J regex     |165|164|152|140|128|126|
|Fast Nvl  |1116|1099|1089|1027|1022|984|
|Fast/JDK        |22|21|21|20|20|19|

For short input strings (access views table)

|ThroughPut(MB/s)|1k|2k|4k|8k|16k|32k|
|----------------|--|--|--|--|---|---|
|JDK regex       |27|33|32|20|31 |35 |
|Re2J regex     |22|18|12|24|22|17|
|Fast Nvl  |296|253|320|330|249|273|
|Fast/JDK        |11|8|10|17|8|8|

Build:
```
mvn clean package
```

Run:

```
cd data
tar zxvf long_32k.tgz
cd ..
java -jar target/SojUdfPerf-1.0-SNAPSHOT-jar-with-dependencies.jar -p data/pat.txt -s data/32k.txt
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
