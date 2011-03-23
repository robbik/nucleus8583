package org.nucleus8583.oim.type.spi;

public interface DataTypeFactory {
	DataType createDataType(String query) throws Exception;
}
