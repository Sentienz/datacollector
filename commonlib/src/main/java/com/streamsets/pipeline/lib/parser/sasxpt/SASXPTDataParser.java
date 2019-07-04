package com.streamsets.pipeline.lib.parser.sasxpt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import com.sentienz.sas.xpt.SASXportFileIterator;
import com.sentienz.sas.xpt.XPTTypes.ReadStatVariable;
import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.ProtoConfigurableEntity;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.lib.parser.AbstractDataParser;

public class SASXPTDataParser extends AbstractDataParser {

    private final ProtoConfigurableEntity.Context context;
	private SASXportFileIterator sasXportFileIterator;
	private List<Field> headers;
	private boolean isClosed;
	private boolean alreadyParsed;
	private String id;
	private String offset;
	private long recordCount;
	private boolean eof;
	
	public SASXPTDataParser(SASXportFileIterator sasXportFileIterator, ProtoConfigurableEntity.Context context, String id,
			String offset) {
		this.sasXportFileIterator = sasXportFileIterator;
		this.context = context;
		this.id = id;
		this.offset = offset;
		this.recordCount = sasXportFileIterator.getRowCount();
	}
	
	@Override
	public Record parse() throws IOException {
		Record record = null;
		if (isClosed) {
			throw new IOException("The parser is closed");
		}
		record = updateRecordsWithHeader(record);
		alreadyParsed = true;

		if(record!=null) {
		  recordCount++;
		}
		return record;
	}
	
	@Override
	public String getOffset(){
	  return eof ? String.valueOf(-1) : Long.toString(sasXportFileIterator.getOffset());
	}
	
	@Override
	public void close() throws IOException {
		isClosed = true;
	}
	
	private Record updateRecordsWithHeader(Record record) throws IOException {
		
	    if(!sasXportFileIterator.hasNext()) {
	      eof = true;
	      return null;
        }
        
	    record = context.createRecord(id + "::" + recordCount);
	  
		List<String> rows = sasXportFileIterator.next();
		headers = new ArrayList<Field>();

		ReadStatVariable[] columnList = sasXportFileIterator.getMetaData().variables;
		for(ReadStatVariable col : columnList) {
			headers.add(Field.create(col.name));
		}
		
		LinkedHashMap<String, Field> listMap = new LinkedHashMap<>();
		for (int i = 0; i < columnList.length; i++) {
			String key;
			Field header = (headers != null) ? headers.get(i) : null;
			if (header != null) {
				key = header.getValueAsString();
			} else {
				key = Integer.toString(i);
			}
			listMap.put(key, Field.create(Field.Type.STRING, rows.get(i)));
		}
		
		record.set(Field.createListMap(listMap));
		return record;


	}	
	
}