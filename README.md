# Summary
This repo demonstrates the optimization of some soj udf:

```SojNvl```
```soj_parse_clientinfo```
```is_decimal```

# Benchmark result

## SojNvl

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

Another finding is no benefit if using local cache:

The Fast Nvl acceleration is not significant when applying cache.

|Acceleration|Long32k|Short32k|
|------------|-------|--------|
|Cached      |15.64|2.64|
|NoCache     |18.92|7.8|

### Run

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

# soj_parse_clientinfo

Improvement is about 4.7X

|ThroughPut(MB/s)|10k|
|----------------|--|
|JDK regex       |1.54601E8|
|AC              |6.51477E8|
|ACWoCopy        |7.12955E8|


## Run
```
hongjizhang@hongjizhang-desktop:~/Work/OpenSource/SojOpt$ java -jar target/SojUdfPerf-1.0-SNAPSHOT-jar-with-dependencies.jar -s data/soj_parse_clientinfo_10k.txt -p data/soj_parse_clientinfo.pat -g
org.example.OrigClientInfoParser warm up takes 1572 (ms)
org.example.OrigClientInfoParser warm up takes 1572 (ms)
org.example.OrigClientInfoParser warm up takes 1678 (ms)
org.example.OrigClientInfoParser warm up takes 1727 (ms)

========Printing the results of org.example.OrigClientInfoParser ======
	0thd throughput: 1.49755E8 bytes/s
	1thd throughput: 1.58731E8 bytes/s
	2thd throughput: 1.59876E8 bytes/s
	3thd throughput: 1.50588E8 bytes/s
Overall throughput: 1.54601E8 bytes/s
Overall duration: 6304(ms) total strings 974609920 bytes
hongjizhang@hongjizhang-desktop:~/Work/OpenSource/SojOpt$ java -jar target/SojUdfPerf-1.0-SNAPSHOT-jar-with-dependencies.jar -s data/soj_parse_clientinfo_10k.txt -p data/soj_parse_clientinfo.pat -z
org.example.OptClientInfoParser warm up takes 682 (ms)
org.example.OptClientInfoParser warm up takes 685 (ms)
org.example.OptClientInfoParser warm up takes 686 (ms)
org.example.OptClientInfoParser warm up takes 713 (ms)

========Printing the results of org.example.OptClientInfoParser ======
	0thd throughput: 6.44583E8 bytes/s
	1thd throughput: 6.56745E8 bytes/s
	2thd throughput: 6.65717E8 bytes/s
	3thd throughput: 6.39507E8 bytes/s
Overall throughput: 6.51477E8 bytes/s
Overall duration: 1496(ms) total strings 974609920 bytes
hongjizhang@hongjizhang-desktop:~/Work/OpenSource/SojOpt$ java -jar target/SojUdfPerf-1.0-SNAPSHOT-jar-with-dependencies.jar -s data/soj_parse_clientinfo_10k.txt -p data/soj_parse_clientinfo.pat -x
org.example.OptClientInfoParser warm up takes 611 (ms)
org.example.OptClientInfoParser warm up takes 616 (ms)
org.example.OptClientInfoParser warm up takes 619 (ms)
org.example.OptClientInfoParser warm up takes 635 (ms)

========Printing the results of org.example.OptClientInfoParser ======
	0thd throughput: 7.10357E8 bytes/s
	1thd throughput: 7.20865E8 bytes/s
	2thd throughput: 7.2732E8 bytes/s
	3thd throughput: 6.94166E8 bytes/s
Overall throughput: 7.12955E8 bytes/s
Overall duration: 1367(ms) total strings 974609920 bytes
```

# is_decimal

Improvement is about 4.45X

|ThroughPut(MB/s)|10k|
|----------------|--|
|JDK regex       |1.54753E8|
|commons.lang3.isNumeric   |6.94242E8|

## Run

```
java -jar target/SojUdfPerf-1.0-SNAPSHOT-jar-with-dependencies.jar -n 3 -pr 18 -sc 0 -s data/isDecimal_test1.txt -i 10000 -onum
org.example.OrigIsDecimal warm up takes 91 (ms)
org.example.OrigIsDecimal warm up takes 92 (ms)
org.example.OrigIsDecimal warm up takes 91 (ms)
org.example.OrigIsDecimal warm up takes 95 (ms)

========Printing the results of org.example.OrigIsDecimal@723279cf ======
	0thd throughput: 1.57128E8 bytes/s
	1thd throughput: 1.57279E8 bytes/s
	2thd throughput: 1.526E8 bytes/s
	3thd throughput: 1.52155E8 bytes/s
Overall throughput: 1.54753E8 bytes/s
Overall duration: 16971(ms) total strings 2626320000 bytes

java -jar target/SojUdfPerf-1.0-SNAPSHOT-jar-with-dependencies.jar -n 3 -pr 18 -sc 0 -s data/isDecimal_test1.txt -i 10000 -fnum
org.example.FastIsDecimal warm up takes 49 (ms)
org.example.FastIsDecimal warm up takes 49 (ms)
org.example.FastIsDecimal warm up takes 49 (ms)
org.example.FastIsDecimal warm up takes 49 (ms)

========Printing the results of org.example.FastIsDecimal@723279cf ======
	0thd throughput: 7.06064E8 bytes/s
	1thd throughput: 6.84713E8 bytes/s
	2thd throughput: 6.93389E8 bytes/s
	3thd throughput: 6.93136E8 bytes/s
Overall throughput: 6.94242E8 bytes/s
Overall duration: 3783(ms) total strings 2626320000 bytes
```
