import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AzureBlobStorageDriver {

    private final BlobServiceClient blobServiceClient;

    public AzureBlobStorageDriver(String connectionString) {
        this.blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
    }

    public void uploadBlob(String containerName, String blobName, byte[] data) {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(blobName);
        try (InputStream dataStream = new ByteArrayInputStream(data)) {
            blobClient.upload(dataStream, data.length, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] downloadBlob(String containerName, String blobName) {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(blobName);
        try (InputStream blobStream = blobClient.openInputStream()) {
            byte[] data = blobStream.readAllBytes();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteBlob(String containerName, String blobName) {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(blobName);
        blobClient.delete();
    }

    // Дополнительные методы для работы с контейнерами и другими операциями можно добавить здесь

    public static void main(String[] args) {
        // Пример использования
        String connectionString = "<your_connection_string>";
        AzureBlobStorageDriver driver = new AzureBlobStorageDriver(connectionString);

        String containerName = "mycontainer";
        String blobName = "myblob.txt";
        String text = "Hello, Azure Blob Storage!";
        byte[] data = text.getBytes();

        // Загрузка файла в хранилище
        driver.uploadBlob(containerName, blobName, data);

        // Скачивание файла из хранилища
        byte[] downloadedData = driver.downloadBlob(containerName, blobName);
        if (downloadedData != null) {
            System.out.println(new String(downloadedData));
        }

        // Удаление файла из хранилища
        driver.deleteBlob(containerName, blobName);
    }
}
