package TestCase;

import java.io.File;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import tungphamdev.oraclesun.RestclientVer2.RestClient;

public class TestUpload {

    public static void main(String[] args) {
        String pathImage = "D:\\netbean12.0\\FaceBook\\DebtForgiveness\\Image\\buttonStartShare.png";
        String pathImgChange = "C:\\Users\\Admin\\Desktop\\New folder\\regpage\\img\\Untitled32.png";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addTextBody("changer", "uls")
                .addBinaryBody("filePre", new File(pathImage))
                .addBinaryBody("fileBehin", new File(pathImgChange));
        RestClient client = new RestClient();

        client.create((clientBuilder) -> clientBuilder.disableRedirectHandling())
                .POST("http://localhost:8080/user/upload/img/chagne", client.MULTIPART(builder), (request) -> {
                })
                .onSuccess((response, bodyResponse) -> {
                    System.out.println(bodyResponse.getContenty());
                })
                .onFailure((statusCode, response, bodyResponse) -> {
                    System.out.println("err " + statusCode + " - " + bodyResponse.getContenty());
                });
    }
}
