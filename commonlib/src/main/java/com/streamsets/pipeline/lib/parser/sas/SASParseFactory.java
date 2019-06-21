package com.streamsets.pipeline.lib.parser.sas;

import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.epam.parso.SasFileReader;
import com.epam.parso.impl.SasFileReaderImpl;
import com.streamsets.pipeline.lib.parser.DataParser;
import com.streamsets.pipeline.lib.parser.DataParserException;
import com.streamsets.pipeline.lib.parser.DataParserFactory;

public class SASParseFactory extends DataParserFactory {

	public static final Set<Class<? extends Enum>> MODES = Collections.emptySet();
	public static final Map<String, Object> CONFIGS = new HashMap<>();
	private SasFileReader sasFileReader;

	public SASParseFactory(Settings settings) {
		super(settings);
	}

	@Override
	public DataParser getParser(String id, InputStream is, String offset) throws DataParserException {
		sasFileReader = new SasFileReaderImpl(is);
		return new SASDataParser(sasFileReader, getSettings().getContext(), id, offset);
	}

	@Override
	public DataParser getParser(String id, Reader reader, long offset) throws DataParserException {
		throw new UnsupportedOperationException();
	}

}
