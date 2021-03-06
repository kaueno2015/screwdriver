package com.semperos.screwdriver.pipeline;

import com.semperos.screwdriver.Config;

import java.io.File;

/**
 * Environment for managing paths to various assets to be processed
 * as part of a build.
 */
public class PipelineEnvironment {
    private JsAssetSpec jsAssetSpec;
    private JsSourceMapAssetSpec jsSourceMapAssetSpec;
    private CssAssetSpec cssAssetSpec;
    private ImageAssetSpec imageAssetSpec;
    private TemplateAssetSpec templateAssetSpec;
    private ServerTemplateAssetSpec serverTemplateAssetSpec;
    private StaticAssetSpec staticAssetSpec;

    public JsAssetSpec getJsAssetSpec() {
        return jsAssetSpec;
    }

    public void setJsAssetSpec(JsAssetSpec jsAssetSpec) {
        this.jsAssetSpec = jsAssetSpec;
    }

    public JsSourceMapAssetSpec getJsSourceMapAssetSpec() {
        return jsSourceMapAssetSpec;
    }

    public void setJsSourceMapAssetSpec(JsSourceMapAssetSpec jsSourceMapAssetSpec) {
        this.jsSourceMapAssetSpec = jsSourceMapAssetSpec;
    }

    public CssAssetSpec getCssAssetSpec() {
        return cssAssetSpec;
    }

    public void setCssAssetSpec(CssAssetSpec cssAssetSpec) {
        this.cssAssetSpec = cssAssetSpec;
    }

    public ImageAssetSpec getImageAssetSpec() {
        return imageAssetSpec;
    }

    public void setImageAssetSpec(ImageAssetSpec imageAssetSpec) {
        this.imageAssetSpec = imageAssetSpec;
    }

    public TemplateAssetSpec getTemplateAssetSpec() {
        return templateAssetSpec;
    }

    public void setTemplateAssetSpec(TemplateAssetSpec templateAssetSpec) {
        this.templateAssetSpec = templateAssetSpec;
    }

    public ServerTemplateAssetSpec getServerTemplateAssetSpec() {
        return serverTemplateAssetSpec;
    }

    public void setServerTemplateAssetSpec(ServerTemplateAssetSpec serverTemplateAssetSpec) {
        this.serverTemplateAssetSpec = serverTemplateAssetSpec;
    }

    public StaticAssetSpec getStaticAssetSpec() {
        return staticAssetSpec;
    }

    public void setStaticAssetSpec(StaticAssetSpec staticAssetSpec) {
        this.staticAssetSpec = staticAssetSpec;
    }

    /**
     * @todo Unhardcode as needed
     */
    public PipelineEnvironment(Config cfg) {
        File jsAssetPath = new File(cfg.getAssetDirectory(), cfg.getJsSubDirectoryName());
        File jsOutputPath = new File(cfg.getOutputDirectory(), cfg.getJsSubDirectoryName());
        jsAssetSpec = new JsAssetSpec(jsAssetPath, cfg.getJsExtensions(), jsOutputPath);
        if (cfg.getJsFileFilter() != null) {
            jsAssetSpec.setAssetFileFilter(cfg.getJsFileFilter());
        }
        if (cfg.getJsDirFilter() != null) {
            jsAssetSpec.setAssetDirFilter(cfg.getJsDirFilter());
        }
        if (cfg.getJsIncludes() != null && cfg.getJsIncludes().size() > 0) {
            jsAssetSpec.setAssetIncludes(cfg.getJsIncludes());
        } else {
            jsAssetSpec.setAssetExcludes(cfg.getJsExcludes());
        }
        if (cfg.isJsSourceMapsEnabled()) {
            jsSourceMapAssetSpec = new JsSourceMapAssetSpec(jsAssetPath, cfg.getJsExtensions(), jsOutputPath);
        }

        File cssAssetPath = new File(cfg.getAssetDirectory(), cfg.getCssSubDirectoryName());
        File cssOutputPath = new File(cfg.getOutputDirectory(), cfg.getCssSubDirectoryName());
        cssAssetSpec = new  CssAssetSpec(cssAssetPath, cfg.getCssExtensions(), cssOutputPath);
        if (cfg.getCssFileFilter() != null) {
            cssAssetSpec.setAssetFileFilter(cfg.getCssFileFilter());
        }
        if (cfg.getCssDirFilter() != null) {
            cssAssetSpec.setAssetDirFilter(cfg.getCssDirFilter());
        }
        if (cfg.getCssIncludes() != null && cfg.getCssIncludes().size() > 0) {
            cssAssetSpec.setAssetIncludes(cfg.getCssIncludes());
        } else {
            cssAssetSpec.setAssetExcludes(cfg.getCssExcludes());
        }


        File imageAssetPath = new File(cfg.getAssetDirectory(), cfg.getImageSubDirectoryName());
        File imageOutputPath = new File(cfg.getOutputDirectory(), cfg.getImageSubDirectoryName());
        imageAssetSpec = new ImageAssetSpec(imageAssetPath, cfg.getImageExtensions(), imageOutputPath);
        if (cfg.getImageFileFilter() != null) {
            imageAssetSpec.setAssetFileFilter(cfg.getImageFileFilter());
        }
        if (cfg.getImageDirFilter() != null) {
            imageAssetSpec.setAssetDirFilter(cfg.getImageDirFilter());
        }
        if (cfg.getImageIncludes() != null && cfg.getImageIncludes().size() > 0) {
            imageAssetSpec.setAssetIncludes(cfg.getImageIncludes());
        } else {
            imageAssetSpec.setAssetExcludes(cfg.getImageExcludes());
        }

        File templateAssetPath = new File(cfg.getAssetDirectory(), cfg.getJsSubDirectoryName());
        File templateOutputPath = new File(cfg.getOutputDirectory(), cfg.getJsSubDirectoryName());
        templateAssetSpec= new TemplateAssetSpec(templateAssetPath, cfg.getTemplateExtensions(), templateOutputPath);
        if (cfg.getTemplateFileFilter() != null) {
            templateAssetSpec.setAssetFileFilter(cfg.getTemplateFileFilter());
        }
        if (cfg.getTemplateDirFilter() != null) {
            templateAssetSpec.setAssetDirFilter(cfg.getTemplateDirFilter());
        }
        if (cfg.getTemplateIncludes() != null && cfg.getTemplateIncludes().size() > 0) {
            templateAssetSpec.setAssetIncludes(cfg.getTemplateIncludes());
        } else {
            templateAssetSpec.setAssetExcludes(cfg.getTemplateExcludes());
        }

        File serverTemplateAssetPath = new File(cfg.getAssetDirectory(), cfg.getServerTemplateSubDirectoryName());
        File serverTemplateOutputPath = cfg.getOutputDirectory();
        serverTemplateAssetSpec = new ServerTemplateAssetSpec(serverTemplateAssetPath,
                cfg.getServerTemplateExtensions(),
                serverTemplateOutputPath);
        serverTemplateAssetSpec.setAssetLocals(cfg.getServerTemplateLocals());
        if (cfg.getServerTemplateFileFilter() != null) {
            serverTemplateAssetSpec.setAssetFileFilter(cfg.getServerTemplateFileFilter());
        }
        if (cfg.getServerTemplateDirFilter() != null) {
            serverTemplateAssetSpec.setAssetDirFilter(cfg.getServerTemplateDirFilter());
        }
        if (cfg.getServerTemplateIncludes() != null && cfg.getServerTemplateIncludes().size() > 0) {
            serverTemplateAssetSpec.setAssetIncludes(cfg.getServerTemplateIncludes());
        } else {
            serverTemplateAssetSpec.setAssetExcludes(cfg.getServerTemplateExcludes());
        }

        File staticAssetAssetPath = new File(cfg.getAssetDirectory(), cfg.getStaticAssetSubDirectoryName());
        File staticAssetOutputPath = new File(cfg.getOutputDirectory(), cfg.getStaticAssetSubDirectoryName());
        staticAssetSpec = new StaticAssetSpec(staticAssetAssetPath, staticAssetOutputPath);
        if (cfg.getStaticAssetFileFilter() != null) {
            staticAssetSpec.setAssetFileFilter(cfg.getStaticAssetFileFilter());
        }
        if (cfg.getStaticAssetDirFilter() != null) {
            staticAssetSpec.setAssetDirFilter(cfg.getStaticAssetDirFilter());
        }
        if (cfg.getStaticAssetIncludes() != null && cfg.getStaticAssetIncludes().size() > 0) {
            staticAssetSpec.setAssetIncludes(cfg.getStaticAssetIncludes());
        } else {
            staticAssetSpec.setAssetExcludes(cfg.getStaticAssetExcludes());
        }
    }

}
