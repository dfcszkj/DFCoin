package com.hfxb.app.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 
 * <p>
 * GZIPResponseWrapper
 * </p>
 * Copyright(c) 2014 yispider. All rights reserved.
 * 
 * @version 1.0.0
 * @author yispider
 */
class GZIPResponseWrapper extends HttpServletResponseWrapper {

    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(GZIPResponseWrapper.class);
    /**
     * HttpServletResponse
     */
    protected HttpServletResponse origResponse = null;
    /**
     * ServletOutputStream
     */
    protected ServletOutputStream stream = null;
    /**
     * PrintWriter
     */
    protected PrintWriter writer = null;

    /**
     * 通过HttpServletResponse构造GZIPResponseWrapper
     * 
     * @param response
     *            HttpServletResponse
     */
    public GZIPResponseWrapper(HttpServletResponse response) {
        super(response);
        origResponse = response;
    }

    /**
     * 
     * 获取ServletOutputStream
     * 
     * @return ServletOutputStream
     * @throws IOException
     *             IO异常
     */
    public ServletOutputStream createOutputStream() throws IOException {
        return (new GZIPResponseStream(origResponse));
    }

    /**
     * 结束Response
     */
    public void finishResponse() {
        try {
            if (writer != null) {
                writer.close();
            } else {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 强制写出
     * 
     * @throws IOException
     *             IO异常
     */
    public void flushBuffer() throws IOException {
        stream.flush();
    }

    /**
     * 获取ServletOutputStream对象
     * 
     * @return ServletOutputStream对象
     * @throws IOException
     *             IO异常
     */
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called!");
        }

        if (stream == null) {
            stream = createOutputStream();
        }
        return (stream);
    }

    /**
     * 获得PrintWriter对象
     * 
     * @return PrintWriter对象
     * 
     * @throws IOException
     *             IO异常
     * 
     */
    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return (writer);
        }

        if (stream != null) {
            return null;
        }

        stream = createOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(stream, "UTF-8"));
        return (writer);
    }

    /**
     * 设置长度
     * 
     * @param length
     *            长度
     */
    public void setContentLength(int length) {
    }
}
