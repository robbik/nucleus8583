package org.nucleus8583.oim;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.nucleus8583.core.Message;
import org.nucleus8583.oim.field.Field;
import org.nucleus8583.oim.util.FastStringReader;

public final class MessageEntity {

	private Field[] fields;

	private int count;

	public void setFields(List<Field> fields) {
		count = fields.size();

		this.fields = fields.toArray(new Field[0]);
	}
	
	public void persist(Message isoMsg, Map<String, Object> root) throws Exception {
		StringWriter sw = new StringWriter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		for (int i = 0; i < count; ++i) {
			Field f = fields[i];
			
			if (f.supportWriter()) {
				// persist the field
				f.write(sw, root);

				// set to iso-message
				isoMsg.set(f.getNo(), sw.toString());

				// reset for later reuse
				sw.getBuffer().setLength(0);
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
		for (int i = 0; i < count; ++i) {
			Field f = fields[i];
			
			// get from iso-message
			Object v = isoMsg.get(f.getNo());
			
			if (v instanceof String) {
				// load the field
				f.read(new FastStringReader((String) v), root);
			} else if (v instanceof byte[]) {
				// load the field
				f.read(new ByteArrayInputStream((byte[]) v), root);
			} else {
				throw new UnsupportedOperationException("unable to persist field #" + f.getNo() +
						" because the field value is not string nor byte[], it is " + v.getClass());
			}
		}
	}
}
