package Web;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linzf
 * @since 2019-06-13
 * 类描述：socket的实现类
 */
@Service(value = "socketIOService")
public class SocketIoServiceImpl implements SocketIoService {

    /**
     * 用来存已连接的客户端
     */
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();
    private static Map<String, String> combatant = new ConcurrentHashMap<>();
    private static Map<String, String> username_ip = new ConcurrentHashMap<>();
    /**
     * socketIo的对象
     */
    @Autowired
    private SocketIOServer socketIOServer;

    /**
     * 功能描述：当前的service被初始化的时候执行以下的方法
     * @throws Exception
     */
    @PostConstruct
    private void autoStartUp() throws Exception {
        start();
    }

    /**
     * 功能描述：当我们的系统停止的时候关闭我们的socketIo
     * @throws Exception
     */
    @PreDestroy
    private void autoStop() throws Exception {
        stop();
    }

    @Override
    public void start() throws Exception {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            /**
             * 此处实现我们的socket的连接的用户的逻辑，此处我前端传的是loginUser这个参数，大家可以根据自己的情况来定义入参
             */
            String loginUser = getParamsByClient(client).get("loginUser").get(0);
            System.out.println("客户端连接成功，当前连接的用户为：" + loginUser);
            clientMap.put(loginUser, client);
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String loginUser = getParamsByClient(client).get("loginUser").get(0);
            System.out.println("客户端断开连接，当前连接的用户为：");
            if (loginUser != null && !"".equals(loginUser)) {
                clientMap.remove(loginUser);
                client.disconnect();
            }
        });

//        // 监听发送到客户端的消息
//        socketIOServer.addEventListener(SEND_EVENT, PushMessage.class, (client, data, ackSender) -> {
//            // TODO do something
//        });
        
        // 监听来自客户端的消息
        socketIOServer.addEventListener(RECEIVE_EVENT , JSONObject.class, (client, data, ackSender) -> {
            String ip = getIpByClient(client);
            if (ip != null)
            {
                System.out.println(ip + " ************ 客户端："+ data.toJSONString());
            }
        });
        
        socketIOServer.start();

    }

    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    /**
     * 功能描述：发送消息到前端
     * @param pushMessage 发送消息的实体
     */
    @Override
    public void pushMessageToUser(PushMessage pushMessage) {
        clientMap.get(pushMessage.getLoginUser()).sendEvent(SEND_EVENT, pushMessage);
    }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     *
     * @param client
     * @return
     */
    private Map<String, List<String>> getParamsByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        return params;
    }

    /**
     * 功能描述：获取用户ip
     * @param client
     * @return
     */
    private String getIpByClient(SocketIOClient client)
    {
        String ip = client.getRemoteAddress().toString();
        System.out.println("ip = " + ip);
        return ip.substring(1, ip.indexOf(":"));
    }
}