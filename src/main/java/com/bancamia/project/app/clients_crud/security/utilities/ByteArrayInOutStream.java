package com.bancamia.project.app.clients_crud.security.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ByteArrayInOutStream extends ByteArrayOutputStream {
	
    public ByteArrayInOutStream() {
        super();
    }
    
    public ByteArrayInOutStream(int size) {
        super(size);
    }
    
    public ByteArrayInputStream getInputStream() {
        ByteArrayInputStream in = new ByteArrayInputStream(this.buf, 0, this.count);
        this.buf = null;
        return in;
    }
}