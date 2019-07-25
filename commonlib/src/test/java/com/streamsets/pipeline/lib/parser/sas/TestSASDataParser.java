package com.streamsets.pipeline.lib.parser.sas;

import com.epam.parso.SasFileReader;
import com.epam.parso.impl.SasFileReaderImpl;
import com.streamsets.pipeline.api.OnRecordError;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.Stage;
import com.streamsets.pipeline.lib.parser.DataParser;
import com.streamsets.pipeline.sdk.ContextInfoCreator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.io.FileInputStream;


import org.junit.Assert;
import org.junit.Test;

public class TestSASDataParser {

    private InputStream is = new FileInputStream("/Users/chitranair/Downloads/samp.sas7bdat");

    public TestSASDataParser() throws FileNotFoundException {
    }

    private Stage.Context getContext() {
        return ContextInfoCreator.createSourceContext("i", false, OnRecordError.TO_ERROR, Collections.emptyList());
    }


    @Test
    public void TestSASWithoutOffset() throws Exception {

        SasFileReader sasFileReader = new SasFileReaderImpl(is);
        DataParser parser = new SASDataParser(sasFileReader , getContext(),"id" , "0");

        Assert.assertEquals(0, Long.parseLong(parser.getOffset()));
        Record record = parser.parse();
        System.out.print(record);
        Assert.assertEquals(1, Long.parseLong(parser.getOffset()));
        Assert.assertEquals("id::0", record.getHeader().getSourceId());
        Assert.assertEquals("BILI", record.get().getValueAsList().get(7).getValueAsString());
        record = parser.parse();
        Assert.assertNull(record);
        Assert.assertEquals(-1, Long.parseLong(parser.getOffset()));
        parser.close();

    }


    @Test
    public void TestSASWithOffset() throws Exception {

        SasFileReader sasFileReader = new SasFileReaderImpl(is);
        DataParser parser = new SASDataParser(sasFileReader , getContext(),"id" , "99600");

        Assert.assertEquals(0, Long.parseLong(parser.getOffset()));
        System.out.println(parser.getOffset());
        Record record = parser.parse();
        System.out.print(record);
        Assert.assertEquals(99601, Long.parseLong(parser.getOffset()));
        Assert.assertEquals("id::99600", record.getHeader().getSourceId());
        Assert.assertEquals("AST", record.get().getValueAsList().get(7).getValueAsString());
        record = parser.parse();
        System.out.print(record);
        Assert.assertNull(record);
        Assert.assertEquals(-1, Long.parseLong(parser.getOffset()));
        parser.close();

    }

    @Test(expected = IOException.class)
    public void testClose() throws Exception {
        SasFileReader sasFileReader = new SasFileReaderImpl(is);
        DataParser parser = new SASDataParser(sasFileReader , getContext(),"0" , "0");

        parser.close();
        parser.parse();
    }

}
