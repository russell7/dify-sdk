package ai.dify.javaclient.dto;

import lombok.Data;
import java.util.Arrays;
import java.util.Map;

/**
 * Workflow streaming response event.
 *
 * Represents different event types from Dify Workflow API streaming response.
 * Different event types have different data structures.
 *
 * Event Types:
 * - workflow_started: Workflow execution started
 * - node_started: Workflow node started execution
 * - text_chunk: Text output chunk
 * - node_finished: Workflow node finished execution
 * - workflow_finished: Workflow execution completed
 * - tts_message: Text-to-speech audio message
 * - tts_message_end: Text-to-speech stream ended
 * - ping: Connection keep-alive ping (no additional data)
 */
@Data
public class WorkflowEvent {

    // Event type constants
    public static final String EVENT_WORKFLOW_STARTED = "workflow_started";
    public static final String EVENT_NODE_STARTED = "node_started";
    public static final String EVENT_TEXT_CHUNK = "text_chunk";
    public static final String EVENT_NODE_FINISHED = "node_finished";
    public static final String EVENT_WORKFLOW_FINISHED = "workflow_finished";
    public static final String EVENT_TTS_MESSAGE = "tts_message";
    public static final String EVENT_TTS_MESSAGE_END = "tts_message_end";
    public static final String EVENT_PING = "ping";

    /**
     * Event type indicator
     */
    private String event;

    /**
     * Workflow run identifier
     */
    private String workflow_run_id;

    /**
     * Task identifier for tracking
     */
    private String task_id;

    /**
     * Event-specific data (structure varies by event type)
     */
    private Object data;

    /**
     * Event creation timestamp
     */
    private Long created_at;

    // Additional fields for specific event types
    private String conversation_id;  // For TTS events
    private String message_id;        // For TTS events
    private String audio;             // For TTS events


    /**
     * Event data for workflow_started event
     */
    @Data
    public static class WorkflowStartedData {
        private String id;
        private String workflow_id;
        private Long created_at;

    }

    /**
     * Event data for node_started event
     */
    @Data
    public static class NodeStartedData {
        private String id;
        private String node_id;
        private String node_type;
        private String title;
        private Integer index;
        private String predecessor_node_id;
        private Map<String, Object> inputs;
        private Long created_at;

    }

    /**
     * Event data for text_chunk event
     */
    @Data
    public static class TextChunkData {
        private String text;
        private String[] from_variable_selector;

    }

    /**
     * Event data for node_finished event
     */
    @Data
    public static class NodeFinishedData {
        private String id;
        private String node_id;
        private Integer index;
        private String predecessor_node_id;
        private Map<String, Object> inputs;
        private Map<String, Object> outputs;
        private String status;
        private String error;
        private Double elapsed_time;
        private Map<String, Object> execution_metadata;
        private Long created_at;

    }

    /**
     * Event data for workflow_finished event
     */
    @Data
    public static class WorkflowFinishedData {
        private String id;
        private String workflow_id;
        private String status;
        private Map<String, Object> outputs;
        private String error;
        private Double elapsed_time;
        private Integer total_tokens;
        private String total_steps;
        private Long created_at;
        private Long finished_at;

    }

    /**
     * Helper method to identify event type
     */
    public boolean isEventType(String type) {
        return type != null && type.equals(this.event);
    }

    /**
     * Get typed data for specific event type
     */
    public <T> T getDataAs(Class<T> dataClass) {
      if (data == null) {
        return null;
      }
      if (dataClass.isInstance(data)) {
        return dataClass.cast(data);
      }
        // Handle if data is a Map (from JSON deserialization)
      if (data instanceof Map) {
        // Would need JSON mapper to convert Map to target class
        return null;
      }
        return null;
    }
}
