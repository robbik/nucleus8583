package org.nucleus8583.core.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.util.ResourceUtils;
import org.xml.sax.SAXParseException;

public class MessageDefinitionReaderTest {

    private MessageDefinitionReader reader;

    @Before
    public void before() {
        reader = new MessageDefinitionReader();
    }

    @Test
    public void testUnmarshalFromURL() throws Exception {
        MessageDefinition def = reader.unmarshal(ResourceUtils.getURL("file:src/test/resources/META-INF/codec8583.xml"));

        assertEquals(129, def.getFields().size());

        assertEquals(0, def.getFields().get(0).getId());
        assertEquals("custom", def.getFields().get(0).getType());
        assertEquals(4, def.getFields().get(0).getLength());
        assertEquals(FieldAlignments.NONE, def.getFields().get(0).getAlign());
        assertEquals(null, def.getFields().get(0).getEmptyValue());
    }

    @Test
    public void testUnmarshalFromInputStream() throws Exception {
        MessageDefinition def = reader.unmarshal(ResourceUtils.getURL("file:src/test/resources/META-INF/codec8583.xml").openStream());

        assertEquals(129, def.getFields().size());

        assertEquals(0, def.getFields().get(0).getId());
        assertEquals("custom", def.getFields().get(0).getType());
        assertEquals(4, def.getFields().get(0).getLength());
        assertEquals(FieldAlignments.NONE, def.getFields().get(0).getAlign());
        assertEquals(null, def.getFields().get(0).getEmptyValue());
    }

    @Test (expected = SAXParseException.class)
    public void testUnmarshalFromURLIfDocumentIsError() throws Exception {
        reader.unmarshal(ResourceUtils.getURL("file:src/test/resources/META-INF/error8583-2.xml"));
    }
}
