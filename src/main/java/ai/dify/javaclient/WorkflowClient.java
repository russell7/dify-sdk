package ai.dify.javaclient;

import ai.dify.javaclient.dto.WorkflowRunResponse;
import ai.dify.javaclient.helper.JsonUtil;
import ai.dify.javaclient.http.DifyRoute;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Client for interacting with Dify Workflow API.
 * Provides methods to run workflows, retrieve workflow status, and manage workflow executions.
 */
public class WorkflowClient extends DifyClient {
    ObjectMapper mapper = JsonUtil.buildMapper();

    /**
     * API route for running a workflow.
     * <p>Endpoint: POST /workflows/run</p>
     * <p>Note: Workflow is identified by the API key. No workflow_id parameter needed.</p>
     */
    public static final String WORKFLOW_RUN_ROUTE = "/workflows/run";

    /**
     * API route for retrieving workflow execution status.
     * <p>Endpoint: GET /workflows/run/{workflowRunId}</p>
     */
    public static final String WORKFLOW_STATUS_ROUTE = "/workflows/run/%s";

    /**
     * API route for stopping a running workflow task.
     * <p>Endpoint: POST /workflows/tasks/{taskId}/stop</p>
     */
    public static final String WORKFLOW_STOP_ROUTE = "/workflows/tasks/%s/stop";

    /**
     * Constructs a new WorkflowClient with the provided API key.
     *
     * @param apiKey The API key to use for authentication.
     */
    public WorkflowClient(String apiKey) {
        super(apiKey);
    }

    /**
     * Constructs a new WorkflowClient with the provided API key and base URL.
     *
     * @param apiKey   The API key to use for authentication.
     * @param baseUrl  The base URL of the Dify API.
     */
    public WorkflowClient(String apiKey, String baseUrl) {
        super(apiKey, baseUrl);
    }

    /**
     * Runs a workflow synchronously and waits for completion.
     *
     * @param inputs     The input variables for the workflow.
     * @param user       The user identifier for audit/logging purposes.
     * @return The workflow execution response.
     * @throws DifyClientException If an error occurs while executing the workflow.
     */
    public WorkflowRunResponse runWorkflowSync(Map<String, Object> inputs, String user) throws DifyClientException {
        Response response = runWorkflow(inputs, user, false);
        String body;
        try {
            assert response.body() != null;
            body = response.body().string();
        } catch (IOException e) {
            DifyClientException ex = new DifyClientException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }

        WorkflowRunResponse workflowResponse;
        try {
            workflowResponse = mapper.readValue(body, WorkflowRunResponse.class);
        } catch (JsonProcessingException e) {
            DifyClientException ex = new DifyClientException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
        return workflowResponse;
    }

    /**
     * Runs a workflow.
     *
     * @param inputs       The input variables for the workflow.
     * @param user         The user identifier for audit/logging purposes.
     * @param streaming    Whether to use streaming response mode.
     * @return The HTTP response containing the workflow execution result.
     * @throws DifyClientException If an error occurs while executing the workflow.
     */
    public Response runWorkflow(Map<String, Object> inputs, String user, boolean streaming) throws DifyClientException {
        ObjectNode json = mapper.createObjectNode();

        // Add workflow inputs
        if (inputs != null && !inputs.isEmpty()) {
            ObjectNode inputsNode = mapper.createObjectNode();
            for (Map.Entry<String, Object> entry : inputs.entrySet()) {
                inputsNode.putPOJO(entry.getKey(), entry.getValue());
            }
            json.set("inputs", inputsNode);
        }

        json.put("user", user);
        json.put("response_mode", streaming ? "streaming" : "blocking");

        // Use the fixed endpoint without workflow ID
        String url = WORKFLOW_RUN_ROUTE;

        return sendRequest(new DifyRoute("POST", url), null, createJsonPayload(json));
    }

    /**
     * Retrieves the status of a running workflow.
     *
     * @param workflowRunId The ID of the workflow run to check.
     * @return The HTTP response containing workflow run status.
     * @throws DifyClientException If an error occurs while retrieving the status.
     */
    public Response getWorkflowStatus(String workflowRunId) throws DifyClientException {
        String url = String.format(WORKFLOW_STATUS_ROUTE, workflowRunId);
        return sendRequest(new DifyRoute("GET", url), null, null);
    }

    /**
     * Stops a running workflow task.
     *
     * @param taskId The ID of the task to stop.
     * @param user   The user identifier who is stopping the task.
     * @return The HTTP response containing the result of the stop operation.
     * @throws DifyClientException If an error occurs while stopping the workflow.
     */
    public Response stopWorkflow(String taskId, String user) throws DifyClientException {
        ObjectNode json = mapper.createObjectNode();
        json.put("user", user);

        String url = String.format(WORKFLOW_STOP_ROUTE, taskId);
        return sendRequest(new DifyRoute("POST", url), null, createJsonPayload(json));
    }

    /**
     * Creates a request body with the given JSON object.
     *
     * @param jsonObject The JSON object to be used in the request body.
     * @return The created request body.
     */
    @Override
    okhttp3.RequestBody createJsonPayload(ObjectNode jsonObject) {
        try {
            return okhttp3.RequestBody.create(mapper.writeValueAsString(jsonObject),
                    okhttp3.MediaType.parse("application/json"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
