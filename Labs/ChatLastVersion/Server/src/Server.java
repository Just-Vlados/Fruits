import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class Server implements HttpHandler {

    private List<Message> messageList = new ArrayList<Message>();
    private MessageExchange messageExchange = new MessageExchange();
    private int currentID = 0;
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Server port");
            return;
        }
        try {
            System.out.println("Server is starting...");
            Integer port = Integer.parseInt(args[0]);
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            System.out.println("Server started.");
            String serverHost = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Get list of tasks: GET http://" + serverHost + ":" + port + "/chat?token={token}");
            System.out.println("Add new task: POST http://" + serverHost + ":" + port + "/chat");
            System.out.println("Edit task: PUT http://" + serverHost + ":" + port + "/chat?id={id}");

            server.createContext("/chat", new Server());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            System.out.println("Error creating http server");
            e.printStackTrace();
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        String response = "";

        try {
            System.out.println("Begin request " + httpExchange.getRequestMethod());
            String query = httpExchange.getRequestURI().getQuery();
            System.out.println("Query " + query);

            if ("GET".equals(httpExchange.getRequestMethod())) {
                response = doGet(httpExchange);
            } else if ("POST".equals(httpExchange.getRequestMethod())) {
                doPost(httpExchange);
            } else if ("PUT".equals(httpExchange.getRequestMethod())) {
                doPut(httpExchange);
            } else if ("OPTIONS".equals(httpExchange.getRequestMethod())) {
                response = "";
            } else {
                throw new Exception("Unsupported http method: " + httpExchange.getRequestMethod());
            }

            System.out.println(response);
            sendResponse(httpExchange, response);
            System.out.println("Response sent, size " + response.length());
            System.out.println("End request " + httpExchange.getRequestMethod());
            return;
        } catch (Exception e) {
            response = messageExchange.getErrorMessage(e.getMessage());
            e.printStackTrace();
        } 

        try{
            sendResponse(httpExchange, response);
        } catch(Exception e) {
            System.out.println("Unable to send response !");
        }
    }

    private String doGet(HttpExchange httpExchange) throws Exception {
        String query = httpExchange.getRequestURI().getQuery();
        if (query != null) {
            Map<String, String> map = queryToMap(query);
            String token = map.get("token");
            if (token != null && !"".equals(token)) {
                int index = messageExchange.getIndex(token);
                return messageExchange.getServerResponse(messageList.subList(index, messageList.size()));
            } else {
                return "Token query parameter is absent in url: " + query;
            }
        }
        return  "Absent query in url";
    }

    private void doPost(HttpExchange httpExchange) throws Exception {
        try {
            Message message = messageExchange.getClientMessage(httpExchange.getRequestBody());
            synchronized (this) {
                message.setId(currentID++);
            }
            messageList.add(message);
            System.out.println("Get Message from User: " + message.getUserName()+message.getText());
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void doPut(HttpExchange httpExchange) throws Exception {
        try {
            Message messageChange = messageExchange.getClientMessage(httpExchange.getRequestBody());
            int idOfChangeMessage =  messageChange.getId();
            if(idOfChangeMessage >= 0 && idOfChangeMessage < messageList.size()) {
                if(!messageChange.isDeleted()) {
                    Message message = messageList.get(idOfChangeMessage);
                    message.setText(messageChange.getText());
                    message.setChanged(true);
                    System.out.println("Change :" + message.toJSONString());
                }
            }
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        byte[] bytes = response.getBytes();
        Headers headers = httpExchange.getResponseHeaders();

        headers.add("Access-Control-Allow-Origin","*");
        if("OPTIONS".equals(httpExchange.getRequestMethod())) {
            headers.add("Access-Control-Allow-Methods","PUT, DELETE, POST, GET, OPTIONS");
        }
        httpExchange.sendResponseHeaders(200, bytes.length);
        writeBody(httpExchange, bytes);
    }

    private void writeBody(HttpExchange httpExchange, byte[] bytes) throws IOException {
        OutputStream os = httpExchange.getResponseBody();

        os.write( bytes);
        os.flush();
        os.close();
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();

        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
