package org.example;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PerfTest {
    @Option(name="-s", aliases="--strFile", required=true, usage="Specify the input string file, every input string is in every single line.")
    private String inputStringFile;

    @Option(name="-p", aliases="--patFile", usage="Specify the pattern string file, each pattern is in every single line.")
    private String inputPatternFile;

    @Option(name="-i", aliases="--iterations", usage="Specify the iterations. Default is 10")
    private int iterations = 10;

    @Option(name="-c", aliases="--compareResult", usage="Verify the different implementation of UDF")
    private boolean compareResult;

    @Option(name="-t", aliases="--threads", usage="Specify the thread number, default is 4")
    private int threads = 4;

    @Option(name="-r", aliases="--re2j", usage="Only run regex through Re2j")
    private boolean onlyRe2j = false;

    @Option(name="-j", aliases="--jdk", usage="Only run regex through JDK")
    private boolean onlyJDK = false;

    @Option(name="-f", aliases="--fastnvl", usage="Only run fast NVL")
    private boolean onlyFast = false;

    @Option(name="-d", aliases="--dummyindexof", usage="Dummy test for indexOf")
    private boolean dummyIndexOf = false;

    @Option(name="-a", aliases="--usecache", usage="Use cache to accelerate process")
    private boolean useCache = false;

    @Option(name="-e", aliases="--extractvalue", usage="Extract value through nvl")
    private boolean extractValue = false;

    @Option(name="-onum", aliases="--originisnum", usage="Only run origin isNumerical")
    private boolean originIsNum = false;

    @Option(name="-fnum", aliases="--fastisnum", usage="Only run fast isNumerical")
    private boolean fastIsNum = false;

    @Option(name="-n", aliases="--sojtype", usage="nvl: 1, clientparser: 2, isdecimal: 3, isint: 4, isbigint: 5")
    private int nvl = 2;

    @Option(name="-g", aliases="--origparser", usage="Original client info parser")
    private boolean originClientParser = false;

    @Option(name="-z", aliases="--optparser", usage="Optimized client info parser")
    private boolean optClientParser = false;

    @Option(name="-x", aliases="--optparserimpr", usage="Optimized client info parser w/o copy")
    private boolean optImprClientParser = false;

    @Option(name="-pr", aliases="--precision", usage="Specify the precision for isDecimal, default is 18")
    private int precision = 18;

    @Option(name="-sc", aliases="--scale", usage="Specify the scale for isDecimal, default is 0")
    private int scale = 0;

    private boolean parseArgs(final String[] args) {
        final CmdLineParser parser = new CmdLineParser(this);
        if (args.length < 1) {
            parser.printUsage(System.out);
            System.exit(-1);
        }
        boolean ret = true;
        try {
            parser.parseArgument(args);
        } catch (CmdLineException clEx) {
            System.out.println("Error: failed to parse command-line opts: " + clEx);
            ret = false;
        }
        return ret;
    }

    private String[] readInputLines(String inputFileLines) {
        List<String> strList = new ArrayList<String>();
        try {
            FileInputStream fis = new FileInputStream(inputFileLines);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = br.readLine()) != null) {
                strList.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (strList.size() > 0) {
            String[] res = new String[strList.size()];
            strList.toArray(res);
            return res;
        } else {
            return null;
        }
    }

    public static void nvlOps(PerfTest inst, boolean useCache, String[] patterns, String[] values) {
        if (inst.onlyJDK && inst.onlyRe2j && inst.onlyFast) {
            System.out.println("onlyJDK, onlyRe2j and onlyFast are exclusive, you cannot run them together");
            System.exit(1);
        }
        if (inst.extractValue) {
            FastSojNvlImpl fast = new FastSojNvlImpl(false);
            StringBuilder sb = new StringBuilder();
            for (String p : patterns) {
                for (String v : values) {
                    String result = fast.getTagValue(v, p);
                    if (result != null) {
                        if (sb.length() > 0) {
                            sb.append('\n');
                        }
                        sb.append(result);
                    }
                }
            }
            System.out.println(sb.toString());
            return;
        }
        if (inst.compareResult) {
            Validate v = new Validate(patterns, values);
            if (v.compareResults()) {
                System.out.println("Results are equal for three SOJ implements: " +
                        JdkSojNvlImpl.class.getName() + " " +
                        Re2JNvlImpl.class.getName() + " " +
                        FastSojNvlImpl.class.getName());
            }
        } else if (inst.onlyJDK) {
            MultipleThreadingSoj mtsJdk = new MultipleThreadingSoj(inst.threads,
                    new SojNvlPerf(new JdkSojNvlImpl(useCache), patterns, values, inst.iterations));
            mtsJdk.RunAll();
        } else if (inst.onlyRe2j) {
            MultipleThreadingSoj re2j = new MultipleThreadingSoj(inst.threads,
                    new SojNvlPerf(new Re2JNvlImpl(useCache), patterns, values, inst.iterations));
            re2j.RunAll();
        } else if (inst.onlyFast) {
            FastSojNvlImpl fast = new FastSojNvlImpl(useCache);
            MultipleThreadingSoj fastSoj = new MultipleThreadingSoj(inst.threads,
                    new SojNvlPerf(fast, patterns, values, inst.iterations));
            fastSoj.RunAll();
            System.out.println("StartsWith: " + fast.startWithMatched() + " IndexOf: " + fast.indexOfMatched());
        } else if (inst.dummyIndexOf) {
            IndexOfImpl indexOf = new IndexOfImpl();
            MultipleThreadingSoj fastSoj = new MultipleThreadingSoj(inst.threads,
                    new SojNvlPerf(indexOf, patterns, values, inst.iterations));
            fastSoj.RunAll();
        } else {
            SojNvlPerf jdk = new SojNvlPerf(new JdkSojNvlImpl(useCache), patterns, values, inst.iterations);
            jdk.setRepeated(useCache ? 10 : 1);
            MultipleThreadingSoj mtsJdk = new MultipleThreadingSoj(inst.threads, jdk);
            mtsJdk.RunAll();

            SojNvlPerf re2 = new SojNvlPerf(new Re2JNvlImpl(useCache), patterns, values, inst.iterations);
            re2.setRepeated(useCache ? 10 : 1);
            MultipleThreadingSoj re2j = new MultipleThreadingSoj(inst.threads, re2);
            re2j.RunAll();

            SojNvlPerf fast = new SojNvlPerf(new FastSojNvlImpl(useCache), patterns, values, inst.iterations);
            fast.setRepeated(useCache ? 10 : 1);
            MultipleThreadingSoj fastSoj = new MultipleThreadingSoj(inst.threads, fast);
            fastSoj.RunAll();
        }
    }

    public static void clientInfoParser(PerfTest inst, boolean useCache, String[] patterns, String[] values) {
        if (inst.originClientParser) {
            MultipleThreadingSoj mtsJdk = new MultipleThreadingSoj(inst.threads,
                    new SojNvlPerf(new OrigClientInfoParser(), patterns, values, inst.iterations));
            mtsJdk.RunAll();
        } else if (inst.optClientParser) {
            MultipleThreadingSoj mtsJdk = new MultipleThreadingSoj(inst.threads,
                    new SojNvlPerf(new OptClientInfoParser(), patterns, values, inst.iterations));
            mtsJdk.RunAll();
        } else if (inst.optImprClientParser) {
            OptClientInfoParser opt = new OptClientInfoParser();
            opt.useSearchFromIndex(true);
            MultipleThreadingSoj mtsJdk = new MultipleThreadingSoj(inst.threads,
                    new SojNvlPerf(opt, patterns, values, inst.iterations));
            mtsJdk.RunAll();
        }
        if (inst.compareResult) {
            ClientInfoParserValidator v = new ClientInfoParserValidator(patterns, values);
            if (v.compareResults()) {
                System.out.println("Results are equal for three SOJ implements: " +
                        OrigClientInfoParser.class.getName() + " " +
                        OptClientInfoParser.class.getName() + " " +
                        OptClientInfoParser.class.getName() + "Impr");
            } else {
                System.out.println("Not equal");
            }
        }
    }

    public static void isnumerical(PerfTest inst, String[] values) {
        if (inst.compareResult) {
            IsNumericalValidator v = new IsNumericalValidator(values, inst.precision, inst.scale);
            if (v.compareResults()) {
                System.out.println("Results are equal for " +
                        OrigIsDecimal.class.getName() + " and " +
                        FastIsDecimal.class.getName());
            } else {
                System.out.println("Not equal");
            }
            return;
        }
        if (inst.originIsNum) {
            if (inst.precision > 0) {
                OrigIsDecimal orig = new OrigIsDecimal(inst.precision, inst.scale);
                MultipleThreadingIsNumerical ot =
                        new MultipleThreadingIsNumerical(inst.threads, new VerifyNumPerf(orig, values, inst.iterations));
                ot.RunAll();
            }
        } else if (inst.fastIsNum) {
            if (inst.precision > 0) {
                FastIsDecimal fast = new FastIsDecimal(inst.precision, inst.scale);
                MultipleThreadingIsNumerical ot =
                        new MultipleThreadingIsNumerical(inst.threads, new VerifyNumPerf(fast, values, inst.iterations));
                ot.RunAll();
            }
        }
    }
    public static void main(final String args[]) {
        final PerfTest inst = new PerfTest();
        if (!inst.parseArgs(args)) {
            return;
        }
        boolean useCache = inst.useCache;
        String[] patterns = null;
        String[] values = null;

        switch (inst.nvl) {
            case 1:
                patterns = inst.readInputLines(inst.inputPatternFile);
                values = inst.readInputLines(inst.inputStringFile);
                assert (patterns != null);
                assert (values != null);
                nvlOps(inst, useCache, patterns, values);
                break;
            case 2:
                patterns = inst.readInputLines(inst.inputPatternFile);
                values = inst.readInputLines(inst.inputStringFile);
                assert (patterns != null);
                assert (values != null);
                clientInfoParser(inst, useCache, patterns, values);
                break;
            case 3:
                values = inst.readInputLines(inst.inputStringFile);
                assert (values != null);
                isnumerical(inst, values);
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }
}
