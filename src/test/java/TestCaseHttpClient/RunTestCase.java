package TestCaseHttpClient;

import io.vertx.core.json.JsonObject;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import tungphamdev.oraclesun.RestclientVer2.BodyResponse;
import tungphamdev.oraclesun.RestclientVer2.ClientConfig;
import tungphamdev.oraclesun.RestclientVer2.MapCookies;
import tungphamdev.oraclesun.RestclientVer2.RestClient;

public class RunTestCase {

    private RestClient client;
    private String domain = "http://localhost:8080";
    private String cookie;
    private Map<String, String> mapCookieResponse = new HashMap<>();

    public RunTestCase setCookie(String cookie) {
        this.cookie = cookie;
        if (cookie != null) {
            new ClientConfig().streamNameValueCookies(cookie, (name, value) -> {
//                System.out.println(name+ " = " +value);
                mapCookieResponse.put(name, value);
            });
        }

        return this;
    }

    public static void main(String[] args) {
        String cookie = "hs=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IkJhc2U2NEVuY29kZSBlbWFpbCIsInBob25lIjoibnVtYmVyIFBob25lIiwidGltZUNyIjoiMTY3MTA4MDM0NjcyMiIsInVpZCI6ImFlMTVhZGJmLTIwMzUtNDgzNS1hMjJiLWYxNjIxYWY3OWIyNSIsImlkIjoiNjM5YWE5OWE5ZThkMDkwZTUzYjc5ZTg1IiwiaWF0IjoxNjcxMDgwMzQ2LCJleHAiOjE2NzE0MjU5NDZ9.sZtfS4Oy9ClB-IPAE0wolmiSxH-iP3Tsx-3PbvBFHGw; pvd=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IkJhc2U2NEVuY29kZSBlbWFpbCIsInBob25lIjoibnVtYmVyIFBob25lIiwidGltZUNyIjoiMTY3MTA4MDM0NjcyMiIsInVpZCI6ImFlMTVhZGJmLTIwMzUtNDgzNS1hMjJiLWYxNjIxYWY3OWIyNSIsImlhdCI6MTY3MTA4MDM0NiwiZXhwIjoxNjcxNDI1OTQ2fQ.hpS_ulEpfXwOlEZ41HhgXZWzEcW7d_RG1szfXJO70M0;";
        long time = System.currentTimeMillis();
        RunTestCase runTest = new RunTestCase();
        runTest.createHttp()
                //                .createAccDriver()
                .setCookie(cookie)
                .uploadImg();

        System.out.println(">> end " + (System.currentTimeMillis() - time));
    }

    public RunTestCase uploadImg() {
        String pathImage = "D:\\netbean12.0\\FaceBook\\DebtForgiveness\\Image\\buttonStartShare.png";
        String pathImgChange = "C:\\Users\\Admin\\Desktop\\New folder\\regpage\\img\\Untitled32.png";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody("filePre", new File(pathImage))
                .addBinaryBody("fileBehin", new File(pathImgChange));

        client.POST(domain + "/user/upload/img", client.MULTIPART(builder), (request) -> {
            request.setHeader("cookie", new MapCookies(mapCookieResponse).toString());
        })
                .onSuccess(this::handleSuccees)
                .onFailure(this::handleFailed);

        return this;
    }

    public RunTestCase createAccDriver() {
        var json = new JsonObject().put("userName", encodeBase64("tùng Phạm"))
                .put("pass", encodeBase64("tung123"))
                .put("email", encodeBase64("xdsjin1892@gmail.com"))
                .put("phone", encodeBase64("98274938472"))
                .put("nameBank", encodeBase64("VietcomBank"))
                .put("idCard", encodeBase64("2304803"))
                .put("cardHolderName", encodeBase64("Pham Quang Tung"))
                .put("x-i-cont", true);

        client.POST(domain + "/register/driver", client.JSON(json.toString()), (request) -> {
            request.setHeader("content-type", "application/json;");
        })
                .onSuccess(this::handleSuccees)
                .onFailure(this::handleFailed);

        return this;
    }

    public RunTestCase createHttp() {

        client = new RestClient();
        client.create((clientBuilder) -> clientBuilder.disableRedirectHandling());

        return this;
    }

    void handleSuccees(CloseableHttpResponse response, BodyResponse bodyResponse) {
        bodyResponse.streamCookiesResponse(this::handleCookie);
//        System.out.println(">> response succes : " + bodyResponse.getContenty() + "\ncookie " + new MapCookies(mapCookieResponse).toString());
    }

    void handleFailed(int status, CloseableHttpResponse response, BodyResponse bodyResponse) {
        System.out.println(">> response faile : " + status + " - " + bodyResponse.getContenty());
    }

    void handleCookie(Cookie cookieRes) {
        mapCookieResponse.put(cookieRes.getName(), cookieRes.getValue());
    }

    String encodeBase64(String value) {
        return new String(Base64.getEncoder().encode(value.getBytes(StandardCharsets.UTF_8)));
    }

}
