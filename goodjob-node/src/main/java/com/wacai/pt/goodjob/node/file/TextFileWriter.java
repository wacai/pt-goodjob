package com.wacai.pt.goodjob.node.file;

import com.wacai.pt.goodjob.common.Constants;

import java.io.*;

/**
 * Created by xuanwu on 16/4/14.
 */
public class TextFileWriter {
    //换行符
    private String lineSeparator;
    private Writer writer = null;
    private String filePath;

    /**
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    public TextFileWriter(String filePath) throws IOException {
        this(filePath, true);
    }

    public TextFileWriter(String filePath, boolean isAppend) throws IOException {
        this(filePath, isAppend, Constants.DEFAULT_ENCODE);
    }

    /**
     *
     * @param filePath 文件路径
     * @param isAppend 是否写入时追加
     * @param encode 编码
     * @throws IOException
     */
    public TextFileWriter(String filePath, boolean isAppend, String encode) throws IOException {
        //验证路径合法性
        verifyFilePath(filePath);
        this.lineSeparator = System.getProperty("line.separator");
        if (lineSeparator == null) {
            lineSeparator = "\n";
        }
        this.filePath = filePath;
        System.out.println("filePath:" + this.filePath);
        initWriter(filePath, isAppend, encode);
    }

    /**
     * 验证文件路径是否合法，并创建文件夹
     *
     * @param filePath
     */
    private void verifyFilePath(String filePath) throws IOException {
        try {
            //创建文件夹
            createFolder(filePath.split("(\\w+\\.){1,3}\\w{1,10}$")[0]);
        } catch (Exception e) {
            throw new IOException("file path is not legitimate.");
        }
    }

    /**
     * 创建多级目录
     *
     * @param path
     */
    private void createFolder(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();//mkdirs创建多级文件夹
        }
    }

    /**
     * 初始化Writer
     * @param filePath
     * @param isAppend
     * @throws IOException
     */
    private void initWriter(String filePath, boolean isAppend, String encode) throws IOException {
        OutputStreamWriter out = null;
        out = new OutputStreamWriter(new FileOutputStream(filePath, isAppend), encode);
        writer = new BufferedWriter(out);
    }

    //    public Writer getWriter() {
    //        return writer;
    //    }

    /**
     * 写一行
     * @param line
     * @param line
     * @throws IOException
     */
    public void writeLine(String line) throws IOException {
        writer.write(line);
        writer.write(lineSeparator);
    }

    /**
     * 写一行
     * 如果发生IO异常则关闭流，再开新流重新写入
     * @param line
     */
    public void writeLineExcRewrit(String line) {
        try {
            writer.write(line);
            writer.write(lineSeparator);
        } catch (IOException e) {//如果发生IO异常则关闭流，再开新流重新写入
            close();
            try {
                initWriter(this.filePath, true, Constants.DEFAULT_ENCODE);
                writer.write(line);
                writer.write(lineSeparator);
            } catch (IOException ie) {
                ie.printStackTrace();
            }

        }
    }

    /**
     * 一次性写入
     * @param content
     * @throws IOException
     */
    public void writeContent(String content) throws IOException {
        try {
            writer.write(content);
            writer.flush();
        } finally {
            close();
        }
    }

    public void flush() throws IOException {
        writer.flush();
    }

    public void close() {
        try {
            if (writer != null)
                writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
