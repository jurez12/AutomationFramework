package com.framework.library;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;




public class HttpUtil {

   // static final Logger logger = Logger.getLogger(HttpUtil.class);
    static final int CONNECTION_TIMEOUT = 10 * 1000;
    static final int SO_TIMEOUT = 60 * 1000;
    
    
    public static HttpResponse get(String url) throws Exception {
        //logger.debug("GET-ing URL: " + url);
        return fetchResponse(new HttpGet(url));
    }
    
    public static HttpResponse get(String url, int connectionTimeout, int soTimeout) throws Exception {
        //logger.debug("GET-ing URL: " + url);
        return fetchResponse(new HttpGet(url), connectionTimeout, soTimeout);
    }
    
    public static HttpResponse get(String url, String proxyServer) throws Exception {
        String host = proxyServer.split(":")[0];
        int port = Integer.parseInt(proxyServer.split(":")[1]);
        //logger.debug("GET-ing URL through proxy [" + host + ":" + port + "]: " + url);
        return fetchResponse(new HttpGet(url), host, port);
    }

    public static HttpResponse post(String url, List<NameValuePair> params) throws Exception {
        //logger.debug("POST-ing to URL: " + url);
        //logger.debug("Params: " + params);
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(params));
        return fetchResponse(post);
    }

    public static JSONObject extractJSON(HttpResponse response) throws Exception {
        String responseBody = EntityUtils.toString(response.getEntity());
        //logger.debug("Converting to JSON: " + responseBody);
        return (JSONObject)new JSONParser().parse(responseBody);
    }

    private static HttpResponse fetchResponse(HttpUriRequest request) throws Exception {
        HttpParams httpParameters = new BasicHttpParams();
        //logger.debug("Setting [ConnectionTimeout] to [" + CONNECTION_TIMEOUT + "] milliseconds and [SoTimeout] to [" + SO_TIMEOUT + "] milliseconds");
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
        
        DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
        return new HttpUtil().wrapClient(httpclient).execute(request);
    }
    
    private static HttpResponse fetchResponse(HttpUriRequest request, int connectionTimeout, int soTimeout) throws Exception {
        HttpParams httpParameters = new BasicHttpParams();
        //logger.debug("Setting [ConnectionTimeout] to [" + connectionTimeout + "] milliseconds and [SoTimeout] to [" + soTimeout + "] milliseconds");
        HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParameters, soTimeout);
        
        DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
        return new HttpUtil().wrapClient(httpclient).execute(request);
    }
    
    private static HttpResponse fetchResponse(HttpUriRequest request, String proxyServer, int port) throws Exception {
        HttpParams httpParameters = new BasicHttpParams();
        //logger.debug("Setting [ConnectionTimeout] to [" + CONNECTION_TIMEOUT + "] milliseconds and [SoTimeout] to [" + SO_TIMEOUT + "] milliseconds");
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
        
        DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpHost proxy = new HttpHost(proxyServer, port);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        return new HttpUtil().wrapClient(httpclient).execute(request);
    }

    // Returns the response as string
    public static String getResponse(HttpResponse response) throws IOException {
        String responseBody = EntityUtils.toString(response.getEntity());
        //logger.debug("Returning Response: [" + responseBody + "]");
        return responseBody;
    }

    // Will wrap HttpClient to ignore SSL certificate expired errors 
    public HttpClient wrapClient(HttpClient base) throws Exception {
        //logger.debug("Overriding default trust manager to allow expired certificates");
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            //logger.error("Failed to wrap HttpClient", ex);
            throw ex;
        }
    }
}
