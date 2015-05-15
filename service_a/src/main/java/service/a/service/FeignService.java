package service.a.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.a.bean.MessageBean;

import java.util.UUID;

/**
 * @author Oreste Luci
 */
@Component
public class FeignService extends AbstractService {

    @Autowired
    private ServiceBClient serviceBClient;

    @HystrixCommand(fallbackMethod = "fallback")
    public MessageBean feignClient() {
        String uuid = UUID.randomUUID().toString();
        MessageBean messageBean = serviceBClient.getMessage(uuid);
        System.out.println(messageBean.toString());
        return messageBean;
    }

    private MessageBean fallback() {
        return new MessageBean("Fallback Method");
    }

    @FeignClient(FeignService.CLIENT_SERVICE)
    public interface ServiceBClient {

        @RequestMapping(method = RequestMethod.GET, value = "/echo?msg={msg}")
        MessageBean getMessage(@PathVariable(value = "msg") String msg);
    }
}
