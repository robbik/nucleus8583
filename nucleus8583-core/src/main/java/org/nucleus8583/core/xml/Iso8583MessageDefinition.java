package org.nucleus8583.core.xml;

import java.io.UnsupportedEncodingException;
import java.util.List;

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

	public String getEncoding() {
		return encoding;
	}

	public CharsetProvider getCharsetProvider() {
		return tpl_provider;
	}

	public Iso8583Field[] getFields() {
		return tpl_fields;
	}

	public boolean[] getBinaries() {
		return tpl_binaries;
	}

	public void createMessageTemplate() {
		tpl_fields_count = fields.size();

		tpl_fields = new Iso8583Field[tpl_fields_count];
		for (int i = 0; i < tpl_fields_count; ++i) {
			tpl_fields[i] = fields.get(i).createField();
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
		return new Iso8583Message(tpl_fields, tpl_binaries, tpl_provider,
				tpl_fields_count);
	}
}
