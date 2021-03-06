package com.semperos.screwdriver.pipeline;

import com.semperos.screwdriver.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Base representation of a type of asset in the pipeline.
 */
public class AssetSpec {
    private static Logger logger = Logger.getLogger(AssetSpec.class);
    private File assetPath;
    private List<String> assetExtensions;
    private IOFileFilter assetFileFilter;
    private IOFileFilter assetDirFilter;
    private List<String> assetIncludes;
    private List<String> assetExcludes;
    private Map<String,Object> assetLocals;
    private File outputPath;

    public AssetSpec(File assetPath, List<String> assetExtensions, File outputPath) {
        this(assetPath, assetExtensions, null, outputPath);
    }

    public AssetSpec(File assetPath, List<String> assetExtensions, List<String> assetIncludes, File outputPath) {
        this.assetPath = assetPath;
        this.assetExtensions = assetExtensions;
        this.assetIncludes = assetIncludes;
        this.outputPath = outputPath;
    }

    public IOFileFilter getAssetFileFilter() {
        return assetFileFilter;
    }

    public void setAssetFileFilter(IOFileFilter assetFileFilter) {
        this.assetFileFilter = assetFileFilter;
    }

    public IOFileFilter getAssetDirFilter() {
        return assetDirFilter;
    }

    public void setAssetDirFilter(IOFileFilter assetDirFilter) {
        this.assetDirFilter = assetDirFilter;
    }

    public List<String> getAssetIncludes() {
        return assetIncludes;
    }

    public void setAssetIncludes(List<String> assetIncludes) {
        this.assetIncludes = assetIncludes;
    }

    public List<String> getAssetExcludes() {
        return assetExcludes;
    }

    public void setAssetExcludes(List<String> assetExcludes) {
        this.assetExcludes = assetExcludes;
    }

    public File getAssetPath() {
        return assetPath;
    }

    public void setAssetPath(File assetPath) {
        this.assetPath = assetPath;
    }

    public List<String> getAssetExtensions() {
        return assetExtensions;
    }

    public void setAssetExtensions(List<String> assetExtensions) {
        this.assetExtensions = assetExtensions;
    }

    public Map<String, Object> getAssetLocals() {
        return assetLocals;
    }

    public void setAssetLocals(Map<String, Object> assetLocals) {
        this.assetLocals = assetLocals;
    }

    public File getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(File outputPath) {
        this.outputPath = outputPath;
    }

    protected IOFileFilter defaultFileFilter() {
        //  This was the default, being removed to widen the scope,
        //  since extension-based dispatch is implemented at the Build level
        //  anyway, to call the right Rhino-based compilers.
        //  return FileUtil.fileFilterForExtensions(this.getAssetExtensions());
        return FileFilterUtils.fileFileFilter();
    }

    protected IOFileFilter defaultDirectoryFilter() {
        return FileUtil.defaultDirectoryFilter();
    }

    /**
     * Retrieve all actual {@code File} objects for this asset type
     *
     * @return List of all assets of this type currently in filesystem
     */
    public List<File> findFiles() {
        File path = getAssetPath();
        if (!path.exists()) {
            throw new RuntimeException("One of the directories that Screwdriver expects to work with " +
                    "does not exist: " + path.getAbsolutePath());
        }
        List<File> assets = new ArrayList<File>();
        IOFileFilter fileFilter = activeAssetFileFilter();
        IOFileFilter dirFilter = activeAssetDirFilter();
        assets.addAll(FileUtils.listFiles(
                path,
                fileFilter,
                dirFilter));
        return assets;
    }

    /**
     * Taking into account all configuration
     * @return
     */
    public IOFileFilter activeAssetFileFilter() {
        IOFileFilter fileFilter;
        if (assetFileFilter != null) {
            fileFilter = assetFileFilter;
        } else if (assetIncludes != null && assetIncludes.size() > 0) {
            StringBuilder sb = new StringBuilder();
            buildRegexFilterPattern(sb, assetIncludes);
            fileFilter = new RegexFileFilter(sb.toString());
        } else if (assetExcludes != null && assetExcludes.size() > 0) {
            StringBuilder sb = new StringBuilder();
            buildRegexFilterPattern(sb, assetExcludes);
            fileFilter = new NotFileFilter(new RegexFileFilter(sb.toString()));
        }  else {
            fileFilter = defaultFileFilter();
        }
        return fileFilter;
    }

    public IOFileFilter activeAssetDirFilter() {
        IOFileFilter dirFilter;
        if (assetDirFilter != null) {
            dirFilter = assetDirFilter;
        } else {
            dirFilter = defaultDirectoryFilter();
        }
        return dirFilter;
    }

    private void buildRegexFilterPattern(StringBuilder sb, List<String> assetPatterns) {
        for (int i = 0; i < assetPatterns.size(); i++) {
            sb.append("(")
                    .append(assetPatterns.get(i).trim())
                    .append(")");
            if (i != assetPatterns.size() - 1) {
                sb.append("|");
            }
        }
    }

    public static String getRelevantPath(File baseThatDoesNotMatter, File baseForOutput, File sourceFile) {
        String fromBase = baseThatDoesNotMatter.getAbsolutePath();
        if (!fromBase.endsWith("/")) {
            fromBase += "/";
        }
        String toBase = baseForOutput.getAbsolutePath();
        if (!toBase.endsWith("/")) {
            toBase += "/";
        }
        String namePattern = sourceFile.getName() + "$";
        String relevantPath = sourceFile.getAbsolutePath().replace(fromBase, "");
        relevantPath = relevantPath.replaceAll(namePattern, "");
        return toBase + relevantPath;
    }

    public File outputFile(File sourceFile) {
        String targetName = outputFileName(sourceFile.getName());
        String path = getRelevantPath(assetPath, outputPath, sourceFile);
        return FileUtils.getFile(path, targetName);
    }

    /**
     * In the simplest case (copying), a target file name is the
     * same as the source file name. For compilation workflows,
     * this looks more like {@literal foo.coffee} to {@literal foo.js}.
     *
     * @param sourceFileName
     * @return
     */
    protected String outputFileName(String sourceFileName) {
        return sourceFileName;
    }
}
