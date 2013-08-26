package com.signicat.support.sds;

import java.io.*;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class JavaPostTest {

    @Test
    public void uploading_a_pdf_file_to_sds_using_apache_http_client() throws IOException {

        // Create the client and set it up for basic authentication
        DefaultHttpClient client = new DefaultHttpClient();
        AuthScope authScope = new AuthScope("test.signicat.com", 443);
        Credentials credentials = new UsernamePasswordCredentials("shared", "Bond007");
        client.getCredentialsProvider().setCredentials(authScope, credentials);

        // Grab the PDF file and add it to the HttpPost request
        HttpPost post = new HttpPost("https://test.signicat.com/doc/shared/sds/'");
        File pdf = new File("src/test/com/signicat/support/sds/mydocument.pdf");
        post.setEntity(new FileEntity(pdf, "application/pdf"));

        // Execute the request and read the document id from the response
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = rd.readLine();

        assertEquals(201, response.getStatusLine().getStatusCode());
        assertNotNull(line);
        assertEquals(58, line.length());

    }

    @Test
    public void downloading_a_pdf_file_from_sds_using_apache_http_client() throws IOException {

        // Create the client and set it up for basic authentication
        DefaultHttpClient client = new DefaultHttpClient();
        AuthScope authScope = new AuthScope("test.signicat.com", 443);
        Credentials credentials = new UsernamePasswordCredentials("shared", "Bond007");
        client.getCredentialsProvider().setCredentials(authScope, credentials);

        // Construct the URL to the document and add it to the HttpGet request
        String documentId = "260820133q4qadupq43zq7fgajithlyxkso8ux96p4bp20l5h3a1en24k2";
        String sdsUrl = "https://test.signicat.com/doc/shared/sds/" + documentId;
        HttpGet get = new HttpGet(sdsUrl);

        // Execute the request and read+save the document id from the response
        HttpResponse response = client.execute(get);
        File pdf = new File("src/test/com/signicat/support/sds/mydownloadedfile.pdf");
        InputStream inputStream = response.getEntity().getContent();
        OutputStream outputStream = new FileOutputStream(pdf);
        IOUtils.copy(inputStream, outputStream);
        outputStream.close();

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("application/pdf", response.getFirstHeader("Content-Type").getValue());

    }
}
