package com.streamsets.pipeline.lib.parser.sasxpt;

import com.sentienz.sas.xpt.SASXportFileIterator;
import com.streamsets.pipeline.api.OnRecordError;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.Stage;
import com.streamsets.pipeline.lib.parser.DataParser;
import com.streamsets.pipeline.sdk.ContextInfoCreator;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;


public class TestSASXPTDataParser {

    private Stage.Context getContext() {
        return ContextInfoCreator.createSourceContext("i", false, OnRecordError.TO_ERROR, Collections.emptyList());
    }

    @Test
    public void TestSASXPTWithoutOffset() throws Exception {

        SASXportFileIterator iterator = new SASXportFileIterator("/Users/chitranair/Downloads/data/test.xpt",0);
        DataParser parser = new SASXPTDataParser(iterator, getContext(), "id","0");

        Assert.assertEquals(1312, Long.parseLong(parser.getOffset()));
        Record record = parser.parse();
        System.out.print(record);
        Assert.assertNotNull(record);
        System.out.print(record);
        Assert.assertEquals("id::0", record.getHeader().getSourceId());
        Assert.assertEquals("Name", record.get().getValueAsList().get(0).getValueAsString());
        Assert.assertEquals(10, Long.parseLong(parser.getOffset()));
        record = parser.parse();
        Assert.assertNull(record);
        Assert.assertEquals(-1, Long.parseLong(parser.getOffset()));
        parser.close();

    }


    @Test
    public void TestSASXPTWithOffset() throws Exception {

        SASXportFileIterator iterator = new SASXportFileIterator("/Users/chitranair/Downloads/data/test.xpt",0);
        DataParser parser = new SASXPTDataParser(iterator, getContext(), "id","0");

        Assert.assertEquals(1312, Long.parseLong(parser.getOffset()));
        Record record = parser.parse();
        System.out.print(record);
        Assert.assertNotNull(record);
        System.out.print(record);
        Assert.assertEquals("id::0", record.getHeader().getSourceId());
        Assert.assertEquals("Name", record.get().getValueAsList().get(0).getValueAsString());
        Assert.assertEquals(10, Long.parseLong(parser.getOffset()));
        record = parser.parse();
        Assert.assertNull(record);
        Assert.assertEquals(-1, Long.parseLong(parser.getOffset()));
        parser.close();

    }

    @Test(expected = IOException.class)
    public void testClose() throws Exception {

        SASXportFileIterator iterator = new SASXportFileIterator("/Users/chitranair/Downloads/Test.xpt",0);
        DataParser parser = new SASXPTDataParser(iterator, getContext(), "id","0");
        parser.close();
        parser.parse();
    }



}
