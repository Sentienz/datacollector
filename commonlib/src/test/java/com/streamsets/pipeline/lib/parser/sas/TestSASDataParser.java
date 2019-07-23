package com.streamsets.pipeline.lib.parser.sas;

import com.epam.parso.SasFileProperties;
import com.epam.parso.SasFileReader;
import com.epam.parso.impl.SasFileReaderImpl;
import com.sentienz.sas.xpt.SASXportFileIterator;
import com.streamsets.pipeline.api.OnRecordError;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.Stage;
import com.streamsets.pipeline.api.ext.json.Mode;
import com.streamsets.pipeline.lib.parser.DataParser;
import com.streamsets.pipeline.lib.parser.DataParserException;
import com.streamsets.pipeline.lib.parser.json.JsonCharDataParser;
import com.streamsets.pipeline.lib.parser.sasxpt.SASXPTDataParser;
import com.streamsets.pipeline.sdk.ContextInfoCreator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.io.FileInputStream;


import org.junit.Assert;
import org.junit.Test;

public class TestSASDataParser {

    private InputStream Hello = new FileInputStream("/Users/admin/desktop/samp1.sas7bdat");

    public TestSASDataParser() throws FileNotFoundException {
    }

    private Stage.Context getContext() {
        return ContextInfoCreator.createSourceContext("i", true, OnRecordError.TO_ERROR, Collections.emptyList());
    }

    @Test
    public void TestSAS() throws Exception {

        SasFileReader sasFileReader = new SasFileReaderImpl(Hello);
        DataParser parser = new SASDataParser(sasFileReader , getContext(),"id" , "0");
        Assert.assertEquals(0, Long.parseLong(parser.getOffset()));
        System.out.println(parser.getOffset() + "  STREAMSET OFFSET 1");

        Record record = parser.parse();
        Assert.assertNotNull(record);
        Assert.assertEquals(1,Long.parseLong(parser.getOffset()));     
        Assert.assertEquals("id::0", record.getHeader().getSourceId());       
        record = parser.parse();
        Assert.assertNull(record);
        Assert.assertEquals(-1, Long.parseLong(parser.getOffset()));
    }

    @Test
    public void TestSASwithOffset() throws Exception {

        SasFileReader sasFileReader = new SasFileReaderImpl(Hello);
        SASDataParser parser = new SASDataParser(sasFileReader , getContext(),"id" , "48");
        Assert.assertEquals(0, Long.parseLong(parser.getOffset()));
        System.out.println(parser.getOffset() + "  STREAMSET OFFSET 1");
        Record record = parser.parse();
        Assert.assertNotNull(record);
        Assert.assertEquals(49,Long.parseLong(parser.getOffset()));     
        Assert.assertEquals("id::48", record.getHeader().getSourceId());       
        record = parser.parse();
        Assert.assertNull(record);
        Assert.assertEquals(-1, Long.parseLong(parser.getOffset()));    
    }
    
    @Test
    public void testgetOffset() throws NumberFormatException, DataParserException, IOException {
    	SasFileReader sasFileReader = new SasFileReaderImpl(Hello); ;
        DataParser parser = new SASDataParser(sasFileReader , getContext(),"0" , "99600");
        Assert.assertEquals(0, Long.parseLong(parser.getOffset()));
    }
       
    @Test(expected = IOException.class)
    public void testClose() throws Exception {
        SasFileReader sasFileReader = new SasFileReaderImpl(Hello); ;
        DataParser parser = new SASDataParser(sasFileReader , getContext(),"0" , "0");

        parser.close();
        parser.parse();
    }

} 