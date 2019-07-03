package com.streamsets.pipeline.lib.generator.sasxpt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
//import com.sentienz.sas.sasxpt.SASXportConverter;
import com.sentienz.sas.xpt.*;
import com.google.common.collect.ImmutableSet;
import com.streamsets.pipeline.lib.generator.DataGenerator;
import com.streamsets.pipeline.lib.generator.DataGeneratorFactory;


public class SASXPTDataGeneratorFactory extends DataGeneratorFactory {
	
	public static final Map<String, Object> CONFIGS = new HashMap<>();
	public static final Set<Class<? extends Enum>> MODES = ImmutableSet.of();
	private SASXportConverter sasxportconverter;

	public SASXPTDataGeneratorFactory(Settings settings) {
		super(settings);
	}

	@Override
	public DataGenerator getGenerator(OutputStream os) throws IOException {
		return new SASXPTDataGenerator(getSettings().getContext(), os, sasxportconverter);
	}


	

}