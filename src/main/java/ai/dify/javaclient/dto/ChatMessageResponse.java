package ai.dify.javaclient.dto;

import lombok.Data;

/**
 * Chat message response DTO.
 * <p>
 * TODO: Establish inheritance structure to share common fields across response types
 * </p>
 */
@Data
public class ChatMessageResponse {

  /**
   * Event type identifier
   */
  private String event;

  /**
   * Unique identifier for the message
   */
  private String message_id;

  /**
   * Unique identifier for the conversation
   */
  private String conversation_id;

  /**
   * Chat mode (e.g., "chat", "completion")
   */
  private String mode;

  /**
   * The generated answer/response text
   */
  private String answer;

  /**
   * Response metadata including usage and retriever information
   */
  private Metadata metadata;

  /**
   * Timestamp when the message was created
   */
  private long created_at;

  /**
   * Metadata containing usage statistics and retriever resources
   */
  @Data
  public static class Metadata {
    /**
     * Token usage statistics
     */
    private Usage usage;

    /**
     * Retrieved resources from knowledge base/retriever
     */
    private RetrieverResource[] retriever_resources;

    /**
     * Token usage and pricing information
     */
    @Data
    public static class Usage {
      /**
       * Number of tokens in the prompt
       */
      private int prompt_tokens;

      /**
       * Unit price per prompt token
       */
      private String prompt_unit_price;

      /**
       * Price unit for prompt (e.g., "USD")
       */
      private String prompt_price_unit;

      /**
       * Total price for prompt tokens
       */
      private String prompt_price;

      /**
       * Number of tokens in the completion
       */
      private int completion_tokens;

      /**
       * Unit price per completion token
       */
      private String completion_unit_price;

      /**
       * Price unit for completion (e.g., "USD")
       */
      private String completion_price_unit;

      /**
       * Total price for completion tokens
       */
      private String completion_price;

      /**
       * Total number of tokens (prompt + completion)
       */
      private int total_tokens;

      /**
       * Total price for all tokens
       */
      private String total_price;

      /**
       * Currency code (e.g., "USD", "CNY")
       */
      private String currency;

      /**
       * API call latency in seconds
       */
      private double latency;
    }

    /**
     * Resource retrieved from knowledge base
     */
    @Data
    public static class RetrieverResource {
      /**
       * Position/rank of the retrieved resource
       */
      private int position;

      /**
       * Unique identifier of the dataset
       */
      private String dataset_id;

      /**
       * Name of the dataset
       */
      private String dataset_name;

      /**
       * Unique identifier of the document
       */
      private String document_id;

      /**
       * Name of the document
       */
      private String document_name;

      /**
       * Unique identifier of the segment
       */
      private String segment_id;

      /**
       * Relevance score of the retrieved resource
       */
      private double score;

      /**
       * Content of the retrieved segment
       */
      private String content;
    }
  }
}
