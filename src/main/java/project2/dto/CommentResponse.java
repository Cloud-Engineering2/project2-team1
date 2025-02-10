package project2.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {
	private final Long cid;
	private final Long pid;
    private final Long uid;
    private final String content;
}
