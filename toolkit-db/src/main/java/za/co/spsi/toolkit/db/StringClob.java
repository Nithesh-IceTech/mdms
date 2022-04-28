package za.co.spsi.toolkit.db;

import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Created by jaspervdb on 2016/04/29.
 */
public class StringClob implements Clob {

    private String data;

    public StringClob(String data) {
        this.data = data;
    }

    @Override
    public long length() throws SQLException {
        return data.length();
    }

    @Override
    public String getSubString(long pos, int length) throws SQLException {
        return data.substring((int)pos,length);
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        return new InputStreamReader(new ByteArrayInputStream(data.getBytes()));
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        return new ByteArrayInputStream(data.getBytes());
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
        return data.substring((int)start).indexOf(searchstr);
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {
        return 0;
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
        return 0;
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        return 0;
    }

    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
        return null;
    }

    @Override
    public Writer setCharacterStream(long pos) throws SQLException {
        return null;
    }

    @Override
    public void truncate(long len) throws SQLException {

    }

    @Override
    public void free() throws SQLException {

    }

    @Override
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        return null;
    }
}
