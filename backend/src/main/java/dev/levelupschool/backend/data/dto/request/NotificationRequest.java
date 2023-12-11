package dev.levelupschool.backend.data.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.thymeleaf.context.Context;

@Setter
@Getter
public class NotificationRequest {
    private String to;
    private String subject;
    private String content;
    private String sender;
    private Context context;
}
