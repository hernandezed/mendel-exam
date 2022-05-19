package com.edh.mendelexam.harness;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileReader {
    private FileReader() {
    }

    public static String read(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path))).replaceAll("\n", "");
    }
}
