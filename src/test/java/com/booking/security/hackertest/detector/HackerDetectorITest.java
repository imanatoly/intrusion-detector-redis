package com.booking.security.hackertest.detector;


import com.booking.security.hackertest.IntTestBase;
import com.booking.security.hackertest.util.LogFileGenerator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static com.booking.security.hackertest.TestConstants.LOG_FILE;
import static com.booking.security.hackertest.TestConstants.SAMPLE_LOG_LINE;
import static com.booking.security.hackertest.constants.Constants.MILISECONDS_IN_SECOND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HackerDetectorITest extends IntTestBase {

    private static final int LOG_LINE_COUNT = 1000;

    @Autowired
    private HackerDetector hackerDetector;

    @BeforeClass
    public static void initLogFile() throws Exception {
        File file = new File(LOG_FILE);
        if (!file.exists()) {
            LogFileGenerator logFileGenerator = new LogFileGenerator();
            logFileGenerator.generateFile(LOG_FILE, LOG_LINE_COUNT);
        }
    }

    @Test
    public void shouldProcessSuccessLogLine() {
        // given
        String logLine = SAMPLE_LOG_LINE;

        // when
        String result = hackerDetector.parseLogLine(logLine);

        // then
        assertEquals("", result);
    }

    @Test
    public void shouldProcessThousandsOfLoglinesInASecond() throws Exception {
        // given
        File file = new File(LOG_FILE);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String logline;
        List<String> logLines = new ArrayList<>(LOG_LINE_COUNT);

        while ((logline = bufferedReader.readLine()) != null) {
            logLines.add(logline);
        }

        // when
        long start = System.currentTimeMillis();
        logLines.parallelStream().forEach(line -> hackerDetector.parseLogLine(line));
        long last = System.currentTimeMillis() - start;

        // then
        System.out.println(logLines.size() + " elements processed in " + last + "ms.");
        System.out.println((float) logLines.size() / last * MILISECONDS_IN_SECOND + " elements processed in 1ms.");

        assertTrue("At least 4000 items processed in a second ", 4000 < (float) logLines.size() / last * MILISECONDS_IN_SECOND);
    }
}
