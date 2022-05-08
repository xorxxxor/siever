package Siever;

import heros.test.IFDSReachingDefinitionsTest;
import heros.test.IFDSReachingDefinitionsTest.Result;
import heros.test.SCC;

import java.util.*;

public class IoTDBTestCase implements TestCase {

    String IoTDBServerBase = "./benchmarks/";
    String IoTDBServerMainClass = "org.apache.iotdb.db.service.IoTDB";
    String testMethod = "";

    String IoTDBClusterMainClass = "org.apache.iotdb.cluster.ClientMain";
    String IoTDBClusterBase = "/home/yongchao/Work/project/test_project/bin";

    List<String> serverMainClasses = new ArrayList<>();
    List<String> clusterMainClasses = new ArrayList<>();

    int numOfRun;
    List<String> IoTDBServerVersions = new ArrayList<>();
    List<String> IoTDBClusterVersions = new ArrayList<>();

    Map<String, IFDSReachingDefinitionsTest.Result> allClassesResults = new HashMap<>();

    @Override
    public void initialize() {
        numOfRun = 3;
        IoTDBServerVersions.add("iotdb-server-0.11.2.jar");
        IoTDBServerVersions.add("iotdb-server-0.11.3.jar");
        IoTDBServerVersions.add("iotdb-server-0.11.4.jar");
        IoTDBServerVersions.add("iotdb-server-0.12.0.jar");
        IoTDBServerVersions.add("iotdb-server-0.12.1.jar");
        IoTDBServerVersions.add("iotdb-server-0.12.2.jar");
        IoTDBServerVersions.add("iotdb-server-0.12.3.jar");
        IoTDBServerVersions.add("iotdb-server-0.12.4.jar");
        IoTDBServerVersions.add("iotdb-server-0.12.5.jar");
        IoTDBServerVersions.add("iotdb-server-0.13.1-SNAPSHOT.jar");
        IoTDBServerVersions.add("iotdb-server-0.14.0-SNAPSHOT.jar");

        serverMainClasses.add("org.apache.iotdb.db.service.IoTDB");
        serverMainClasses.add("org.apache.iotdb.db.tools.TsFileSketchTool");

        IoTDBClusterVersions.add("iotdb-cluster-0.12.0.jar");
        IoTDBClusterVersions.add("iotdb-cluster-0.12.1.jar");
        IoTDBClusterVersions.add("iotdb-cluster-0.12.2.jar");
        IoTDBClusterVersions.add("iotdb-cluster-0.12.3.jar");
        IoTDBClusterVersions.add("iotdb-cluster-0.12.4.jar");
        IoTDBClusterVersions.add("iotdb-cluster-0.12.5.jar");
        IoTDBClusterVersions.add("iotdb-cluster-0.13.1-SNAPSHOT.jar");
        IoTDBClusterVersions.add("iotdb-cluster-0.14.0-SNAPSHOT.jar");

        clusterMainClasses.add("org.apache.iotdb.cluster.ClientMain");
        clusterMainClasses.add("org.apache.iotdb.cluster.ClusterIoTDB");
        clusterMainClasses.add("org.apache.iotdb.cluster.ClusterMain");
    }

    public void runTestOnIoTDBServer(String version1, String version2, String mainClass) {
        IFDSReachingDefinitionsTest ifdsReachingDefinitionsTest = new IFDSReachingDefinitionsTest(IoTDBServerBase, mainClass, testMethod);
        Result result = ifdsReachingDefinitionsTest.checkVersion(version1, version2, numOfRun);
        allClassesResults.put(mainClass, result);
    }

    public void runTestOnToTDBCluster(String version1, String version2, String mainClass) {
        IFDSReachingDefinitionsTest ifdsReachingDefinitionsTest = new IFDSReachingDefinitionsTest(IoTDBClusterBase, mainClass, testMethod);
        Result result = ifdsReachingDefinitionsTest.checkVersion(version1, version2, numOfRun);
        allClassesResults.put(mainClass, result);
    }

    @Override
    public void runTest(String module, int version1, int version2) {
        String currentVersionName = "";
        String updateVersionName = "";

        if (module.startsWith("iotdb-server")) {
            currentVersionName = IoTDBServerVersions.get(version1);
            updateVersionName = IoTDBServerVersions.get(version2);
            for (String mainClass : serverMainClasses) {
                runTestOnIoTDBServer(currentVersionName, updateVersionName, mainClass);
            }
        } else if (module.startsWith("iotdb-cluster")) {
            currentVersionName = IoTDBServerVersions.get(version1);
            updateVersionName = IoTDBServerVersions.get(version2);
            for (String mainClass : serverMainClasses) {
                if (version2 <= 3) {
                    if (mainClass.equals("org.apache.iotdb.cluster.ClusterIoTDB")) {
                        continue;
                    }
                    runTestOnToTDBCluster(currentVersionName, updateVersionName, mainClass);
                } else {
                    if (mainClass.equals("org.apache.iotdb.cluster.ClusterMain")) {
                        continue;
                    }
                    runTestOnToTDBCluster(currentVersionName, updateVersionName, mainClass);
                }
            }
        } else {
            throw new RuntimeException("WRONG MODULE IS PROVIDED!");
        }

        printResult(currentVersionName, updateVersionName);
    }

    public IoTDBTestCase(int numOfRun) {
        this.numOfRun = numOfRun;
        initialize();
    }

    private void printResult(String currentVersion, String updateVersion) {
        System.out.println("Checked version " + currentVersion + " -> " + updateVersion + " ...");
        System.out.println("Copy following results to fill table ... ");
        System.out.println(updateVersion);
        for (String className : allClassesResults.keySet()) {
            Result result = allClassesResults.get(className);
            System.out.println(result.className);
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
            System.out.println();
        }
    }

    @Override
    public void scc() {
//        SCC scc = new SCC(this.IoTDBServerBase, this.IoTDBServerBase + "/" + IoTDBServerVersions.get(IoTDBServerVersions.size() - 1),
//                IoTDBServerMainClass);
//        System.out.println("#Class #Method");
//        System.out.print(IoTDBServerMainClass);
//        System.out.print(",");
//        System.out.print(scc.getNumOfClass());
//        System.out.print(",");
//        System.out.print(scc.getNumOfMethod());
//        System.out.println();

        SCC scc1 = new SCC(this.IoTDBClusterBase, this.IoTDBClusterBase + "/" + IoTDBClusterVersions.get(IoTDBClusterVersions.size() - 1),
                IoTDBClusterMainClass);
        System.out.println("#Class #Method");
        System.out.print(IoTDBClusterMainClass);
        System.out.print(",");
        System.out.print(scc1.getNumOfClass());
        System.out.print(",");
        System.out.print(scc1.getNumOfMethod());
        System.out.println();
    }
}


