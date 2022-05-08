package Siever;

import org.apache.commons.cli.*;


public class Main {

    public static void main(String[] args) {
        Options options = new Options();

        Option project = new Option("p", "project", true, "Project Name");
        project.setRequired(true);
        options.addOption(project);

        Option before = new Option("b", "before", true, "Original Version Number");
        before.setRequired(true);
        options.addOption(before);

        Option after = new Option("a", "after", true, "Updated Version Number");
        after.setRequired(true);
        options.addOption(after);

        Option numOfRun = new Option("n", "num", false, "Number of run to perform");
        options.addOption(numOfRun);

        Option scc = new Option("scc", "Whether output of #Class and #Method");
        options.addOption(scc);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        int numOfPass = 3;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(-1);
        }

        String projectName = cmd.getOptionValue("project").toLowerCase();

        int originalNum = Integer.parseInt(cmd.getOptionValue("before"));
        int updatedNum = Integer.parseInt(cmd.getOptionValue("after"));

        if (originalNum >= updatedNum) {
            System.err.println("Original version number MUST be small than updated version number !");
            System.exit(-1);
        }

        if (cmd.hasOption("numOfRun")) {
            numOfPass = Integer.parseInt(cmd.getOptionValue("numOfRun"));
        }

        if (projectName.startsWith("junit")) {
            JunitTestCase junitTestCase = new JunitTestCase(numOfPass);
            if (cmd.hasOption("scc")) {
                junitTestCase.scc();
            } else {
                junitTestCase.runTest(projectName, originalNum, updatedNum);
            }
        } else if (projectName.startsWith("iotdb")) {
            IoTDBTestCase ioTDBTestCase = new IoTDBTestCase(numOfPass);
            if (cmd.hasOption("scc")) {
                ioTDBTestCase.scc();
            } else {
                ioTDBTestCase.runTest(projectName, originalNum, updatedNum);
            }
        } else if (projectName.startsWith("hadoop")) {
            HadoopTestCase hadoopTestCase = new HadoopTestCase(numOfPass);
            if (cmd.hasOption("scc")) {
                hadoopTestCase.scc();
            } else {
                hadoopTestCase.runTest(projectName, originalNum, updatedNum);
            }
        }
    }
}
