package com.alibaba.chaosblade.box.dao.scheduler.quartz;

import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author haibin.lhb
 *
 * 
 */
public class StdJDBCDelegateExt extends StdJDBCDelegate {

    /**
     * <p>
     * This method should be overridden by any delegate subclasses that need
     * special handling for BLOBs. The default implementation uses standard JDBC
     * <code>java.sql.Blob</code> operations.
     * </p>
     *
     * <p>
     * This implementation overcomes the incorrect classloader being used in
     * ObjectInputStream, overriding it with the current threads classloader.
     * </p>
     *
     * @param rs      the result set, already queued to the correct row
     * @param colName the column name for the BLOB
     * @return the deserialized Object from the ResultSet BLOB
     * @throws ClassNotFoundException if a class found during deserialization cannot be found
     * @throws IOException if deserialization causes an error
     */
    @Override
    protected Object getObjectFromBlob(ResultSet rs, String colName)
        throws ClassNotFoundException, IOException, SQLException {
        Object obj = null;
        Blob blobLocator = rs.getBlob(colName);
        if (blobLocator != null && blobLocator.length() != 0) {
            InputStream binaryInput = blobLocator.getBinaryStream();
            if (null != binaryInput) {
                if (binaryInput instanceof ByteArrayInputStream
                    && ((ByteArrayInputStream)binaryInput).available() == 0) {
                    // do nothing
                } else {
                    ObjectInputStream in = new ObjectInputStream(binaryInput) {
                        @Override
                        protected Class<?> resolveClass(ObjectStreamClass desc)
                            throws IOException, ClassNotFoundException {
                            String name = desc.getName();
                            try {
                                return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
                            } catch (ClassNotFoundException ex) {
                                Class<?> clazz = ChaosQuartzClassLoadHelper.getJobClassName(name);
                                if (clazz == null) {
                                    return super.resolveClass(desc);
                                } else {
                                    ReflectionUtils.doWithFields(desc.getClass(), new FieldCallback() {
                                        @Override
                                        public void doWith(Field field)
                                            throws IllegalArgumentException, IllegalAccessException {
                                            field.setAccessible(true);
                                            ReflectionUtils.setField(field, desc, clazz.getName());
                                        }
                                    }, new FieldFilter() {
                                        @Override
                                        public boolean matches(Field field) {
                                            return field.getName().equals("name");
                                        }
                                    });
                                }
                                return clazz;
                            }
                        }
                    };

                    try {
                        obj = in.readObject();
                    } finally {
                        in.close();
                    }
                }
            }

        }
        return obj;
    }

}
