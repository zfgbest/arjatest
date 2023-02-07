package us.msu.cse.repair.core.faultlocalizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;

import com.gzoltar.core.GZoltar;
import com.gzoltar.core.components.Statement;
import com.gzoltar.core.instr.testing.TestResult;

import us.msu.cse.repair.core.parser.LCNode;

public class GZoltarFaultLocalizer implements IFaultLocalizer, Serializable {
	Set<String> positiveTestMethods;
	Set<String> negativeTestMethods;

	Map<LCNode, Double> faultyLines;

	public GZoltarFaultLocalizer(Set<String> binJavaClasses, Set<String> binExecuteTestClasses, String binJavaDir,
			String binTestDir, Set<String> dependences) throws FileNotFoundException, IOException {

		String projLoc = new File("").getAbsolutePath();
		GZoltar gz = new GZoltar(projLoc);

		gz.getClasspaths().add(binJavaDir);
		gz.getClasspaths().add(binTestDir);

		if (dependences != null)
			gz.getClasspaths().addAll(dependences);

		for (String testClass : binExecuteTestClasses)
			gz.addTestToExecute(testClass);

		for (String javaClass : binJavaClasses)
			gz.addClassToInstrument(javaClass);

		gz.run();

		positiveTestMethods = new HashSet<String>();
		negativeTestMethods = new HashSet<String>();

		for (TestResult tr : gz.getTestResults()) {
			String testName = tr.getName();
			if (tr.wasSuccessful())
				positiveTestMethods.add(testName);
			else {
				String trace = tr.getTrace();
				if (trace.startsWith("java.lang.NoClassDefFoundError:")
						|| trace.startsWith("java.lang.ExceptionInInitializerError")
						|| trace.startsWith("java.lang.IllegalAccessError")
						|| trace.startsWith("java.lang.VerifyError")) {
					tr.setSuccessful(true);
					positiveTestMethods.add(testName);
				} else if (!tr.getName().startsWith("junit.framework"))
					negativeTestMethods.add(testName);
			}
		}

		faultyLines = new HashMap<LCNode, Double>();
		for (Statement gzoltarStatement : gz.getSuspiciousStatements()) {
			String className = gzoltarStatement.getMethod().getParent().getLabel();
			int lineNumber = gzoltarStatement.getLineNumber();

			double suspValue = gzoltarStatement.getSuspiciousness();

			LCNode lcNode = new LCNode(className, lineNumber);
			faultyLines.put(lcNode, suspValue);
		}
	}

	@Override
	public Map<LCNode, Double> searchSuspicious(double thr) {
		Map<LCNode, Double> partFaultyLines = new HashMap<LCNode, Double>();
		for (Map.Entry<LCNode, Double> entry : faultyLines.entrySet()) {
			if (entry.getValue() >= thr)
				partFaultyLines.put(entry.getKey(), entry.getValue());
		}
		return partFaultyLines;
	}

	@Override
	public Set<String> getPositiveTests() {
		return this.positiveTestMethods;
	}

	@Override
	public Set<String> getNegativeTests() {
		return this.negativeTestMethods;
	}
}
