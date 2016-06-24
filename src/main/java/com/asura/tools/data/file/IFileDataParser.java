package com.asura.tools.data.file;

import com.asura.tools.data.DataIterator;
import com.asura.tools.util.exception.DataParserException;

public interface IFileDataParser<T, F> {
	public DataIterator<T> parse(F paramF) throws DataParserException;
}
