package com.hfxb.app.core.helper;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


public class YiXMLWriter extends XMLWriter {

	public YiXMLWriter() {
		super();
	}

	public YiXMLWriter(OutputFormat format) throws UnsupportedEncodingException {
		super(format);
	}

	public YiXMLWriter(OutputStream out, OutputFormat format)
			throws UnsupportedEncodingException {
		super(out, format);
	}

	public YiXMLWriter(OutputStream out) throws UnsupportedEncodingException {
		super(out);
	}

	public YiXMLWriter(Writer writer, OutputFormat format) {
		super(writer, format);
	}

	public YiXMLWriter(Writer writer) {
		super(writer);
	}

	// allow set preserve field
	public void setPreserve(boolean preserve){
		super.preserve = preserve;
	}
	
}
