package com.streamsets.pipeline.lib.parser.sasxpt;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.sentienz.sas.xpt.SASXportFileIterator;
import com.streamsets.pipeline.lib.parser.DataParser;
import com.streamsets.pipeline.lib.parser.DataParserFactory;

public class SASXPTParseFactory extends DataParserFactory {

	
	public static final Set<Class<? extends Enum>> MODES = Collections.emptySet();
	public static final Map<String, Object> CONFIGS = new HashMap<>();
	
	
	public SASXPTParseFactory(Settings settings) {
		super(settings);
	}
	
	@Override
	public DataParser getParser(String id, InputStream is, String offset) {
		SASXportFileIterator sasXportFileIterator = null;
		try {
			sasXportFileIterator = new SASXportFileIterator(is);
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