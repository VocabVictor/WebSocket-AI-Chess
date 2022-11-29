package Web;

public interface SocketIoService {

    /**
     * 推送的事件
     */
    String SEND_EVENT = "send_event";
    String RECEIVE_EVENT = "receive_event";
    /**
     * 启动服务
     *
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * 停止服务
     */
    void stop();

    /**
     * 推送信息
     *
     * @param pushMessage
     */
    void pushMessageToUser(PushMessage pushMessage);

}