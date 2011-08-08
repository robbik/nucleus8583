package org.nucleus8583.core.xml;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.nucleus8583.core.Iso8583Binary;
import org.nucleus8583.core.Iso8583Field;
import org.nucleus8583.core.Iso8583Message;
import org.nucleus8583.core.charset.Charsets;
import org.nucleus8583.core.charset.spi.CharsetProvider;

@XmlRootElement(name = "iso-message", namespace = "http://www.nucleus8583.org/schema/iso-message")
@XmlAccessorType(XmlAccessType.FIELD)
public class Iso8583MessageDefinition {

	@XmlTransient
	private static Comparator<Iso8583Field> byFieldId = new Comparator<Iso8583Field>() {
		public int compare(Iso8583Field a, Iso8583Field b) {
			return a.getId() - b.getId();
		}
	};

	@XmlAttribute(name = "encoding", required = true)
	private String encoding;

	@XmlElementRef
	private List<Iso8583FieldDefinition> fields;

	@XmlTransient
	private Iso8583Field[] tpl_fields;

	@XmlTransient
	private int tpl_fields_count;

	@XmlTransient
	private boolean[] tpl_binaries;

	@XmlTransient
	private CharsetProvider tpl_provider;

	@XmlTransient
	private boolean hasMti;

	public String getEncoding() {
		return encoding;
	}

	public CharsetProvider getCharsetProvider() {
		return tpl_provider;
	}
	
	public boolean hasMti() {
		return hasMti;
	}

	public Iso8583Field[] getFields() {
		return tpl_fields;
	}

	public boolean[] getBinaries() {
		return tpl_binaries;
	}

    private boolean replace(int id, Iso8583FieldDefinition newdef, List<Iso8583FieldDefinition> fields, int count) {
        for (int i = 0; i < count; ++i) {
        	Iso8583FieldDefinition def = fields.get(i);

            if (def.getId() == id) {
                fields.set(i, newdef);
                return true;
            }
        }

        return false;
    }

    private void checkDuplicateId(List<Iso8583FieldDefinition> list, int count) {
    	Set<Integer> set = new HashSet<Integer>();
    	
    	for (int i = 0; i < count; ++i) {
    		Integer id = Integer.valueOf(list.get(i).getId());
    		
    		if (set.contains(id)) {
    			throw new IllegalArgumentException("duplicate id " + id + " found");
    		}
    		
    		set.add(id);
    	}
    }

	public void createMessageTemplate() {
		List<Iso8583FieldDefinition> fields = this.fields;

		int count = fields.size();

		// automatic override field no 0 (if any)
        hasMti = replace(0, Iso8583FieldDefinition.FIELD_0, fields, count);
        if (!hasMti) {
		    fields.add(Iso8583FieldDefinition.FIELD_0);
		}

        // automatic set field no 1
		if (!replace(1, Iso8583FieldDefinition.FIELD_1, fields, count)) {
		    fields.add(Iso8583FieldDefinition.FIELD_1);
		}

        // automatic set field no 65
        if (!replace(65, Iso8583FieldDefinition.FIELD_65, fields, count)) {
            fields.add(Iso8583FieldDefinition.FIELD_65);
        }
        
        checkDuplicateId(fields, count);

        tpl_fields_count = fields.size();

		tpl_fields = new Iso8583Field[tpl_fields_count];
		for (int i = 0; i < tpl_fields_count; ++i) {
			tpl_fields[i] = fields.get(i).createField();
		}

		// sort fields by it's id
		Arrays.sort(tpl_fields, byFieldId);
		
		// check for skipped fields
		for (int i = 2; i < tpl_fields_count; ++i) {
			if (tpl_fields[i].getId() != i) {
				throw new IllegalArgumentException("field #" + i + " is not defined");
			}
		}
		
		tpl_binaries = new boolean[tpl_fields_count];
		for (int i = tpl_fields_count - 1; i >= 0; --i) {
			tpl_binaries[i] = (tpl_fields[i] instanceof Iso8583Binary);
		}

		tpl_provider = Charsets.getProvider(encoding);
		if (tpl_provider == null) {
			throw new RuntimeException(new UnsupportedEncodingException(
					encoding));
		}
	}

	public Iso8583Message createMessage() {
		return new Iso8583Message(hasMti, tpl_fields, tpl_binaries, tpl_provider,
				tpl_fields_count);
	}
}
