package ai.dify.javaclient.http;

/**
 * Represents an API route in the Dify system.
 * <p>
 * Encapsulates the HTTP method and URL path for Dify API endpoints.
 * </p>
 *
 * @author Ziyao_Zhu
 */
public class DifyRoute {
    /**
     * HTTP method for the route (e.g., "GET", "POST", "PUT", "DELETE", "PATCH")
     */
    public String method;

    /**
     * URL path for the route (may include format placeholders like %s)
     */
    public String url;

    /**
     * Constructs a new DifyRoute with the specified HTTP method and URL.
     *
     * @param method The HTTP method to use for this route
     * @param url    The URL path for this route
     */
    public DifyRoute(String method, String url) {
        this.method = method;
        this.url = url;
    }
}