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


	InputStream Hello = getClass().getClassLoader().getResourceAsStream("samp1.sas7bdat");


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
        Record record = parser.parse();
        Assert.assertNotNull(record);    
        Assert.assertEquals("id::0", record.getHeader().getSourceId());   
        Assert.assertEquals("AAAA", record.get().getValueAsList().get(5).getValueAsString());
        Assert.assertEquals(1,Long.parseLong(parser.getOffset())); 
        record = parser.parse();
        Assert.assertNotNull(record);
    }

    
    @Test
    public void TestSASwithOffset() throws Exception {

        SasFileReader sasFileReader = new SasFileReaderImpl(Hello);
        DataParser parser = new SASDataParser(sasFileReader , getContext(),"id" , "5");
        Assert.assertEquals(5, Long.parseLong(parser.getOffset()));
        Record record = parser.parse();
        Assert.assertNotNull(record);
        Assert.assertEquals("ffffffff", record.get().getValueAsList().get(0).getValueAsString());   
        Assert.assertEquals("id::5", record.getHeader().getSourceId());       
        Assert.assertEquals(6,Long.parseLong(parser.getOffset()));  
        record = parser.parse();
        Assert.assertEquals(7, Long.parseLong(parser.getOffset()));
     
    }
    @Test
    public void TestSASLastOffset() throws Exception {

        SasFileReader sasFileReader = new SasFileReaderImpl(Hello);
        DataParser parser = new SASDataParser(sasFileReader , getContext(),"id" , "49");
        Assert.assertEquals(49, Long.parseLong(parser.getOffset()));
        Record record = parser.parse();
        Assert.assertNotNull(record);
        Assert.assertEquals("zzzzzzzz", record.get().getValueAsList().get(0).getValueAsString());   
        Assert.assertEquals("id::49", record.getHeader().getSourceId());       
        Assert.assertEquals(50,Long.parseLong(parser.getOffset()));  
        record = parser.parse();
        Assert.assertEquals(-1, Long.parseLong(parser.getOffset())); 
    }

    
    @Test
    public void testgetOffset() throws NumberFormatException, DataParserException, IOException {
    	SasFileReader sasFileReader = new SasFileReaderImpl(Hello); ;
        DataParser parser = new SASDataParser(sasFileReader , getContext(),"0" , "43");
        Assert.assertEquals(43, Long.parseLong(parser.getOffset()));  
    }
    
    @Test(expected = IOException.class)
    public void testClose() throws Exception {
        SasFileReader sasFileReader = new SasFileReaderImpl(Hello); ;
        DataParser parser = new SASDataParser(sasFileReader , getContext(),"0" , "0");

        parser.close();
        parser.parse();
    }

} 
