package us.msu.cse.repair.core.faultlocalizer;

import us.msu.cse.repair.core.parser.LCNode;

import java.util.Map;
import java.util.Set;

public class ReaderLocalizer implements IFaultLocalizer{

    Set<String> positiveTestMethods;
    Set<String> negativeTestMethods;

    Map<LCNode, Double> faultyLines;

    @Override
    public Map<LCNode, Double> searchSuspicious(double thr) {
        return null;
    }

    @Override
    public Set<String> getPositiveTests() {
        return null;
    }

    @Override
    public Set<String> getNegativeTests() {
        return null;
    }
}
