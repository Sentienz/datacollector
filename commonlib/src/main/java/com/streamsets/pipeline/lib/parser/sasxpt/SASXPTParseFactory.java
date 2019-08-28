package com.streamsets.pipeline.lib.parser.sasxpt;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sentienz.sas.xpt.SASXportFileIterator;
import com.streamsets.pipeline.lib.parser.DataParser;
import com.streamsets.pipeline.lib.parser.DataParserException;
import com.streamsets.pipeline.lib.parser.DataParserFactory;
import com.streamsets.pipeline.lib.parser.Errors;

public class SASXPTParseFactory extends DataParserFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(SASXPTParseFactory.class);

	public static final Set<Class<? extends Enum>> MODES = Collections.emptySet();
	public static final Map<String, Object> CONFIGS = new HashMap<>();
	
	
	public SASXPTParseFactory(Settings settings) {
		super(settings);
	}
	
	@Override
	public DataParser getParser(String id, InputStream is, String offset) throws DataParserException {
		SASXportFileIterator sasXportFileIterator = null;
		logger.info("Input Stream: "+is.getClass()+" id: "+id+" offset: "+offset);
		try {
			sasXportFileIterator = new SASXportFileIterator(is);
		} catch (Exception e) {
		  logger.error("Exception occured while initializing parser",e);
		  throw new DataParserException(Errors.DATA_PARSER_01, "", e.toString(), e);
		}
		return new SASXPTDataParser(sasXportFileIterator, getSettings().getContext(), id, offset);
	}
	
	@Override
	public DataParser getParser(String id, Reader reader, long offset)  {
		throw new UnsupportedOperationException();
	}
}