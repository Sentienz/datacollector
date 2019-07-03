package com.streamsets.pipeline.lib.parser.sasxpt;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.epam.parso.SasFileReader;

import com.sentienz.sas.xpt.*;
//import com.epam.parso.impl.SasFileReaderImpl;
import com.streamsets.pipeline.lib.parser.DataParser;
//import com.streamsets.pipeline.lib.parser.DataParserException;
import com.streamsets.pipeline.lib.parser.DataParserFactory;

public class SASXPTParseFactory extends DataParserFactory {

	
	public static final Set<Class<? extends Enum>> MODES = Collections.emptySet();
	public static final Map<String, Object> CONFIGS = new HashMap<>();
	private SASXportConverter sasXportConverter;
	
	
	public SASXPTParseFactory(Settings settings) {
		super(settings);
	}
	
	@Override
	public DataParser getParser(String id, InputStream is, String offset) {
		String string_is = null;
		try{
			 string_is = IOUtils.toString(is, StandardCharsets.UTF_8);
		}
		catch(IOException e){
			
		}
		
		SASXportFileIterator sasXportFileIterator = null;
		try {
			sasXportFileIterator = new SASXportFileIterator(string_is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new SASXPTDataParser(sasXportFileIterator, getSettings().getContext(), id, offset);
	}
	
	@Override
	public DataParser getParser(String id, Reader reader, long offset)  {
		throw new UnsupportedOperationException();
	}
}