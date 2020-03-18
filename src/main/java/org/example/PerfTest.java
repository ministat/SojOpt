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

    @Option(name="-p", aliases="--patFile", required=true, usage="Specify the pattern string file, each pattern is in every single line.")
    private String inputPatternFile;

    @Option(name="-i", aliases="--iterations", usage="Specify the iterations. Default is 10")
    private int iterations = 10;

    @Option(name="-c", aliases="--compareResult", usage="Verfiy the different implementation of UDF")
    private boolean compareResult;

    @Option(name="-t", aliases="--threads", usage="Specify the thread number, default is 4")
    private int threads = 4;

    @Option(name="-r", aliases="--re2j", usage="Only run regex through Re2j")
    private boolean onlyRe2j = false;

    @Option(name="-j", aliases="--jdk", usage="Only run regex through JDK")
    private boolean onlyJDK = false;

    @Option(name="-f", aliases="--fastnvl", usage="Only run fast NVL")
    private boolean onlyFast = false;

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

    public static void main(final String args[]) {
        final PerfTest inst = new PerfTest();
        if (!inst.parseArgs(args)) {
            return;
        }

        String[] patterns = inst.readInputLines(inst.inputPatternFile);
        String[] values = inst.readInputLines(inst.inputStringFile);
        assert (patterns != null);
        assert (values != null);

        if (inst.onlyJDK && inst.onlyRe2j && inst.onlyFast) {
            System.out.println("onlyJDK, onlyRe2j and onlyFast are exclusive, you cannot run them together");
            System.exit(1);
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
                        new SojNvlPerf(new JdkSojNvlImpl(), patterns, values, inst.iterations));
                mtsJdk.RunAll();
        } else if (inst.onlyRe2j) {
            MultipleThreadingSoj re2j = new MultipleThreadingSoj(inst.threads,
                    new SojNvlPerf(new Re2JNvlImpl(), patterns, values, inst.iterations));
            re2j.RunAll();
        } else if (inst.onlyFast) {
            FastSojNvlImpl fast = new FastSojNvlImpl();
            MultipleThreadingSoj fastSoj = new MultipleThreadingSoj(inst.threads,
                    new SojNvlPerf(fast, patterns, values, inst.iterations));
            fastSoj.RunAll();
            System.out.println("StartsWith: " + fast.startWithMatched() + " IndexOf: " + fast.indexOfMatched());
        } else {
                MultipleThreadingSoj mtsJdk = new MultipleThreadingSoj(inst.threads,
                        new SojNvlPerf(new JdkSojNvlImpl(), patterns, values, inst.iterations));
                mtsJdk.RunAll();
                MultipleThreadingSoj re2j = new MultipleThreadingSoj(inst.threads,
                        new SojNvlPerf(new Re2JNvlImpl(), patterns, values, inst.iterations));
                re2j.RunAll();
                MultipleThreadingSoj fastSoj = new MultipleThreadingSoj(inst.threads,
                        new SojNvlPerf(new FastSojNvlImpl(), patterns, values, inst.iterations));
                fastSoj.RunAll();
        }
    }
}
