package com.semperos.screwdriver.js;

import com.semperos.screwdriver.FileUtil;
import com.semperos.screwdriver.js.rhino.RhinoCompiler;
import com.semperos.screwdriver.js.rhino.RhinoEvaluatorException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Compile Dust templates to JavaScript via Rhino
 */
public class DustCompiler extends AbstractCompiler implements JsCompilation {

    public DustCompiler() {
        rhinoCompiler = new RhinoCompiler(new JsRuntimeSupport());
        HashMap<String,String> deps = new HashMap<String,String>();
        deps.put("jade.js", "com/semperos/screwdriver/js/vendor/dust-full-1.2.0.js");
        rhinoCompiler.addDependencies(deps);
    }

    @Override
    public String compile(File sourceFile) throws IOException, RhinoEvaluatorException {
        rhinoCompiler.registerCompiler("DustCompiler", "com/semperos/screwdriver/js/extension/compile-dust.js");
        rhinoCompiler.addSourceFilePath(sourceFile.getAbsolutePath());
        rhinoCompiler.addScriptSource(FileUtil.readFile(sourceFile));
        compilerOptions.put("dustRegistrationName", FilenameUtils.getBaseName(sourceFile.toString()));
        rhinoCompiler.addCompilerOptions(compilerOptions);
        return rhinoCompiler.compile();
    }
}
