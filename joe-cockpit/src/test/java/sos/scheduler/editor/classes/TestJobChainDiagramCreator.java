package sos.scheduler.editor.classes;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestJobChainDiagramCreator {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        JobChainDiagramCreator jobChainDiagramCreator = new JobChainDiagramCreator(new File("c:/temp/file.xml"), new File("c:/temp"));
        jobChainDiagramCreator.createGraphwizFile(true);
    }

}
