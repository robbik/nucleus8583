package org.nucleus8583.oim.accessor;

import org.nucleus8583.core.Iso8583Message;
import org.nucleus8583.oim.util.BeanUtils;
import org.nucleus8583.oim.xml.EntityDefinition;
import org.nucleus8583.oim.xml.EntityFieldDefinition;

public class EntityAccessor {

    private final String className;

    private final Class<?> _class;

    private final int count;

    private final int[] fieldIds;

    private final EntityFieldAccessor[] fields;

    public EntityAccessor(EntityDefinition def) {
        className = def.getClassName();
        _class = BeanUtils.forClassName(className);

        count = def.getFields().size();
        fieldIds = new int[count];
        fields = new EntityFieldAccessor[count];

        int i = 0;

        for (EntityFieldDefinition efd : def.getFields()) {
            int id = Integer.parseInt(efd.getId());

            fieldIds[i] = id;
            fields[i] = new EntityFieldAccessor(efd);

            ++i;
        }
    }

    public Object read(Iso8583Message in) {
        Object out = BeanUtils.newInstance(_class);

        for (int i = 0; i < count; ++i) {
            fields[i].set(className, out, in.get(fieldIds[i]));
        }

        return out;
    }

    public void read(Iso8583Message in, Object out) {
        if (!_class.isInstance(in)) {
            throw new IllegalArgumentException("Object " + out + " is not instance of " + className);
        }

        for (int i = 0; i < count; ++i) {
            fields[i].set(className, out, in.get(fieldIds[i]));
        }
    }

    public void write(Object in, Iso8583Message out) {
        if (!_class.isInstance(in)) {
            throw new IllegalArgumentException("Object " + in + " is not instance of " + className);
        }

        for (int i = 0; i < count; ++i) {
            Object value = fields[i].get(className, in);

            if (value == null) {
                out.unset(fieldIds[i]);
            } else if (value instanceof byte[]) {
                out.set(fieldIds[i], (byte[]) value);
            } else if (value instanceof String) {
                out.set(fieldIds[i], (String) value);
            } else {
                throw new IllegalArgumentException("contract violation while writing field #" + fieldIds[i]
                        + " (value is not null nor byte[] nor java.lang.String).");
            }
        }
    }
}
