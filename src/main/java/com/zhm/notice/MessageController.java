package com.zhm.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    /**
     * 发送消息接口测试
     * http://localhost:9090/sendMsg?msg=test&userid=接受消息的用户ID
     *
     * @param msg
     * @param userid
     * @return
     */
    @RequestMapping(value = "/sendMsg",method = RequestMethod.POST)
    public @ResponseBody
    String sendMsg(String msg, String userid){
        Arrays.stream(userid.split(","))
                .forEach(uid->
                        simpMessagingTemplate.
                                convertAndSend("/topic/notice/"+uid,new ReturnMessage(msg)));
        return "Send Success";
    }


    @Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次
    public void autoSendAllMsg(){
        simpMessagingTemplate.convertAndSend("/topic//all",
                new ReturnMessage(LocalDateTime.now().
                        format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

    @Scheduled(cron="0/9 * *  * * ? ")   //每9秒执行一次
    public void autoSendSingleMsg(){
        simpMessagingTemplate.convertAndSend("/topic/notice/5788dbd970f00b24c2812c54",new ReturnMessage("收到消息！"));
    }

}
