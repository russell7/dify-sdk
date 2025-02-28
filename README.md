# Dify Java SDK
This is the Java SDK for the Dify API, which allows you to seamlessly integrate Dify into your Java applications.

## Deploy

put the following code in your settings.xml, chant repo.domain to your nexus server.
see [guide large scale centralized deployments](https://maven.apache.org/guides/mini/guide-large-scale-centralized-deployments.html)
```xml
<properties>
  <altReleaseDeploymentRepository>releases::default::http://repo.domain/repository/maven-releases/</altReleaseDeploymentRepository>
  <altSnapshotDeploymentRepository>snapshots::default::http://repo.domain/repository/maven-snapshots/</altSnapshotDeploymentRepository>
</properties>
```
```shell
mvn clean deploy
```

## Usage
Once the SDK is deployed/installed, you can use it in your project as follows:

```xml
<dependency>
    <groupId>ai.dify</groupId>
    <artifactId>dify-sdk</artifactId>
    <version>1.1.0-SNAPSHOT</version>
</dependency>
```

```java
import ai.dify.javaclient.DifyClient;
import ai.dify.javaclient.ChatClient;
import ai.dify.javaclient.CompletionClient;
import okhttp3.Response;

public class DifyApp {

    public static void main(String[] args) {
        String apiKey = System.getenv("AI_DIFY_API_KEY");
        // skip if you don't need to customize the base URL
        String baseUrl = System.getenv("AI_DIFY_BASE_URL");
        try {
            String user = "random-user-id";
            String inputs = "{\"name\":\"test name a\"}";
            String query = "Please tell me a short story in 10 words or less.";
            boolean responseMode = true;

            // Create a completion client
            CompletionClient completionClient = new CompletionClient(apiKey, baseUrl);
            Response completionResponse = completionClient.createCompletionMessage(inputs, query, user, responseMode);
            System.out.println(completionResponse.body().string());

            // Create a chat client
            ChatClient chatClient = new ChatClient(apiKey);
            // Create a chat message
            Response chatResponse = chatClient.createChatMessage(inputs, query, user, true, null);
            System.out.println(chatResponse.body().string());

            // Fetch conversations
            chatClient.getConversations(user);
            // Rename conversation
            String conversationId = "example-conversation-id";
            String name = "new-name";
            chatClient.renameConversation(conversationId, name, user);

            // And so on for other methods...

            DifyClient client = new DifyClient(apiKey);
            // Fetch application parameters
            client.getApplicationParameters(user);

            // Provide feedback for a message
            String messageId = "your-message-id";
            String rating = "5";
            client.messageFeedback(messageId, rating, user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Replace `'AI_DIFY_API_KEY'` with your actual Dify API key.

## License
This SDK is released under the MIT License.
