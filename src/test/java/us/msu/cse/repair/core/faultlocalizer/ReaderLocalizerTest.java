package us.msu.cse.repair.core.faultlocalizer;

import org.junit.Test;
import us.msu.cse.repair.config.ProjectConfig;
import us.msu.cse.repair.core.parser.LCNode;

import java.util.Map;

import static org.junit.Assert.*;

public class ReaderLocalizerTest {
    @Test
    public void testLoader_Math3() {
        ProjectConfig config = ProjectConfig.getInstance("math_3");
        ReaderLocalizer rl = new ReaderLocalizer(config, false);
        Map<LCNode, Double> result = rl.searchSuspicious(0);
        assertEquals(rl.getNegativeTests().size(), 1);
        assertTrue(rl.getNegativeTests().contains("org.apache.commons.math3.util.MathArraysTest#testLinearCombinationWithSingleElementArray"));
    }

    @Test
    public void testLoader_Math4() {
        ProjectConfig config = ProjectConfig.getInstance("math_4");
        ReaderLocalizer rl = new ReaderLocalizer(config, false);
        Map<LCNode, Double> result = rl.searchSuspicious(0);
        assertEquals(rl.getNegativeTests().size(), 2);
        assertTrue(rl.getNegativeTests().contains("org.apache.commons.math3.geometry.euclidean.threed.SubLineTest#testIntersectionNotIntersecting"));
        assertTrue(rl.getNegativeTests().contains("org.apache.commons.math3.geometry.euclidean.twod.SubLineTest#testIntersectionParallel"));
    }

    @Test
    public void testLoader_Closure1() {
        ProjectConfig config = ProjectConfig.getInstance("closure_1");
        ReaderLocalizer rl = new ReaderLocalizer(config, false);
        Map<LCNode, Double> result = rl.searchSuspicious(0);
        assertEquals(rl.getNegativeTests().size(), 8);
    }
}