package net.kkolyan.elements.engine.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author nplekhanov
 */
public class JsDemo {
    public static void main(String[] args) throws ScriptException {
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("rhino");
        scriptEngine.put("x", 15.6);
        scriptEngine.put("y", -75);

        Object result = scriptEngine.eval("Math.pow(x, y)");
        System.out.println(result+":"+result.getClass().getName());
    }
}
