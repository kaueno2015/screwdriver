package com.semperos.screwdriver.build;

import com.semperos.screwdriver.pipeline.ImageAssetSpec;

import java.io.File;
import java.io.IOException;

/**
 * Steps required to "build" an image.
 *
 * For now, images are just copied. However, we could also include tooling
 * for applying automatic optimizations/transformations to images, e.g., to
 * keep them at a certain size or quality level.
 */
public class BuildImage {
    ImageAssetSpec imageAssetSpec;
    public BuildImage(ImageAssetSpec imageAssetSpec) {
        this.imageAssetSpec = imageAssetSpec;
    }

    public void compile(File sourceFile) {
        // This is a stand-in for possible future functionality, e.g., auto-optimization
        // of images for the web
    }

    public void build(File sourceFile) throws IOException {
        BuildUtil.copyFile(sourceFile, imageAssetSpec.outputFile(sourceFile));
    }

    public void buildAll() throws IOException {
        for (File f : imageAssetSpec.findFiles()) {
            build(f);
        }
    }
}
