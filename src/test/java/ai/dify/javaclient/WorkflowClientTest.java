package ai.dify.javaclient;

import ai.dify.javaclient.dto.WorkflowRunResponse;
import ai.dify.javaclient.dto.WorkflowRunResponse.WorkflowRunData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for Dify Workflow Client.
 * <p>
 * Tests the WorkflowClient with real or test Dify API endpoints.
 * Configuration is loaded from application-test.properties file via ClassLoader.
 * </p>
 *
 * <h3>Configuration:</h3>
 * <p>Update src/test/resources/application-test.properties with:</p>
 * <ul>
 *   <li>dify.api.base-url - Your Dify API base URL</li>
 *   <li>dify.api.key - Your API key from Dify console (already bound to a specific workflow)</li>
 *   <li>dify.test.user - Test user identifier (optional, defaults to "test-user")</li>
 * </ul>
 * <p>Note: workflow_id is not required - it's implicit in the API key.</p>
 *
 * <h3>Running tests:</h3>
 * <pre>
 * mvn clean test                                    # Run all tests
 * mvn clean test -Dtest=WorkflowClientTest         # Run this test class
 * mvn clean test -Dtest=WorkflowClientTest#testWorkflowExecution  # Run specific method
 * </pre>
 *
 * @author Dify SDK
 * @see WorkflowClient
 */
@DisplayName("WorkflowClient Integration Test")
public class WorkflowClientTest {

    private String baseUrl;
    private String apiKey;
    private String testUser;
    private int maxExecutionTime;

    private WorkflowClient workflowClient;

    /**
     * Set up the WorkflowClient before each test.
     * Loads configuration from application-test.properties.
     */
    @BeforeEach
    void setUp() {
        loadTestConfiguration();
        workflowClient = new WorkflowClient(apiKey, baseUrl);
    }

    /**
     * Load test configuration from properties file.
     * Note: workflow_id is not loaded - it's implicit in the API key.
     */
    private void loadTestConfiguration() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application-test.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application-test.properties");
            }
            props.load(input);

            baseUrl = props.getProperty("dify.api.base-url");
            apiKey = props.getProperty("dify.api.key");
            testUser = props.getProperty("dify.test.user", "test-user");
            maxExecutionTime = Integer.parseInt(
                props.getProperty("dify.test.max-execution-time", "60000")
            );

            System.out.println("Test Configuration Loaded:");
            System.out.println("  Base URL: " + baseUrl);
            System.out.println("  Test User: " + testUser);

        } catch (IOException e) {
            throw new RuntimeException("Error loading test configuration", e);
        }
    }

    /**
     * Test basic workflow execution with simple inputs.
     * <p>
     * This test runs a workflow synchronously and verifies the response.
     * </p>
     *
     * @throws DifyClientException if workflow execution fails
     */
    @Test
    @DisplayName("Test workflow execution with sample query")
    public void testWorkflowExecution() throws DifyClientException {
        // Arrange
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("query", "分析2024年第一季度的销售数据");
        inputs.put("role", "FINANCIAL_ANALYST");

        // Act
        WorkflowRunResponse response = workflowClient.runWorkflowSync(inputs, testUser);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getWorkflow_run_id(), "Workflow run ID should not be null");
      WorkflowRunData data = response.getData();
      assertEquals("succeeded", data.getStatus(), "Workflow should complete successfully");
        assertNotNull(data.getOutputs(), "Outputs should not be null");

        // Log results for manual verification
        System.out.println("Workflow Run ID: " + response.getWorkflow_run_id());
        System.out.println("Status: " + data.getStatus());
        System.out.println("Outputs: " + data.getOutputs());
    }

    /**
     * Test workflow execution with multiple input parameters.
     * <p>
     * Verifies that the workflow can handle complex input structures.
     * </p>
     *
     * @throws DifyClientException if workflow execution fails
     */
    @Test
    @DisplayName("Test workflow execution with multiple inputs")
    public void testWorkflowWithMultipleInputs() throws DifyClientException {
        // Arrange
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("query", "What are the Q1 sales trends?");
        inputs.put("role", "FINANCIAL_ANALYST");
        inputs.put("dataSource", "sales_database");
        inputs.put("timeRange", "2024-Q1");

        // Act
        WorkflowRunResponse response = workflowClient.runWorkflowSync(inputs, testUser);

        // Assert
        assertNotNull(response, "Response should not be null");

        // Verify metadata if available
        if (response.getMetadata() != null) {
            System.out.println("Latency: " + response.getMetadata().getLatency() + " ms");
            if (response.getMetadata().getUsage() != null) {
                System.out.println("Total tokens: " + response.getMetadata().getUsage().getTotal_tokens());
            }
        }
    }

    /**
     * Test workflow error handling with invalid inputs.
     * <p>
     * Verifies that appropriate exceptions are thrown for invalid inputs.
     * </p>
     */
    @Test
    @DisplayName("Test workflow error handling with invalid inputs")
    public void testWorkflowErrorHandling() {
        // Arrange
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("query", ""); // Empty query should fail

        // Act & Assert
        assertThrows(DifyClientException.class, () -> {
            workflowClient.runWorkflowSync(inputs, testUser);
        }, "Should throw exception for invalid inputs");
    }

    /**
     * Test workflow execution timing and performance.
     * <p>
     * Verifies that workflow execution completes within acceptable time limits.
     * </p>
     *
     * @throws DifyClientException if workflow execution fails
     */
    @Test
    @DisplayName("Test workflow execution timing")
    public void testWorkflowExecutionTiming() throws DifyClientException {
        // Arrange
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("query", "Quick analysis test");
        inputs.put("role", "OPERATIONS_ANALYST");

        // Act
        long startTime = System.currentTimeMillis();
        WorkflowRunResponse response = workflowClient.runWorkflowSync(inputs, testUser);
        long endTime = System.currentTimeMillis();

        long executionTime = endTime - startTime;

        // Assert
        assertNotNull(response, "Response should not be null");
        assertTrue(executionTime < maxExecutionTime,
            "Workflow should complete within " + maxExecutionTime + " milliseconds");

        System.out.println("Workflow execution time: " + executionTime + " ms");
    }

    /**
     * Test workflow execution with empty inputs.
     * <p>
     * Verifies that workflow can handle empty or minimal input sets.
     * </p>
     *
     * @throws DifyClientException if workflow execution fails
     */
    @Test
    @DisplayName("Test workflow execution with empty inputs")
    public void testWorkflowWithEmptyInputs() throws DifyClientException {
        // Arrange
        Map<String, Object> inputs = new HashMap<>();

        // Act
        WorkflowRunResponse response = workflowClient.runWorkflowSync(inputs, testUser);

        // Assert
        assertNotNull(response, "Response should not be null");
    }

    /**
     * Test workflow status retrieval.
     * <p>
     * Verifies that workflow status can be queried after execution.
     * </p>
     *
     * @throws DifyClientException if status retrieval fails
     */
    @Test
    @DisplayName("Test workflow status retrieval")
    public void testWorkflowStatus() throws DifyClientException {
        // First run a workflow
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("query", "Test query for status check");

        WorkflowRunResponse runResponse = workflowClient.runWorkflowSync(inputs, testUser);
        String workflowRunId = runResponse.getWorkflow_run_id();

        // Then get its status
        okhttp3.Response statusResponse = workflowClient.getWorkflowStatus(workflowRunId);

        assertNotNull(statusResponse, "Status response should not be null");
        assertTrue(statusResponse.isSuccessful(), "Status request should succeed");
    }
}
