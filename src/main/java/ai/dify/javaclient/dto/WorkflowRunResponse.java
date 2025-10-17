package ai.dify.javaclient.dto;

import lombok.Data;
import java.util.Map;

/**
 * Workflow run response for both blocking and streaming modes.
 *
 * This class represents the complete response from Dify Workflow API.
 *
 * Blocking Mode Response Example:
 * <pre>
 * {
 *   "workflow_run_id": "djflajgkldjgd",
 *   "task_id": "9da23599-e713-473b-982c-4328d4f5c78a",
 *   "data": {
 *     "id": "fdlsjfjejkghjda",
 *     "workflow_id": "fldjaslkfjlsda",
 *     "status": "succeeded",
 *     "outputs": {"text": "Nice to meet you."},
 *     "error": null,
 *     "elapsed_time": 0.875,
 *     "total_tokens": 3562,
 *     "total_steps": 8,
 *     "created_at": 1705407629,
 *     "finished_at": 1727807631
 *   }
 * }
 * </pre>
 */
@Data
public class WorkflowRunResponse {

  /**
   * Unique identifier for the workflow run execution
   */
  private String workflow_run_id;

  /**
   * Task ID for request tracking and cancellation
   */
  private String task_id;

  /**
   * Execution data wrapper containing all workflow execution details
   */
  private WorkflowRunData data;

  /**
   * Execution metadata - for compatibility with streaming mode responses
   */
  private Metadata metadata;

  /**
   * Workflow execution data containing all execution details.
   */
  @Data
  public static class WorkflowRunData {
    private String id;
    private String workflow_id;
    private String status;
    private Map<String, Object> outputs;
    private String error;
    private Double elapsed_time;
    private Integer total_tokens;
    private Integer total_steps;
    private Long created_at;
    private Long finished_at;
  }

  /**
   * Execution metadata for streaming mode responses.
   */
  @Data
  public static class Metadata {
    private Long latency;
    private TokenUsage usage;
    private Object[] retriever_resources;

    /**
     * Token usage statistics.
     */
    @Data
    public static class TokenUsage {
      private Integer prompt_tokens;
      private Integer completion_tokens;
      private Integer total_tokens;
      private Double total_price;
      private String currency;
    }
  }
}
