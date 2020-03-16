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

    @Option(name="-i", aliases="--iterations", usage="Specify the iterations. Default is 1000")
    private int iterations = 1000;

    @Option(name="-c", aliases="--compareResult", usage="Verfiy the different implementation of UDF")
    private boolean compareResult;

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

        PerfEntry pe = new PerfEntry(patterns, values);

        if (inst.compareResult) {
            if (pe.compareResults()) {
                System.out.println("Results are equal for two SOJ implements: " + JdkSojNvlImpl.class.getName() + " " + Re2JNvlImpl.class.getName());
            }
        } else {
            pe.runPerf(new JdkSojNvlImpl());
            pe.runPerf(new Re2JNvlImpl());
        }
    }
}
