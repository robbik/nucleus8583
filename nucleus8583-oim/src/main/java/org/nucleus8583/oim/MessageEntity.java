package org.nucleus8583.oim;

import java.util.List;
import java.util.Map;

import org.nucleus8583.core.Message;
import org.nucleus8583.oim.field.Field;

import rk.commons.util.FastByteArrayInputStream;
import rk.commons.util.FastByteArrayOutputStream;
import rk.commons.util.FastStringReader;
import rk.commons.util.FastStringWriter;

public final class MessageEntity {

	private Field[] fields;

	private int count;

	public void setFields(List<Field> fields) {
		count = fields.size();

		this.fields = fields.toArray(new Field[0]);
	}
	
	public void persist(Message isoMsg, Map<String, Object> root) throws Exception {
		FastStringWriter sw = new FastStringWriter(1024);
		FastByteArrayOutputStream baos = new FastByteArrayOutputStream(1024);

		for (int i = 0; i < count; ++i) {
			Field f = fields[i];
			
			if (f.supportWriter()) {
				// persist the field
				f.write(sw, root);

				// set to iso-message
				isoMsg.set(f.getNo(), sw.toString());

				// reset for later reuse
				sw.reset();
			} else {
				// persist the field
				f.write(baos, root);

				// set to iso-message
				isoMsg.set(f.getNo(), baos.toByteArray());

				// reset for later reuse
				baos.reset();
			}
		}
	}
	
	public void load(Message isoMsg, Map<String, Object> root) throws Exception {
		FastStringReader reader = new FastStringReader();
		FastByteArrayInputStream inputstream = new FastByteArrayInputStream();
		
		for (int i = 0; i < count; ++i) {
			Field f = fields[i];
			
			// get from iso-message
			Object v = isoMsg.get(f.getNo());
			
			if (v instanceof String) {
				reader.reset((String) v);
				
				// load the field
				f.read(reader, root);
			} else if (v instanceof byte[]) {
				inputstream.reset((byte[]) v);
				
				// load the field
				f.read(inputstream, root);
			} else {
				throw new UnsupportedOperationException("unable to persist field #" + f.getNo() +
						" because the field value is not string nor byte[], it is " + v.getClass());
			}
		}
	}
}
