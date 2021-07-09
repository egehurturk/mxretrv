package org.mxretrv.utils;

import org.apache.commons.cli.*;

public class ArgumentParser {
    private final String[] args;
    private final CommandLine cmd;

    public ArgumentParser(String[] args) {
        this.args = args;
        Options options = prepareOptions();

        HelpFormatter formatter = new HelpFormatter();
        customizeHelpFormatter(formatter);

        CommandLineParser parser = new DefaultParser();
        cmd = parseCommandLine(parser, options, formatter);
    }

    private Options prepareOptions() {

        Options options = new Options();
        Option input = new Option("i", "input", true, "a text file containing domain names delimited with \"\\n\"");
        input.setRequired(true);
        input.setArgName("input_file");
        options.addOption(input);

        Option output = new Option("o", "output", true, "JSON file to output MX records for each domain name (otherwise stdout is used)");
        output.setRequired(false);
        output.setArgName("output_file");
        options.addOption(output);

        Option batch_size = new Option("b", "batch-size", true, "the number of domain names to process in each thread (if multithreading is not enabled, the value of this argument will be ignored)");
        batch_size.setRequired(false);
        batch_size.setArgName("size");
        options.addOption(batch_size);

        Option multi = new Option("m", "multi", false, "enable multithreading");
        multi.setRequired(false);
        options.addOption(multi);

        Option verbose = new Option("v", "verbose", false, "output extra information about the process");
        verbose.setRequired(false);
        options.addOption(verbose);
        return options;
    }

    private void customizeHelpFormatter(HelpFormatter help) {
        help.setOptionComparator(null);
        help.setWidth(140);
        help.setNewLine("\n\n");
    }

    private CommandLine parseCommandLine(CommandLineParser parser, Options options, HelpFormatter formatter) {
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            String header = "Save DNS MX records of domain names\n\n";
            String footer = "\nMade by Ege Hurturk";
            formatter.printHelp("mxretrv", header, options, footer, true);
            System.exit(1);
        }
        return cmd;
    }

    public String[] getArgs() { return this.args; }
    public String getInputArgument() { return cmd.getOptionValue("input"); }
    public String getOutputArgument() { return cmd.getOptionValue("output"); }
    public String getBatchArgument() { return cmd.getOptionValue("batch-size"); }
    public boolean getMultiArgument() { return cmd.hasOption("multi"); }
    public boolean getVerboseArgument() { return cmd.hasOption("verbose"); }


}
