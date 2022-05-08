package Siever;

import heros.test.IFDSReachingDefinitionsTest;

import heros.test.IFDSReachingDefinitionsTest.Result;
import heros.test.SCC;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JunitTestCase implements TestCase {

    String junitBase = "./benchmarks/";
    String junitMainClass = "org.junit.runner.JUnitCore";
    String testMethod = "runMainAndExit";

    List<String> versions = new ArrayList<>();

    List<String> mainClasses = new ArrayList<>();

    Map<String, Result> allClassesResults = new HashMap<>();
    int numOfRun;

    @Override
    public void initialize() {
        versions.add("junit-4.10.jar");
        versions.add("junit-4.11-beta-1.jar");
        versions.add("junit-4.11.jar");
        versions.add("junit-4.12-beta-1.jar");
        versions.add("junit-4.12-beta-2.jar");
        versions.add("junit-4.12-beta-3.jar");
        versions.add("junit-4.12.jar");
        versions.add("junit-4.13-beta-1.jar");
        versions.add("junit-4.13-beta-2.jar");
        versions.add("junit-4.13-beta-3.jar");
        versions.add("junit-4.13-rc-1.jar");
        versions.add("junit-4.13-rc-2.jar");
        versions.add("junit-4.13.jar");
        versions.add("junit-4.13.1.jar");
        versions.add("junit-4.13.2.jar");
        mainClasses.add("org.junit.runner.JUnitCore");
    }

    private void runTestOn(String version1, String version2, String mainClass) {
        IFDSReachingDefinitionsTest ifdsReachingDefinitionsTest = new IFDSReachingDefinitionsTest(junitBase, mainClass, testMethod);
        Result result = ifdsReachingDefinitionsTest.checkVersion(version1, version2, numOfRun);
        allClassesResults.put(mainClass, result);
    }

    @Override
    public void runTest(String module, int i, int j) {
        String currentVersionName = versions.get(i);
        String updateVersionName = versions.get(j);
        for (String mainClass : mainClasses) {
            runTestOn(currentVersionName, updateVersionName, mainClass);
        }
        printResult(currentVersionName, updateVersionName);
    }

    public JunitTestCase(int numOfRun) {
        this.numOfRun = numOfRun;
        initialize();
    }

    private void printResult(String currentVersion, String updateVersion) {
        System.out.println("Checked version " + currentVersion + " -> " + updateVersion + " ...");
        System.out.println("Copy following results to fill table ... ");
        System.out.println(updateVersion);
        for (String className : allClassesResults.keySet()) {
            IFDSReachingDefinitionsTest.Result result = allClassesResults.get(className);
            System.out.print(result.className);
            System.out.print(",");
            System.out.printf("%.2f", result.TotalnanoFullComputationTime / 1E9 / numOfRun);
            System.out.print(",");
            System.out.printf("%d", result.TotalfullnumOfESPEdgeOfFullComputation / numOfRun);
            System.out.print(",");
            System.out.printf("%d", result.TotalbaselinesizeOfNewEdges / numOfRun);
            System.out.print(",");
            System.out.printf("%d", result.TotalbaselinesizeofExpiredNodes / numOfRun);
            System.out.print(",");
            System.out.printf("%.2f", result.TotalbaselinenanoUpdateTime / 1E9/ numOfRun);
            System.out.print(",");
            System.out.printf("%d", result.TotalbaselinenumOfPathEdgePropagated / numOfRun);
            System.out.print(",");
            System.out.printf("%d", result.TotalbaselinenumOfESPEdgePropagated / numOfRun);
            System.out.print(",");
            System.out.printf("%.2f", result.TotalsievernanoUpdateTime / 1E9 / numOfRun);
            System.out.print(",");
            System.out.printf("%d", result.TotalsievernumOfPathEdgePropagated / numOfRun);
            System.out.print(",");
            System.out.printf("%d", result.TotalsievernumOfESPEdgePropagated / numOfRun);
            System.out.print(",");
            System.out.printf("%d", result.TotalNumResults);
            System.out.println();
        }
    }

    @Override
    public void scc() {
        for (String mainClass : mainClasses) {
            SCC scc = new SCC(this.junitBase, this.junitBase + "/" + versions.get(versions.size() - 1),
                    mainClass);
            System.out.println("#Class #Method");
            System.out.print(mainClass);
            System.out.print(",");
            System.out.print(scc.getNumOfClass());
            System.out.print(",");
            System.out.print(scc.getNumOfMethod());
            System.out.println();
        }
    }
}
