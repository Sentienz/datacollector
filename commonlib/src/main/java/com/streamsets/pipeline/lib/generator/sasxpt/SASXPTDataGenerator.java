package com.streamsets.pipeline.lib.generator.sasxpt;

import java.io.IOException;
import java.io.OutputStream;
//import com.sentienz.sas.sasxpt.SASXportConverter;
import com.sentienz.sas.xpt.*;
import com.streamsets.pipeline.api.ProtoConfigurableEntity;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.lib.generator.DataGenerator;
import com.streamsets.pipeline.lib.generator.DataGeneratorException;


public class SASXPTDataGenerator implements DataGenerator {

	
	private final ProtoConfigurableEntity.Context context;
	private final OutputStream outputStream;
	private final SASXportConverter sasxportconverter;

	public SASXPTDataGenerator(ProtoConfigurableEntity.Context context, OutputStream os, SASXportConverter sasxportconverter) {
		this.context = context;
		this.outputStream = os;
		this.sasxportconverter = sasxportconverter;
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

