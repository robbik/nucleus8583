package org.nucleus8583.oim.accessor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.nucleus8583.oim.xml.ArrayDefinition;
import org.nucleus8583.oim.xml.ArrayLengthDefinition;
import org.nucleus8583.oim.xml.BasicDefinition;
import org.nucleus8583.oim.xml.ConstantDefinition;
import org.nucleus8583.oim.xml.EntityFieldDefinition;
import org.nucleus8583.oim.xml.IgnoreDefinition;
import org.nucleus8583.oim.xml.SubDefinition;
import org.nucleus8583.oim.xml.VarBasicDefinition;

public class EntityFieldAccessor {

    private final boolean binary;

    private final int count;

    private final ValueAccessor[] contents;

    public EntityFieldAccessor(EntityFieldDefinition def) {
        binary = def.isBinary();

        List<Object> list = def.getContents();
        if (list == null) {
            count = 0;
            contents = null;
        } else {
            count = list.size();

            int i = 0;
            for (Object obj : list) {
                if (obj instanceof ArrayDefinition) {
                    // TODO
                } else if (obj instanceof ArrayLengthDefinition) {
                    // TODO
                } else if (obj instanceof BasicDefinition) {
                    // TODO
                } else if (obj instanceof ConstantDefinition) {
                    // TODO
                } else if (obj instanceof IgnoreDefinition) {
                    // TODO
                } else if (obj instanceof SubDefinition) {
                    // TODO
                } else if (obj instanceof VarBasicDefinition) {
                    // TODO
                }

                ++i;
            }
        }
    }

    public void set(String className, Object obj, Object value) {
        InputStream in;

        if (value == null) {
            return;
        }

        if (value instanceof byte[]) {
            in = new ByteArrayInputStream((byte[]) value);
        } else {
            in = new ByteArrayInputStream((byte[]) value);
        }
    }

    public void set(String className, Object obj, InputStream value) {
        //
    }

    public Object get(String className, Object obj) {
        //
    }
}
