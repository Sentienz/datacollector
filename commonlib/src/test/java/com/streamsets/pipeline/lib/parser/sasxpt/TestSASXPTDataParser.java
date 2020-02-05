
package com.streamsets.pipeline.lib.parser.sasxpt;

import com.epam.parso.SasFileReader;
import com.epam.parso.impl.SasFileReaderImpl;
import com.sentienz.sas.xpt.SASXportFileIterator;
import com.streamsets.pipeline.api.OnRecordError;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.Stage;
import com.streamsets.pipeline.api.ext.io.OverrunReader;
import com.streamsets.pipeline.api.ext.json.Mode;
import com.streamsets.pipeline.lib.parser.DataParser;
import com.streamsets.pipeline.lib.parser.DataParserException;
import com.streamsets.pipeline.lib.parser.json.JsonCharDataParser;
import com.streamsets.pipeline.lib.parser.sas.SASDataParser;
import com.streamsets.pipeline.sdk.ContextInfoCreator;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collections;

public class TestSASXPTDataParser {

	InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.xpt");

	private Stage.Context getContext() {
		return ContextInfoCreator.createSourceContext("i", false, OnRecordError.TO_ERROR, Collections.emptyList());
	}

	@Test
	public void TestSASXPT() throws Exception {

		SASXportFileIterator iterator = new SASXportFileIterator(inputStream, "windows-1252");
		DataParser parser = new SASXPTDataParser(iterator, getContext(), "id", "0");
		Assert.assertEquals(1, Long.parseLong(parser.getOffset()));
		Record record = parser.parse();
		System.out.println(record);
		Assert.assertNotNull(record);
		Assert.assertEquals("id::1", record.getHeader().getSourceId());
		Assert.assertEquals("Allanson, Andy", record.get().getValueAsList().get(0).getValueAsString());
		Assert.assertEquals(2, Long.parseLong(parser.getOffset()));
		record = parser.parse();
		Assert.assertNotNull(record);
		Assert.assertEquals(3, Long.parseLong(parser.getOffset()));
		parser.close();

	}

	@Test
	public void TestSASXPTWithOffset() throws Exception {

		SASXportFileIterator iterator = new SASXportFileIterator(inputStream);
		DataParser parser = new SASXPTDataParser(iterator, getContext(), "id", "7");
		Assert.assertEquals(7, Long.parseLong(parser.getOffset()));
		Record record = parser.parse();
		Assert.assertNotNull(record);
		Assert.assertEquals("id::7", record.getHeader().getSourceId());
		Assert.assertEquals("Newman, Al", record.get().getValueAsList().get(0).getValueAsString());
		Assert.assertEquals(8, Long.parseLong(parser.getOffset()));
		record = parser.parse();
		Assert.assertNotNull(record);
		Assert.assertEquals("Salazar, Argenis", record.get().getValueAsList().get(0).getValueAsString());
		Assert.assertEquals(9, Long.parseLong(parser.getOffset()));
		parser.close();

	}

	@Test
	public void TestSASXPTLastOffset() throws Exception {

		SASXportFileIterator iterator = new SASXportFileIterator(inputStream);
		DataParser parser = new SASXPTDataParser(iterator, getContext(), "id", "532");
		Assert.assertEquals(532, Long.parseLong(parser.getOffset()));
		Record record = parser.parse();
		Assert.assertNotNull(record);
		Assert.assertEquals("id::532", record.getHeader().getSourceId());
		Assert.assertEquals("", record.get().getValueAsList().get(3).getValueAsString());
		Assert.assertEquals(533, Long.parseLong(parser.getOffset()));
		record = parser.parse();
		Assert.assertNotNull(record);
		Assert.assertEquals("", record.get().getValueAsList().get(6).getValueAsString());
		Assert.assertEquals(534, Long.parseLong(parser.getOffset()));
		record = parser.parse();
		Assert.assertEquals(-1, Long.parseLong(parser.getOffset()));
		record = parser.parse();
		Assert.assertNull(record);
		parser.close();

	}

	@Test
	public void testgetOffset() throws Exception {
		SASXportFileIterator iterator = new SASXportFileIterator(inputStream);
		DataParser parser = new SASXPTDataParser(iterator, getContext(), "id", "43");
		Assert.assertEquals(43, Long.parseLong(parser.getOffset()));
	}

	@Test(expected = IOException.class)
	public void testClose() throws Exception {
		SASXportFileIterator iterator = new SASXportFileIterator(inputStream);
		DataParser parser = new SASXPTDataParser(iterator, getContext(), "id", "0");
		parser.close();
		parser.parse();
	}
}