package com.github.paicoding.forum.core.ai;

import com.github.paicoding.forum.core.util.JsonUtil;
import com.plexpt.chatgpt.entity.chat.ChatChoice;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@Accessors(chain = true)
public class ChatRecord {
    /**
     * 提问内容
     */
    private String qas;
    /**
     * 提问时间
     */
    private Long qasTime;
    private List<ChatChoice> res;
    private ChatRecord next;
    private ChatRecord pre;
    private volatile boolean lastReturn;
    private String sysErr;

    public ChatRecord() {
        qasTime = 0L;
        sysErr = null;
        lastReturn = false;
    }

    /**
     * 之前没有回复过，且chatgpt出错，或者有结果了，才能继续回复
     *
     * @return
     */
    public boolean canReply() {
        return !lastReturn && (sysErr != null || res != null);
    }

    public String reply() {
        lastReturn = true;
        if (!CollectionUtils.isEmpty(res)) {
            return qas + "\n--------------------\n" + JsonUtil.toStr(res);
        }

        return sysErr;
    }
}