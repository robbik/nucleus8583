package org.nucleus8583.oim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nucleus8583.core.Message;
import org.nucleus8583.oim.field.Field;
import org.nucleus8583.oim.util.BeanAccessor;

import rk.commons.util.FastByteArrayInputStream;
import rk.commons.util.FastByteArrayOutputStream;
import rk.commons.util.FastStringReader;
import rk.commons.util.FastStringWriter;

public abstract class MessageEntity {
	
	private Access access;

    private Field[] fields;

    private int count;
	
	public void setAccess(Access access) {
		this.access = access;
	}
	
    public void setFields(List<Field> fields) {
        count = fields.size();
        this.fields = fields.toArray(new Field[0]);
    }
    
    public void write(Message isoMsg, Object object) throws Exception {
    	write(isoMsg, new BeanAccessor(object, access));
    }
    
    public void write(Message isoMsg, BeanAccessor ba) throws Exception {
        FastStringWriter sw = new FastStringWriter(1024);
        FastByteArrayOutputStream baos = new FastByteArrayOutputStream(1024);
        
        Map<String, Object> tmp = new HashMap<String, Object>();

        for (int i = 0; i < count; ++i) {
            Field f = fields[i];
            
            if (f.supportWriter()) {
                // persist the field
                try {
                    f.write(sw, ba, tmp);
                } catch (Throwable t) {
                   throw new Exception("unable to write field #" + f.getNo(), t);
                }

                // set to iso-message
                isoMsg.set(f.getNo(), sw.toString());

                // reset for later reuse
                sw.reset();
            } else {
                // persist the field
                try {
                    f.write(baos, ba, tmp);
                } catch (Throwable t) {
                   throw new Exception("unable to write field #" + f.getNo(), t);
                }

                // set to iso-message
                isoMsg.set(f.getNo(), baos.toByteArray());

                // reset for later reuse
                baos.reset();
            }
        }
    }
    
    public Object read(Message isoMsg) throws Exception {
    	Object object = newBeanInstance();
    	read(isoMsg, new BeanAccessor(object, access));
    	
    	return object;
    }
    
    public BeanAccessor readAsBeanAccessor(Message isoMsg) throws Exception {
    	BeanAccessor ba = new BeanAccessor(newBeanInstance(), access);
    	
    	read(isoMsg, ba);
    	
    	return ba;
    }
    
    public void read(Message isoMsg, BeanAccessor ba) throws Exception {
        FastStringReader reader = new FastStringReader();
        FastByteArrayInputStream inputstream = new FastByteArrayInputStream();
        
        Map<String, Object> tmp = new HashMap<String, Object>();
        
        for (int i = 0; i < count; ++i) {
            Field f = fields[i];
            
            // get from iso-message
            Object v = isoMsg.get(f.getNo());
            
            if (v == null) {
                // value is not set, skipping
            } else if (v instanceof String) {
                reader.reset((String) v);
                
                // load the field
                try {
                   f.read(reader, ba, tmp);
                } catch (Throwable t) {
                   throw new Exception("unable to read field #" + f.getNo(), t);
                }
            } else if (v instanceof byte[]) {
                inputstream.reset((byte[]) v);
                
                // load the field
                try {
                    f.read(inputstream, ba, tmp);
                } catch (Throwable t) {
                   throw new Exception("unable to read field #" + f.getNo(), t);
                }
            } else {
                throw new UnsupportedOperationException("unable to read field #" + f.getNo() +
                        " because the field value is not string nor byte[], it is " + v.getClass());
            }
        }
    }
    
    public abstract Object newBeanInstance();
}
