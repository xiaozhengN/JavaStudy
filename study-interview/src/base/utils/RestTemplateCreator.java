package base.utils;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * RestTemplateCreator
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-18 1:36:45
 */
public class RestTemplateCreator {
    // 默认的超时时间
    private static final int TIME_OUT = 5000;

    // SSLRestTemplate
    private static RestTemplate sslRestTemplate = createSSLRestTemplate();

    // RestTemplate
    private static RestTemplate restTemplate = createRestTemplate();

    /**
     * 获取 SSLRestTemplate
     *
     * @return SSLRestTemplate
     */
    public static RestTemplate getSslRestTemplate() {
        return sslRestTemplate;
    }

    /**
     * 获取 RestTemplate
     *
     * @return RestTemplate
     */
    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }

    private static RestTemplate createRestTemplate() {
        return createSSLRestTemplate(TIME_OUT);
    }

    private static RestTemplate createSSLRestTemplate() {
        return createSSLRestTemplate(TIME_OUT);
    }

    private static RestTemplate createSSLRestTemplate(int timeOut) {
        try {
            HttpClientBuilder clientBuilder = HttpClients.custom();
            setSsl(clientBuilder);
            CloseableHttpClient httpClient = clientBuilder.build();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setConnectionRequestTimeout(timeOut);
            requestFactory.setConnectTimeout(timeOut);
            requestFactory.setReadTimeout(timeOut);
            requestFactory.setHttpClient(httpClient);

            return new RestTemplate(requestFactory);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void setSsl(HttpClientBuilder clientBuilder) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // 直接返回true, 这里不检查证书的有效性
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        clientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
    }
}
