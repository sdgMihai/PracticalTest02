package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02.model.TimerInformation;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;
    private String timer = "timer";

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (hour / minute!");
            String hour = bufferedReader.readLine();
            String minute = bufferedReader.readLine();
            String type = bufferedReader.readLine();
            if (hour == null || hour.isEmpty() || minute == null || minute.isEmpty() || type == null || type.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (hour / minute / type!");
                return;
            }
            HashMap<String, TimerInformation> data = serverThread.getData();
            TimerInformation timerInformation = new TimerInformation("0", "0");
            if (data.containsKey(timer)) {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                timerInformation = data.get(timer);
            } else {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
//                Socket tsocket = new Socket("128.138.140.44", 13);
//                try{
//                    String pageSourceCode = "";
//                    if (tsocket == null) {
//                        Log.e(Constants.TAG, "[comm THREAD] edu Could not create socket!");
//                        return;
//                    }
//                    BufferedReader bufferedReaderEdu = Utilities.getReader(socket);
//                    PrintWriter printWriterEdu = Utilities.getWriter(socket);
//                    if (bufferedReader == null || printWriter == null) {
//                        Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
//                        return;
//                    }
//                    printWriterEdu.println(hour);
//                    printWriterEdu.flush();
//                    printWriterEdu.println(minute);
//                    printWriterEdu.flush();
//                } catch (IOException ioException) {
//                    Log.e(Constants.TAG, "[edui THREAD] An exception has occurred: " + ioException.getMessage());
//                    if (Constants.DEBUG) {
//                        ioException.printStackTrace();
//                    }
//                } finally {
//                    if (tsocket != null) {
//                        try {
//                            socket.close();
//                        } catch (IOException ioException) {
//                            Log.e(Constants.TAG, "[edui THREAD] An exception has occurred: " + ioException.getMessage());
//                            if (Constants.DEBUG) {
//                                ioException.printStackTrace();
//                            }
//                        }
//                    }
//                }
                timerInformation.setHour(hour);
                timerInformation.setMinute(minute);
                serverThread.setData(timer, timerInformation);
            }

            String result = null;
            result = "[COMMUNICATION THREAD] test information type " + type;
            printWriter.println(result);
            printWriter.flush();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } // catch (JSONException jsonException) {
//            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + jsonException.getMessage());
//            if (Constants.DEBUG) {
//                jsonException.printStackTrace();
//            }
//        }
        finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
