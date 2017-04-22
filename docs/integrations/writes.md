# Writing to Sidewinder
Writing data to Sidewinder is easy. There's support for a wide variety of input protocols and you can pick up ones that provide your team maximum comfort level and meets the requirements of your business domain.

### OpenTSDB protocol over HTTP

### InfluxDB Wire Protocol over HTTP
InfluxData has a wire protocol that defines a text based format to ingest time series data. Here's how the format looks like:
```
[measurement name],[tags] [field1=value1,field2=value2...] [timestamp]
```

|Field|Validation Constraints|
|-----|----------------------|
|Measurement Name|Contains valid characters a-z, A-Z, 0-9, -, _|
|Tags|Contains valid characters a-z, A-Z, 0-9, -, _, =|
|Field Names|Contains valid characters a-z, A-Z, 0-9, -, _|
|Field Value|Floating and non-floating point data; non-fp data must have 'i' suffix|
|Timestamp|Epoch timestamp Jan 1, 1970 in nano seconds (Sidewinder only stores millisecond resolution)|

An example of a simple POST request to Sidewinder using the InfluxDB wire protocol:
```
curl -XPOST http://localhost:8080/influx?db=<dbName> 'cpu,host=h1.sidewinder.local usr=1.22,sys=2i 1434055562000000000'
```

**Sample Java code using Apache Http Commons Client:**

```java
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class WriteToSidewinder {
	public static void main(String[] args) throws Exception {
    HttpClientBuilder clientBuilder = HttpClients.custom(); // Create an HTTP client Builder
		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).build(); // Build request configurations
		CloseableHttpClient dbC = clientBuilder.setDefaultRequestConfig(config).build(); // Create the client
		long ts = System.currentTimeMillis(); // Start timestamp for the data points
		StringBuilder builder = new StringBuilder(); // Buffer for the batch request
		for (int k = 0; k < 10; k++) {
			for (int i = 0; i < 1_000; i++) {
				builder.append("cpu,host="+k+".sidewinder.local usr=" + i + "i " + ((ts + i) * 1000 * 1000) + "\n"); // Fabricate the write payload
			}
		}
    String URL = "http://localhost:8080/influx?db=test"; // URL for Sidewinder
		HttpPost db = new HttpPost(URL); // create the HTTP POST request
		db.setEntity(new StringEntity(builder.toString())); // Set the payload
		CloseableHttpResponse execute = dbC.execute(db); // Execute request and capture the response
		System.out.println(execute.getStatusLine()); // Print the Response
		execute.close(); // Close the connection
	}
}
```

### Binary protocol over TCP

### GRPC protocol
