package Siever;

public interface TestCase {
    public void initialize();

    public void runTest(String module, int i, int j);

    public void scc();
}
