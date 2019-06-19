package com.streamsets.pipeline.lib.generator.sas;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.epam.parso.SasFileReader;
import com.google.common.collect.ImmutableSet;
import com.streamsets.pipeline.lib.generator.DataGenerator;
import com.streamsets.pipeline.lib.generator.DataGeneratorFactory;

public class SASDataGeneratorFactory extends DataGeneratorFactory {

	public static final Map<String, Object> CONFIGS = new HashMap<>();
	public static final Set<Class<? extends Enum>> MODES = ImmutableSet.of();
	private SasFileReader sasFileReader;

	public SASDataGeneratorFactory(Settings settings) {
		super(settings);
	}

	@Override
	public DataGenerator getGenerator(OutputStream os) throws IOException {
		return new SASDataGenerator(getSettings().getContext(), os, sasFileReader);
	}

}
