/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package sun.juwin.core;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import sun.juwin.exception.BaseCodeMakerException;
import sun.juwin.core.handlers.*;
import sun.juwin.constant.CodeMakerConstant;

import java.util.*;

/**
 * @author sunqinwen
 * @version \: CodeMaker.java,v 0.1 2018-08-07 17:51
 */
public class CodeMaker {

    private VelocityEngine ve;

    private int handlerSize = 0;

    private int cursor = 0;

    private VelocityContext context = new VelocityContext();

    private List<Map<String, Object>> filePath = new ArrayList<>();

    private List<CodeMakerHandler> handlers = new ArrayList<CodeMakerHandler>();

    private CodeMaker() {
        String path = CodeMaker.class.getResource("").getPath();
        String temPath = path.substring(0, path.indexOf(CodeMakerConstant.PRO_NAME)) + CodeMakerConstant.TEMPLATE_PATH;
        this.ve = new VelocityEngine();
        Properties p = new Properties();
        p.put(Velocity.FILE_RESOURCE_LOADER_PATH, temPath);
        ve.init(p);
    }

    public static CodeMaker buildMaker() {
        return new CodeMaker();
    }

    public void setHandlers(Map<String, Object> vmParam){
        setHandlers(vmParam, new BaseHandler());
    }

    public void setHandlers(Map<String, Object> vmParam, CodeMakerHandler handler) {
        if (handler != null) {
            if (vmParam != null) {
                vmParam.forEach((k, v) -> {
                    if (!CodeMakerConstant.TARGET_FILE_NAME.equals(k) &&
                            !CodeMakerConstant.TARGET_FILE_PATH.equals(k) &&
                            !CodeMakerConstant.TARGET_VM_PATH.equals(k)) {
                        context.put(k, v);
                    }
                });
                handlers.add(handler);
                filePath.add(vmParam);
                handlerSize = handlers.size();
            }
        }
    }

    public void make() throws Exception {
        getNextCodeMakerHandler().makeCode(this);
    }

    public CodeMakerHandler getNextCodeMakerHandler() throws BaseCodeMakerException {
        if (handlers.size() == 0) {
            throw new BaseCodeMakerException("no handlers");
        }
        int p = cursor++;
        return this.handlers.get(p);
    }

    public VelocityEngine getVe() {
        return ve;
    }

    public int getHandlerSize() {
        return handlerSize;
    }

    public VelocityContext getContext() {
        return context;
    }

    public List<Map<String, Object>> getFilePath() {
        return filePath;
    }

    public int getCursor() {
        return cursor;
    }
}