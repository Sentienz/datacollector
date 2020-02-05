package com.streamsets.pipeline.lib.parser.sasxpt;

import com.streamsets.pipeline.api.OnRecordError;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.Stage;
import com.streamsets.pipeline.lib.parser.DataParser;
import com.streamsets.pipeline.lib.parser.DataParserFactory;
import com.streamsets.pipeline.lib.parser.DataParserFactoryBuilder;
import com.streamsets.pipeline.lib.parser.DataParserFormat;
import com.streamsets.pipeline.sdk.ContextInfoCreator;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Collections;

public class TestSASXPTParseFactory {

	private Stage.Context getContext() {
		return ContextInfoCreator.createSourceContext("i", false, OnRecordError.TO_ERROR, Collections.EMPTY_LIST);
	}

	InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.xpt");

	@Test
	public void testGetParser() throws Exception {
		DataParserFactoryBuilder dataParserFactoryBuilder = new DataParserFactoryBuilder(getContext(),
				DataParserFormat.SASXPT);
		DataParserFactory factory = dataParserFactoryBuilder.setMaxDataLen(1000).build();

		DataParser parser = factory.getParser("id", inputStream, "0");
		Assert.assertEquals(1, Long.parseLong(parser.getOffset()));
		Record record = parser.parse();
		Assert.assertNotNull(record);
		Assert.assertEquals("id::1", record.getHeader().getSourceId());
		Assert.assertEquals("Allanson, Andy", record.get().getValueAsList().get(0).getValueAsString());
		Assert.assertEquals(2, Long.parseLong(parser.getOffset()));
		Assert.assertTrue(0 < Long.parseLong(parser.getOffset()));
		parser.close();

	}

	@Test
	public void testGetParserWithOffset() throws Exception {
		DataParserFactoryBuilder dataParserFactoryBuilder = new DataParserFactoryBuilder(getContext(),
				DataParserFormat.SASXPT);
		DataParserFactory factory = dataParserFactoryBuilder.setMaxDataLen(1000).build();

		DataParser parser = factory.getParser("id", inputStream, "7");
		Assert.assertEquals(7, Long.parseLong(parser.getOffset()));
		Record record = parser.parse();
		Assert.assertNotNull(record);
		Assert.assertEquals("id::7", record.getHeader().getSourceId());
		Assert.assertEquals("Newman, Al", record.get().getValueAsList().get(0).getValueAsString());
		Assert.assertEquals(8, Long.parseLong(parser.getOffset()));
		Assert.assertTrue(0 < Long.parseLong(parser.getOffset()));
		Assert.assertNotNull(record);
		parser.close();

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCharacterBaseParserMethod() throws Exception {
		DataParserFactoryBuilder dataParserFactoryBuilder = new DataParserFactoryBuilder(getContext(),
				DataParserFormat.SASXPT);
		DataParserFactory factory = dataParserFactoryBuilder.setMaxDataLen(1000).build();
		factory.getParser("id", new StringReader(""), 0);
	}

}