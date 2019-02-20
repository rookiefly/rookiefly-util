package com.rookiefly.commons.agent;

import java.lang.instrument.Instrumentation;

public class Main {
    public static void premain(String agentOps, Instrumentation inst) {
        inst.addTransformer(new NewTransformer());
    }
}