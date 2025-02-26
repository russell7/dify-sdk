package ai.dify.javaclient.dto;

import lombok.Data;

/**
 * TODO 建立继承式结构
 */
@Data
public class ChatMessageResponse {

  private String event;
  private String message_id;
  private String conversation_id;
  private String mode;
  private String answer;
  private Metadata metadata;
  private long created_at;

  @Data
  public static class Metadata {
    private Usage usage;
    private RetrieverResource[] retriever_resources;

    @Data
    public static class Usage {
      private int prompt_tokens;
      private String prompt_unit_price;
      private String prompt_price_unit;
      private String prompt_price;
      private int completion_tokens;
      private String completion_unit_price;
      private String completion_price_unit;
      private String completion_price;
      private int total_tokens;
      private String total_price;
      private String currency;
      private double latency;
    }

    @Data
    public static class RetrieverResource {
      private int position;
      private String dataset_id;
      private String dataset_name;
      private String document_id;
      private String document_name;
      private String segment_id;
      private double score;
      private String content;
    }
  }
}
