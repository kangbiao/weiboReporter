package org.kangbiao.weiboReporter.schduler;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by bradykang on 4/19/2017.
 * url队列管理器
 */
public class FileScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler, Closeable {
    private String filePath = System.getProperty("java.io.tmpdir");
    private String fileUrlAllName = ".urls.txt";
    private Task task;
    private String fileCursor = ".cursor.txt";
    private PrintWriter fileUrlWriter;
    private PrintWriter fileCursorWriter;
    private AtomicInteger cursor = new AtomicInteger();
    private AtomicBoolean inited = new AtomicBoolean(false);
    private BlockingQueue<Request> queue;
    private Set<String> urls;
    private ScheduledExecutorService flushThreadPool;

    public FileScheduler(String filePath) {
        if (!filePath.endsWith("/") && !filePath.endsWith("\\")) {
            filePath = filePath + "/";
        }

        this.filePath = filePath;
        this.initDuplicateRemover();
    }

    private void flush() {
        this.fileUrlWriter.flush();
        this.fileCursorWriter.flush();
    }

    private void init(Task task) {
        this.task = task;
        File file = new File(this.filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        this.readFile();
        this.initWriter();
        this.initFlushThread();
        this.inited.set(true);
        this.logger.info("init cache scheduler success");
    }

    private void initDuplicateRemover() {
        this.setDuplicateRemover(new DuplicateRemover() {
            public boolean isDuplicate(Request request, Task task) {
                if (!FileScheduler.this.inited.get()) {
                    FileScheduler.this.init(task);
                }

                return !FileScheduler.this.urls.add(request.getUrl());
            }

            public void resetDuplicateCheck(Task task) {
                FileScheduler.this.urls.clear();
            }

            public int getTotalRequestsCount(Task task) {
                return FileScheduler.this.urls.size();
            }
        });
    }

    private void initFlushThread() {
        this.flushThreadPool = Executors.newScheduledThreadPool(1);
        this.flushThreadPool.scheduleAtFixedRate(new Runnable() {
            public void run() {
                FileScheduler.this.flush();
            }
        }, 10L, 10L, TimeUnit.SECONDS);
    }

    private void initWriter() {
        try {
            this.fileUrlWriter = new PrintWriter(new FileWriter(this.getFileName(this.fileUrlAllName), true),true);
            this.fileCursorWriter = new PrintWriter(new FileWriter(this.getFileName(this.fileCursor), false),true);
        } catch (IOException var2) {
            throw new RuntimeException("init cache scheduler error", var2);
        }
    }

    private void readFile() {
        try {
            this.queue = new LinkedBlockingQueue<Request>();
            this.urls = new LinkedHashSet<String>();
            this.readCursorFile();
            this.readUrlFile();
        } catch (FileNotFoundException var2) {
            this.logger.info("init cache file " + this.getFileName(this.fileUrlAllName));
        } catch (IOException var3) {
            this.logger.error("init file error", var3);
        }

    }

    private void readUrlFile() throws IOException {
        BufferedReader fileUrlReader = null;

        try {
            fileUrlReader = new BufferedReader(new FileReader(this.getFileName(this.fileUrlAllName)));
            int lineReaded = 1;

            String line;
            while ((line = fileUrlReader.readLine()) != null) {
                String [] lineArr=line.split("  ");
                this.urls.add(lineArr[0].trim());
                if (lineReaded >this.cursor.get()) {
                    Request request=new Request(lineArr[0]);
                    request.setExtras(JSON.parseObject(lineArr[1]));
                    this.queue.add(request);
                }
                lineReaded++;
            }
        } finally {
            if (fileUrlReader != null) {
                IOUtils.closeQuietly(fileUrlReader);
            }

        }

    }

    private void readCursorFile() throws IOException {
        BufferedReader fileCursorReader = null;

        String line;
        try {
            for (fileCursorReader = new BufferedReader(new FileReader(this.getFileName(this.fileCursor))); (line = fileCursorReader.readLine()) != null; this.cursor = new AtomicInteger(NumberUtils.toInt(line))) {
                ;
            }
        } finally {
            if (fileCursorReader != null) {
                IOUtils.closeQuietly(fileCursorReader);
            }

        }

    }

    public void close() throws IOException {
        this.flushThreadPool.shutdown();
        this.fileUrlWriter.close();
        this.fileCursorWriter.close();
    }

    private String getFileName(String filename) {
        return this.filePath + this.task.getUUID() + filename;
    }

    protected void pushWhenNoDuplicate(Request request, Task task) {
        if (!this.inited.get()) {
            this.init(task);
        }

        this.queue.add(request);
        this.fileUrlWriter.println(request.getUrl()+"  "+ JSON.toJSON(request.getExtras()));
    }

    public synchronized Request poll(Task task) {
        if (!this.inited.get()) {
            this.init(task);
        }

        this.fileCursorWriter.println(this.cursor.incrementAndGet());
        return this.queue.poll();
    }

    public int getLeftRequestsCount(Task task) {
        return this.queue.size();
    }

    public int getTotalRequestsCount(Task task) {
        return this.getDuplicateRemover().getTotalRequestsCount(task);
    }
}
