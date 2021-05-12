package com.rookiefly.commons.threadlocal;

public final class TraceContext {

    private static final ThreadLocal<TraceContext> LOCAL = ThreadLocal.withInitial(TraceContext::new);

    private String caller;

    private String traceId;

    public static TraceContext getContext() {
        return LOCAL.get();
    }

    public static void clear() {
        LOCAL.remove();
    }

    private TraceContext() {
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}