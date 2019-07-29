package com.streamsets.pipeline.lib.parser.sas;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.epam.parso.Column;
import com.epam.parso.SasFileProperties;
import com.epam.parso.SasFileReader;
import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.ProtoConfigurableEntity;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.lib.parser.AbstractDataParser;
import com.streamsets.pipeline.lib.parser.DataParserException;

public class SASDataParser extends AbstractDataParser {

	private final ProtoConfigurableEntity.Context context;
	private SasFileReader sasFileReader;
	private SasFileProperties sasFileProperties;
	private List<Field> headers;
	private static final String OFFSET_MINUS_ONE = "-1";
	private static final String OFFSET_ZERO = "0";
	private boolean isClosed;
	private boolean alreadyParsed = false;
	private String id;
	private int offset;
	private long recordCount;
	private boolean eof;
	int currentOffset;

	public SASDataParser(SasFileReader sasFileReader, ProtoConfigurableEntity.Context context, String id,
			String offset) throws DataParserException, IOException {
		this.sasFileReader = sasFileReader;
		this.context = context;
		this.id = id;
		this.offset = Integer.parseInt(offset);
		this.sasFileProperties = sasFileReader.getSasFileProperties();
		this.recordCount = sasFileProperties.getRowCount();
		seekOffset();
		}																																																																																	

	@Override
	public Record parse() throws IOException, DataParserException {	
		
		Record record = null;
		if(eof==true) {
			return null;
		}
		if (isClosed) {
			throw new IOException("The parser is closed");
		}
		record = updateRecordsWithHeader(record);
		return record;
	}

	@Override
	public void close() throws IOException {
		isClosed = true;
	}
	
	@Override
	public String getOffset() throws  IOException {
		  return eof ? String.valueOf(-1) : sasFileReader.getOffset().toString();
	}
	
	private Record updateRecordsWithHeader(Record record) throws IOException {
		currentOffset = Integer.valueOf(sasFileReader.getOffset());	
		record = context.createRecord(id + "::" + currentOffset);
		Object rows[] = sasFileReader.readNext();
		try {
			if(rows.length==0 || rows==null) {
				eof = true;
			return null;
			}
		}
		catch(Exception e) {
			eof=true;
			return null;
		}
		
		if(Integer.parseInt(getOffset())>recordCount) {
			eof=true;
		}
		
		List<Column> columnlist = sasFileReader.getColumns();
		headers = new ArrayList<Field>();
		for(Column col :sasFileReader.getColumns()) {
			headers.add(Field.create(col.getName()));
		}		
		
		LinkedHashMap<String,Field> listMap = new LinkedHashMap<String,Field>();
		for(int i = 0; i<columnlist.size();i++) {
			String key;
			Field header = (headers!=null)?headers.get(i):null;
			if(headers!=null) {
				key=header.getValueAsString();
			}
			else {
				key = Integer.toString(i);
			}
			listMap.put(key,Field.create(Field.Type.STRING,rows[i]));	
			}
		
		record.set(Field.createListMap(listMap));
		
	return record;
	}
	
	private void seekOffset() throws IOException ,DataParserException{
		int count = 0;
	    while(count < offset) {
	      if(count<recordCount) {
	    	 Object rows[] = sasFileReader.readNext();
	        count++;
	      } else {
	        break;
	      }
	    }
	}	
}
