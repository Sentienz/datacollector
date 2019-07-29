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
	private List<Field> column_types;
	private boolean isClosed;
	private boolean alreadyParsed = false;
	private String id;
	private String offset;
	private long recordCount;
	private boolean eof;
	long currentOffset;
	
	public SASXPTDataParser(SASXportFileIterator sasXportFileIterator, ProtoConfigurableEntity.Context context, String id,
			String offset) {
		this.sasXportFileIterator = sasXportFileIterator;
		this.context = context;
		this.id = id;
		this.offset = offset;
		this.recordCount = sasXportFileIterator.getRowCount();
		seekOffset();
	}
	
	@Override
	public Record parse() throws IOException {
		
		Record record = null;
		if (isClosed) {
			throw new IOException("The parser is closed");
		}
		record = updateRecordsWithHeader(record);
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
		 		
	    currentOffset = sasXportFileIterator.getOffset();
		if(!sasXportFileIterator.hasNext()) {
		      eof = true;
	        }
        
	    record = context.createRecord(id + "::" + currentOffset);
		List<String> rows = sasXportFileIterator.next();
		
		try {
			if(rows.size()==0 || rows==null) {
				eof = true;
			return null;
			}
		}
		catch(Exception e) {
			eof=true;
			return null;
			
		}
		headers = new ArrayList<Field>();
		column_types = new ArrayList<Field>();

		ReadStatVariable[] columnList = sasXportFileIterator.getMetaData().variables;
		for(ReadStatVariable col : columnList) {
			headers.add(Field.create(col.name));
			column_types.add(Field.create(col.type.toString().substring(14)));
		}
		
		LinkedHashMap<String, Field> listMap = new LinkedHashMap<>();
		for (int i = 0; i < columnList.length; i++) {
			String key;
			String column_type;
			Field header = (headers != null) ? headers.get(i) : null;
			if (header != null) {
				key = header.getValueAsString();
			} else {
				key = Integer.toString(i);
			}
			column_type = column_types.get(i).getValueAsString();
			listMap.put(key,Field.create(Field.Type.STRING,rows.get(i)));
			}
			record.set(Field.createListMap(listMap));
		return record;
	}

	private long getCurrentOffset(long offset, long currentOffset) {
		while(offset!=currentOffset){
			List<String> rows = sasXportFileIterator.next();
			currentOffset = sasXportFileIterator.getOffset();
	}
		return sasXportFileIterator.getOffset();
	}
	
	private void seekOffset() {
		int count = 0;
		int offset2=Integer.parseInt(offset);
	    while(count < offset2-1) {
	    	 List<String> rows = sasXportFileIterator.next();
	        count++;
	    }	
	}

}
