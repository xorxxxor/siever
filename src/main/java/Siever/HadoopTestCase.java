package Siever;

import heros.test.IFDSReachingDefinitionsTest;
import heros.test.IFDSReachingDefinitionsTest.Result;
import heros.test.SCC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HadoopTestCase implements TestCase{

    String hadoopBase = "./benchmarks/";
    String hadoopMainClass = "org.apache.hadoop.service.launcher.ServiceLauncher";

    List<String> hadoopCommonMainClasses = new ArrayList<>();
    String testMethod = "";

    List<String> hadoopCommonVersions = new ArrayList<>();

    Map<String, Result> allClassesResults = new HashMap<>();

    int numOfrun;

    @Override
    public void initialize() {
        hadoopCommonVersions.add("hadoop-common-3.0.0.jar");
        hadoopCommonVersions.add("hadoop-common-3.0.1.jar");
        hadoopCommonVersions.add("hadoop-common-3.0.2.jar");
        hadoopCommonVersions.add("hadoop-common-3.0.3.jar");
        hadoopCommonVersions.add("hadoop-common-3.1.0.jar");
        hadoopCommonVersions.add("hadoop-common-3.1.1.jar");
        hadoopCommonVersions.add("hadoop-common-3.1.2.jar");
        hadoopCommonVersions.add("hadoop-common-3.1.3.jar");
        hadoopCommonVersions.add("hadoop-common-3.1.4.jar");
        hadoopCommonVersions.add("hadoop-common-3.2.0.jar");
        hadoopCommonVersions.add("hadoop-common-3.2.1.jar");
        hadoopCommonVersions.add("hadoop-common-3.2.2.jar");
        hadoopCommonVersions.add("hadoop-common-3.2.3.jar");
        hadoopCommonVersions.add("hadoop-common-3.3.0.jar");
        hadoopCommonVersions.add("hadoop-common-3.3.1.jar");
        hadoopCommonVersions.add("hadoop-common-3.3.2.jar");

        hadoopCommonMainClasses.add("org.apache.hadoop.service.launcher.ServiceLauncher");
        hadoopCommonMainClasses.add("org.apache.hadoop.io.MapFile");
        hadoopCommonMainClasses.add("org.apache.hadoop.io.file.tfile.TFile");
        hadoopCommonMainClasses.add("org.apache.hadoop.io.compress.CompressionCodecFactory");
        hadoopCommonMainClasses.add("org.apache.hadoop.security.UserGroupInformation");
        hadoopCommonMainClasses.add("org.apache.hadoop.security.KDiag");
        hadoopCommonMainClasses.add("org.apache.hadoop.security.alias.CredentialShell");
        hadoopCommonMainClasses.add("org.apache.hadoop.crypto.key.KeyShell");
        hadoopCommonMainClasses.add("org.apache.hadoop.fs.FsShell");
    }

    private void runTestOnHadoopCommon(String version1, String version2) {
        IFDSReachingDefinitionsTest ifdsReachingDefinitionsTest = new IFDSReachingDefinitionsTest(hadoopBase, hadoopMainClass, testMethod);
        ifdsReachingDefinitionsTest.checkVersion(version1, version2, numOfrun);
    }

    private void runTestOnHadoopCommon(String version1, String version2, String mainClass) {
        IFDSReachingDefinitionsTest ifdsReachingDefinitionsTest = new IFDSReachingDefinitionsTest(hadoopBase, mainClass, testMethod);
        Result result = ifdsReachingDefinitionsTest.checkVersion(version1, version2, numOfrun);
        allClassesResults.put(mainClass, result);
    }

    @Override
    public void runTest(String module, int i, int j) {
        String currentVersionName = "";
        String updateVersionName = "";
        if (module.startsWith("hadoop-common")) {
            currentVersionName = hadoopCommonVersions.get(i);
            updateVersionName = hadoopCommonVersions.get(j);
            for (String className : hadoopCommonMainClasses) {
                runTestOnHadoopCommon(currentVersionName, updateVersionName, className);
            }
        } else {
            throw new RuntimeException("WRONG MODULE IS PROVIDED!");
        }
        printResult(currentVersionName, updateVersionName);
    }

    public HadoopTestCase(int numOfrun) {
        this.numOfrun = numOfrun;
        initialize();
    }

    private void printResult(String currentVersion, String updateVersion) {
        System.out.println("Checked version " + currentVersion + " -> " + updateVersion + " ...");
        System.out.println("Copy following results to fill table ... ");
        System.out.println(updateVersion);
        for (String className : allClassesResults.keySet()) {
            Result result = allClassesResults.get(className);
            System.out.print(result.className);
            System.out.print(",");
            System.out.printf("%.2f", result.TotalnanoFullComputationTime / 1E9 / numOfrun);
            System.out.print(",");
            System.out.printf("%d", result.TotalfullnumOfESPEdgeOfFullComputation / numOfrun);
            System.out.print(",");
            System.out.printf("%d", result.TotalbaselinesizeOfNewEdges / numOfrun);
            System.out.print(",");
            System.out.printf("%d", result.TotalbaselinesizeofExpiredNodes / numOfrun);
            System.out.print(",");
            System.out.printf("%.2f", result.TotalbaselinenanoUpdateTime / 1E9/ numOfrun);
            System.out.print(",");
            System.out.printf("%d", result.TotalbaselinenumOfPathEdgePropagated / numOfrun);
            System.out.print(",");
            System.out.printf("%d", result.TotalbaselinenumOfESPEdgePropagated / numOfrun);
            System.out.print(",");
            System.out.printf("%.2f", result.TotalsievernanoUpdateTime / 1E9 / numOfrun);
            System.out.print(",");
            System.out.printf("%d", result.TotalsievernumOfPathEdgePropagated / numOfrun);
            System.out.print(",");
            System.out.printf("%d", result.TotalsievernumOfESPEdgePropagated / numOfrun);
            System.out.println();
        }
    }

    @Override
    public void scc() {
        for (String mainClass : hadoopCommonMainClasses) {
            SCC scc = new SCC(this.hadoopBase, this.hadoopBase + "/" + hadoopCommonVersions.get(hadoopCommonVersions.size() - 1),
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
