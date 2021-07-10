package org.mxretrv.threads;

import javax.naming.NamingException;
import java.io.IOException;

public interface Worker {
    void work() throws IOException, NamingException;
}
