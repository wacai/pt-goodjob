package com.wacai.pt.goodjob.node.file;

import com.wacai.pt.goodjob.common.Constants;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by xuanwu on 16/4/14.
 */
public class TextFileReader {
    private BufferedReader buffReader = null;

    public TextFileReader(String filePath) throws IOException {
        this(filePath, Constants.DEFAULT_ENCODE);
    }

    public TextFileReader(String filePath, String encode) throws IOException {
        buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encode));
    }

    public BufferedReader getBuffReader() {
        return buffReader;
    }

    public String readLine() throws IOException {
        return buffReader.readLine();
    }

    public void close() {
        try {
            if (buffReader != null)
                buffReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
