package us.msu.cse.repair.config;

import us.msu.cse.repair.core.util.visitors.CMD;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ProjectConfig {
    /** such as `chart_3`  */
    private String bugName;

    private String rootDir;
    private String srcJavaDir;
    private String binJavaDir;
    private String binTestDir;

    public ProjectConfig(String bugName, String rootDir, String srcJavaDir, String binJavaDir, String binTestDir) {
        this.bugName = bugName;
        this.rootDir = rootDir;
        this.srcJavaDir = srcJavaDir;
        this.binJavaDir = binJavaDir;
        this.binTestDir = binTestDir;
    }

    public String getBugName() {
        return bugName;
    }

    public void setBugName(String bugName) {
        this.bugName = bugName;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public String getSrcJavaDir() {
        return srcJavaDir;
    }

    public void setSrcJavaDir(String srcJavaDir) {
        this.srcJavaDir = srcJavaDir;
    }

    public String getBinJavaDir() {
        return binJavaDir;
    }

    public void setBinJavaDir(String binJavaDir) {
        this.binJavaDir = binJavaDir;
    }

    public String getBinTestDir() {
        return binTestDir;
    }

    public void setBinTestDir(String binTestDir) {
        this.binTestDir = binTestDir;
    }

    @Override
    public String toString() {
        return "ProjectConfig{" +
                "\n bugName='" + bugName + '\'' +
                "\n rootDir='" + rootDir + '\'' +
                "\n srcJavaDir='" + srcJavaDir + '\'' +
                "\n binJavaDir='" + binJavaDir + '\'' +
                "\n binTestDir='" + binTestDir + '\'' +
                "\n}";
    }

    public static ProjectConfig getInstance(String bugID) {

        String d4jRoot = System.getenv("D4JSrcRoot");
        assert d4jRoot != null: "The evn variable D4JSrcRoot is null!";
        assert new File(d4jRoot).exists(): d4jRoot;

        bugID = bugID.toLowerCase();
        String[] arr = bugID.split("_");
        assert arr.length == 2;
        String proj = arr[0];
        int id = Integer.valueOf(arr[1]);

        File rootFile = new File(d4jRoot + "/" + proj + "/" + bugID + "_buggy");
        assert rootFile.exists();
        String rootPath = rootFile.getAbsolutePath();

        String srcJavaDir = null, binJavaDir = null, binTestDir = null;
        switch (proj) {
            case "chart": {
                assert id >=1 && id <=26;
                srcJavaDir = rootPath + "/source/";
                binJavaDir = rootPath + "/build/";
                binTestDir = rootPath + "/build-tests/";
                break;
            }
            case "lang": {
                assert id >=1 && id <= 65;
                srcJavaDir = rootPath + "/src/main/java/";
                binJavaDir = rootPath + "/target/classes/";
                binTestDir = rootPath + "/target/tests/";
                break;
            }
            case "math": {
                assert id >=1 && id <= 106;
                if(id <= 84) {
                    srcJavaDir = rootPath + "/src/main/java/";
                } else {
                    srcJavaDir = rootPath + "/src/java/";
                }
                binJavaDir = rootPath + "/target/classes/";
                binTestDir = rootPath + "/target/test-classes/";
                break;
            }
            case "time": {
                assert id >=1 && id <= 27;
                srcJavaDir = rootPath + "/src/main/java/";
                if (id <= 11) {
                    binJavaDir = rootPath + "/target/classes/";
                    binTestDir = rootPath + "/target/test-classes/";
                } else {
                    binJavaDir = rootPath + "/build/classes/";
                    binTestDir = rootPath + "/build/tests/";
                }
                break;
            }
            case "closure": {
                assert id >=1 && id <= 133;
                srcJavaDir = rootPath + "/src/";
                binJavaDir = rootPath + "/build/classes/";
                binTestDir = rootPath + "/build/test/";
                break;
            }
            default:{
                throw new Error("ERROR PROJECT NAME: " + bugID);
            }
        }
        assert srcJavaDir != null && new File(srcJavaDir).exists();

        File srcFile = new File(srcJavaDir);
        File binFile = new File(binJavaDir);
        File testBinFile = new File(binTestDir);
        if(!binFile.exists() || !testBinFile.exists()) {
            List<String> params = new ArrayList<>();
            params.add("defects4j");
            params.add("compile");
            try {
                CMD.run(params, rootFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        assert srcFile.exists();
        assert binFile.exists();
        assert testBinFile.exists();
        return new ProjectConfig(bugID, rootFile.getAbsolutePath(), srcFile.getAbsolutePath(), binFile.getAbsolutePath(), testBinFile.getAbsolutePath());
    }
}
