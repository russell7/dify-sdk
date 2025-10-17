package ai.dify.javaclient.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Workflow run request for Dify Workflow API.
 *
 * This class represents a request to execute a workflow with specified inputs.
 *
 * Required fields:
 * - inputs: Workflow input variables
 * - response_mode: "streaming" or "blocking"
 * - user: User identifier
 *
 * Optional fields:
 * - files: File uploads for the workflow
 * - trace_id: Trace ID for request tracking (distributed tracing)
 *
 * Example:
 * <pre>
 * {
 *   "inputs": {"query": "what is AI?"},
 *   "response_mode": "blocking",
 *   "user": "user-123",
 *   "files": [],
 *   "trace_id": "trace-uuid"
 * }
 * </pre>
 */
@Data
public class WorkflowRunRequest {

    /**
     * Workflow input variables/parameters (Required)
     *
     * Map of key-value pairs where keys correspond to workflow input variables
     * and values are the input data
     */
    private Map<String, Object> inputs;

    /**
     * User identifier (Required)
     *
     * Unique identifier for the end user executing the workflow
     * Used for audit logging and tracking
     */
    private String user;

    /**
     * Response mode (Required)
     *
     * Determines how the API returns results:
     * - "blocking": Wait for complete execution and return full result (may timeout after 100s)
     * - "streaming": Return results as server-sent events (SSE) in real-time
     */
    private String response_mode;

    /**
     * File uploads for the workflow (Optional)
     *
     * List of files to be processed by the workflow
     * Each file can be uploaded separately or referenced by upload_file_id
     */
    private List<FileUpload> files;

    /**
     * Trace ID for distributed tracing (Optional)
     *
     * Can be passed via:
     * - HTTP Header: X-Trace-Id (highest priority)
     * - URL Query Parameter: trace_id
     * - Request Body: trace_id (this field, lowest priority)
     *
     * Used to correlate workflow execution with other system traces
     */
    private String trace_id;

    /**
     * File upload information for workflow inputs
     */
    @Data
    public static class FileUpload {

        /**
         * Transfer method for the file
         * - "remote_url": File is at a remote URL
         * - "local_file": File is uploaded locally
         */
        private String transfer_method;

        /**
         * Remote URL (only when transfer_method is "remote_url")
         */
        private String url;

        /**
         * Upload file ID (only when transfer_method is "local_file")
         *
         * Obtained from the /files/upload endpoint
         */
        private String upload_file_id;

        /**
         * File type for document/image/audio/video files
         *
         * Document types: TXT, MD, MARKDOWN, PDF, HTML, XLSX, XLS, DOCX, CSV, EML, MSG, PPTX, PPT, XML, EPUB
         * Image types: JPG, JPEG, PNG, GIF, WEBP, SVG
         * Audio types: MP3, M4A, WAV, WEBM, AMR
         * Video types: MP4, MOV, MPEG, MPGA
         * Custom types: other file types
         */
        private String type;
    }
}
