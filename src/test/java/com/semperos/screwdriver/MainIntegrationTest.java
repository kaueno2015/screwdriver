package com.semperos.screwdriver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Integration test for application entry-point
 */
public class MainIntegrationTest {
    @Before
    public void cleanupBefore() throws Exception {
        TestUtil.deleteAssetDirectories();
    }
    @After
    public void cleanupAfter() throws Exception {
        TestUtil.deleteAssetDirectories();
    }

    @Test
    public void testMain() throws Exception {
        String[] args = { "-a", TestUtil.assetDirectoryPath(),
                "-o", TestUtil.outputDirectoryPath() };
        Main.main(args);
        LinkedList<File> jsFiles = (LinkedList<File>) FileUtils.listFiles(
                TestUtil.outputDirectory(),
                new RegexFileFilter(".*\\.js"),
                DirectoryFileFilter.DIRECTORY);
        LinkedList<File> cssFiles = (LinkedList<File>) FileUtils.listFiles(
                TestUtil.outputDirectory(),
                new RegexFileFilter(".*\\.css"),
                DirectoryFileFilter.DIRECTORY);
        LinkedList<File> imageFiles = (LinkedList<File>) FileUtils.listFiles(
                TestUtil.outputDirectory(),
                new RegexFileFilter(".*\\.png"),
                DirectoryFileFilter.DIRECTORY);
        assertEquals(5, jsFiles.size());
        assertEquals(5, cssFiles.size());
        assertEquals(1, imageFiles.size());
    }

    @Test
    public void testMainWithCssIncludes() throws Exception {
        String[] args = { "-a", TestUtil.assetDirectoryPath(),
                "-o", TestUtil.outputDirectoryPath(),
                "-icss", ".*?main\\.less"};
        Main.main(args);
        LinkedList<File> files = (LinkedList<File>) FileUtils.listFiles(TestUtil.outputDirectory(),
                new RegexFileFilter(".*\\.css"), DirectoryFileFilter.DIRECTORY);
        assertEquals(1, files.size());
        assertEquals("main.css", files.get(0).getName());
    }

    @Test
    public void testMainWithImageExcludes() throws Exception {
        String[] args = { "-a", TestUtil.assetDirectoryPath(),
                "-o", TestUtil.outputDirectoryPath(),
                "-eimage", ".*?screwdriver_icon\\.png" };
        Main.main(args);
        LinkedList<File> files = (LinkedList<File>) FileUtils.listFiles(
                TestUtil.outputDirectory(),
                new RegexFileFilter(".*\\.png"),
                DirectoryFileFilter.DIRECTORY);
        assertEquals(0, files.size());
    }

    @Test
    public void testMainWithJsOptimizations() throws Exception {
        String[] args = { "-a", TestUtil.assetDirectoryPath(),
                "-o", TestUtil.outputDirectoryPath(),
                "-ojs",
                "--rjs-modules", "common", "main" };
        Main.main(args);
        LinkedList<File> files = (LinkedList<File>) FileUtils.listFiles(
                new File(TestUtil.outputDirectoryPath(), "built/javascripts"),
                new RegexFileFilter(".*-built\\.js"),
                DirectoryFileFilter.DIRECTORY);
        assertEquals(2, files.size());
    }

}
