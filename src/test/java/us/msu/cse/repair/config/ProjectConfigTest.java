package us.msu.cse.repair.config;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProjectConfigTest {

    @Test
    public void getInstance() {
        ProjectConfig config = ProjectConfig.getInstance("math_1");
        System.out.println(config.toString());
    }
}