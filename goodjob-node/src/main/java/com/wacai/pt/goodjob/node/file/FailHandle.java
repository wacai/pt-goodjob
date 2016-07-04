package com.wacai.pt.goodjob.node.file;

import com.wacai.pt.goodjob.common.Constants;
import com.wacai.pt.goodjob.common.utils.JsonMapper;
import com.wacai.pt.goodjob.remote.bean.ExecuteResponse;
import com.wacai.pt.goodjob.remote.service.JobExecuteRespService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by xuanwu on 16/4/14.
 */
public class FailHandle {
    private static final Logger   logger     = LoggerFactory.getLogger(FailHandle.class);
    private JsonMapper            jsonMapper = JsonMapper.getNonNullBinder();

    @Resource
    private JobExecuteRespService jobExecuteRespService;

    public FailHandle() {
        Thread t = new Thread(new FailHandle.ProcessFile());
        t.setDaemon(true);
        t.start();
        logger.info("FailHandle Thread start.");
    }

    public void writerResponse(ExecuteResponse executeResponse) {
        try {
            String msg = jsonMapper.toJson(executeResponse);
            logger.info("=============writerResponse:{}", msg);
            String path = Constants.BASE_PATH + "/" + executeResponse.getJobId() + "_"
                          + System.currentTimeMillis() + ".gj";
            TextFileWriter textFileWriter = new TextFileWriter(path);
            textFileWriter.writeLineExcRewrit(msg);
            textFileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ProcessFile implements Runnable {

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000 * 60 * 1L);
                    String path = Constants.BASE_PATH;
                    File file = new File(path);
                    File[] childFiles = file.listFiles();
                    if (childFiles != null) {
                        for (File childFile : childFiles) {
                            if (childFile.getAbsolutePath().endsWith("gj")) {
                                TextFileReader textFileReader = null;
                                try {
                                    textFileReader = new TextFileReader(childFile.getAbsolutePath());
                                    ExecuteResponse executeResponse = jsonMapper.fromJson(
                                        textFileReader.readLine(), ExecuteResponse.class);
                                    logger.info("=============process failure response:{}",
                                        jsonMapper.toJson(executeResponse));
                                    jobExecuteRespService.executeResp(executeResponse);
                                    if (textFileReader != null) {
                                        textFileReader.close();
                                    }

                                    childFile.delete();
                                } catch (Throwable t) {
                                    logger.error("process file failure:", t);
                                }
                            }
                        }
                    }
                } catch (Throwable t) {
                    logger.error("send response failure:", t);
                }
            }
        }
    }
}
