package us.msu.cse.repair.core.util.visitors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CMD {

    /**
     * Run the cmd in a new process
     * @param params: command line parameters
     * @return the output lines combining stdout and stderr
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<String> run(List<String> params, File dir) throws IOException, InterruptedException{
        ProcessBuilder builder = new ProcessBuilder(params);
        builder.redirectOutput();
        builder.redirectErrorStream(true);
        builder.directory(dir);

        java.lang.Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        List<String> lines = new ArrayList<>();
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        process.waitFor();

        in.close();

        return lines;
    }
}
