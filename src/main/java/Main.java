import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=Lg8mGBNOHMwfNw1xOFdWBflGkZz4a1xcUtj45IHB");
        CloseableHttpResponse response = httpClient.execute(request);

        Image image = mapper.readValue(response.getEntity().getContent(), Image.class);

        String imageUrl = image.getUrl();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);

        HttpGet requestUrl = new HttpGet(imageUrl);
        CloseableHttpResponse responseUrl = httpClient.execute(requestUrl);
        byte[] content = EntityUtils.toByteArray(responseUrl.getEntity());

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(content);
        }

        System.out.println("Файл успешно сохранен как: " + fileName);
    }
}

