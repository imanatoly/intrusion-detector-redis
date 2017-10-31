package com.booking.security.hackertest.util;

import static com.booking.security.hackertest.constants.Constants.FAILURE;
import static com.booking.security.hackertest.constants.Constants.SUCCESS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.util.Random;

public class LogFileGenerator {

    private static final String[] NAMES = {"James", "John", "Robert", "Michael", "William", "Marry",
            "Patricia", "Linda", "Barbara", "Elizabeth"};
    private static final String[] SURNAMES = {"Smith", "Jones", "Williams", "Taylor", "Brown",
            "Davies", "Evans", "Wilson", "Thomas", "Johnson"};

    private Random random = new Random();

    private String generateUsername() {
        return NAMES[random.nextInt(NAMES.length)] + "." + SURNAMES[random.nextInt(SURNAMES.length)];
    }

    private String generateIp() {
        return random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "."
                + random.nextInt(256);
    }


    public void generateFile(String filename, int lineCount) throws Exception {
        File file = new File(filename);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        for (int i = 0; i < lineCount; i++) {
            String logLine = String.valueOf(Instant.now().getEpochSecond()) + "," +
                    generateIp() + "," +
                    generateUsername() + "," +
                    (random.nextBoolean() ? SUCCESS : FAILURE);
            bufferedWriter.write(logLine);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        fileWriter.close();
    }
}
