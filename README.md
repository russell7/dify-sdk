# Dify Java SDK
This is the Java SDK for the Dify API, which allows you to seamlessly integrate Dify into your Java applications.

## Installation

For the sake of this README, let's assume the SDK is available on Maven Central:

```xml
<dependency>
    <groupId>ai.dify</groupId>
    <artifactId>dify-sdk</artifactId>
    <version>1.1.0-SNAPSHOT</version>
</dependency>
```

## Usage
Once the SDK is installed, you can use it in your project as follows:

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
