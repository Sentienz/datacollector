package com.streamsets.pipeline.lib.parser.sas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.parso.Column;
import com.epam.parso.SasFileReader;
import com.streamsets.pipeline.api.Field;
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
	private List<Field> headers;

	public SASDataParser(SasFileReader sasFileReader, ProtoConfigurableEntity.Context context, String id) {
		this.sasFileReader = sasFileReader;
		this.context = context;
		this.id = id;
	}

	@Override
	public Record parse() throws DataParserException, IOException {
		if (isClosed) {
			throw new IOException("The parser is closed");
		}
		if (!alreadyParsed) {
			Record record = context.createRecord(id);
			record = updateRecordsWithHeader(record);
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

	private Record updateRecordsWithHeader(Record record) throws IOException {
		List<Column> columnList = sasFileReader.getColumns();
		Object[] rows = sasFileReader.readNext();

		headers = new ArrayList<>();
		for (Column col : sasFileReader.getColumns()) {
			headers.add(Field.create(col.getName()));
		}

		LinkedHashMap<String, Field> listMap = new LinkedHashMap<>();
		for (int i = 0; i < columnList.size(); i++) {
			String key;
			Field header = (headers != null) ? headers.get(i) : null;
			if (header != null) {
				key = header.getValueAsString();
			} else {
				key = Integer.toString(i);
			}
			listMap.put(key, Field.create(Field.Type.STRING, rows[i]));
		}
		record.set(Field.createListMap(listMap));
		return record;
	}
}
