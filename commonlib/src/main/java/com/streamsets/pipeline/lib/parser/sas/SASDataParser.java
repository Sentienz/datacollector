package com.streamsets.pipeline.lib.parser.sas;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.parso.SasFileReader;
import com.streamsets.pipeline.api.ProtoConfigurableEntity;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.lib.parser.AbstractDataParser;
import com.streamsets.pipeline.lib.parser.DataParserException;

public class SASDataParser extends AbstractDataParser {

	private static final Logger LOG = LoggerFactory.getLogger(SASDataParser.class);

	private final ProtoConfigurableEntity.Context context;
	private SasFileReader sasFileReader;
	private static final String OFFSET_MINUS_ONE = "-1";
	private static final String OFFSET_ZERO = "0";
	private boolean isClosed;
	private boolean alreadyParsed;
	private String id;

	public SASDataParser(SasFileReader sasFileReader, ProtoConfigurableEntity.Context context, String id) {
		this.sasFileReader = sasFileReader;
		this.context = context;
		this.id = id;
	}

	@Override
	public Record parse() throws IOException, DataParserException {
		LOG.debug("Test SAS - Inside parse()");
		if (isClosed) {
			throw new IOException("The parser is closed");
		}
		if (!alreadyParsed) {
			LOG.debug("Test SAS: alreadyparsed");
			// Object[] records = sasFileReader.readNext();
			Record record = context.createRecord(id);
			alreadyParsed = true;
			return record;
		}
		return null;
	}

	@Override
	public String getOffset() throws DataParserException, IOException {
		// Will return the offset -1 if already parsed else return 0.
		return (!alreadyParsed) ? OFFSET_ZERO : OFFSET_MINUS_ONE;
	}

	@Override
	public void close() throws IOException {
		isClosed = true;
	}

}
