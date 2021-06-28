package org.mxretrv.dns;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MXRecordRetriever {

    /**
     * Get MX records of a given domain name as an array
     *
     * @param domain domain name
     * @return list of MX records for the domain. If there are no records, null is returned.
     * @throws NamingException exception
     */
    public static List<String> mxRecords(String domain) throws NamingException {
        Attribute mx = mxAttr(domain);
        if (mx == null)
            return null;
        NamingEnumeration<?> nEnum = mx.getAll();
        List<String> records = new ArrayList<String>();

        while (nEnum.hasMore())
            records.add((String) nEnum.next());
        return records;
    }


    public static Attribute mxAttr(String hostName ) throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.factory.initial",
                "com.sun.jndi.dns.DnsContextFactory");
        DirContext ictx = new InitialDirContext( env );
        Attributes attrs =
                ictx.getAttributes( hostName, new String[] { "MX" });
        return attrs.get( "MX" );
    }
}
