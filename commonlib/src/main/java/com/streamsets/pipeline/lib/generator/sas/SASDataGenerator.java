package com.streamsets.pipeline.lib.generator.sas;

import java.io.IOException;
import java.io.OutputStream;

import com.epam.parso.SasFileReader;
import com.streamsets.pipeline.api.ProtoConfigurableEntity;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.lib.generator.DataGenerator;
import com.streamsets.pipeline.lib.generator.DataGeneratorException;

public class SASDataGenerator implements DataGenerator {

	private final ProtoConfigurableEntity.Context context;
	private final OutputStream outputStream;
	private final SasFileReader sasFileReader;

	public SASDataGenerator(ProtoConfigurableEntity.Context context, OutputStream os, SasFileReader sasFileReader) {
		this.context = context;
		this.outputStream = os;
		this.sasFileReader = sasFileReader;
	}

	@Override
	public void write(Record record) throws IOException, DataGeneratorException {

	}

	@Override
	public void flush() throws IOException {
		outputStream.flush();
	}

	@Override
	public void close() throws IOException {
		outputStream.close();
	}

}
