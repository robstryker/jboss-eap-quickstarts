/*
 * Copyright 2022 Red Hat, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jboss.as.quickstarts.servlet_security;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 */

public class RobServletTest {
    private static final String ENDPOINT_1 = "hello1";
    private static final String ENDPOINT_2 = "hello2";

    protected URI getHTTPEndpoint(String endpoint) {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080/rob-hello";
        }
        try {
            return new URI(host + "/" + endpoint);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected static String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }

    @Test
    public void testConnectEndpoint1() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(getHTTPEndpoint(ENDPOINT_1))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(200, response.statusCode());
        String[] lines = response.body().toString().split(System.lineSeparator());
        Assert.assertTrue(lines[1].trim().contains("RobServlet1"));
    }

    @Test
    public void testConnectEndpoint2() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(getHTTPEndpoint(ENDPOINT_2))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(200, response.statusCode());
        String[] lines = response.body().toString().split(System.lineSeparator());
        Assert.assertTrue(lines[1].trim().contains("RobServlet2"));
    }
}
