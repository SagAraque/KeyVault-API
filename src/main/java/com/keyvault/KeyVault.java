package com.keyvault;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.keyvault.entities.*;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.Map;

public class KeyVault {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;
    private Socket socket;
    private Object[] responseContent;

    private Tokens userToken;
    private boolean localhost = false;

    private final Map<Integer, String> serverMessages = Map.of(
            101, "Credenciales incorrectas",
            102, "Error de verificación",
            103, "Verificación fallida",
            104, "Credenciales ya registradas",
            200, "Operación correcta",
            201, "Token no válido",
            202, "Error en servidor",
            203, "Operación no soportada"
    );

    public KeyVault(){

    }

    public KeyVault(boolean localhost){
        this.localhost = localhost;
    }

    private void connect() throws IOException {
        socket = new Socket(localhost ? "localhost" : "129.151.227.217", 5556);
        bos = new BufferedOutputStream(socket.getOutputStream());
        out = new ObjectOutputStream(bos);
        out.flush();
        bis = new BufferedInputStream(socket.getInputStream());
        in = new ObjectInputStream(bis);
    }

    private void disconnect(){
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int login(Users user){
        try {

            connect();
            Response response = sendRequest(new Request(new Object[]{user, createDevice()}, Request.LOGIN));

            if(response.getResponseCode() == 200){
                userToken = (Tokens) response.getResponseContent()[0];
                disconnect();
            }

            disconnect();

            return response.getResponseCode();

        } catch (IOException | ClassNotFoundException e) {
            return 202;
        }
    }

    public int verifyLogin(String code, boolean saveDevice, Users user){
        try {
            connect();

            Response response = sendRequest(new Request(new Object[]{user, createDevice()}, Request.VERIFY));

            if(response.getResponseCode() == 102){
                out.writeUTF(code + "::" + (saveDevice ? 1 : 0));
                out.flush();

                response = (Response) in.readObject();

                if(response.getResponseCode() == 200)
                    userToken = (Tokens) response.getResponseContent()[0];

            }

            disconnect();

            return response.getResponseCode();

        } catch (IOException | ClassNotFoundException e) {
            return 202;
        }
    }

    public int register(Users user){
        return serverOperation(new Request(new Object[]{user, createDevice()}, Request.REGISTER));
    }

    public int getItems(){
        return serverOperation(new Request(Request.GET, userToken));
    }
    public int getDevices() { return serverOperation(new Request(Request.GET_DEVICES, userToken)); }

    public int modItem(Items item){
        return serverOperation(new Request(new Object[]{item},Request.MOD, userToken));
    }

    public int insertItem(Items item){
        return serverOperation(new Request(new Object[]{item},Request.INSERT, userToken));
    }

    public int deleteItem(Items item){
        return serverOperation(new Request(new Object[]{item},Request.DELETE, userToken));
    }

    public int totp(){
        return serverOperation(new Request(Request.TOTP, userToken));
    }

    public int clearDevices(){
        return serverOperation(new Request(Request.CLEAR_DEVICE, userToken));
    }

    public int deleteAccount(){
        return serverOperation(new Request(Request.DELETE_USER, userToken));
    }

    private int serverOperation(Request request){
        try{
            connect();

            Response response = sendRequest(request);

            disconnect();

            return response.getResponseCode();

        }catch (IOException | ClassNotFoundException e){
            return 202;
        }
    }

    private Response sendRequest(Request request) throws IOException, ClassNotFoundException {
        out.writeObject(request);
        out.flush();
        out.reset();
        bos.flush();

        Response response = (Response) in.readObject();
        responseContent = response.getResponseContent();

        return response;
    }

    public int generateQR(String qr){
        try{
            BitMatrix matrix = new MultiFormatWriter().encode(qr, BarcodeFormat.QR_CODE, 40, 40);
            FileOutputStream out = new FileOutputStream("qr.png");
            MatrixToImageWriter.writeToStream(matrix, "png", out);

            return 200;
        }catch (WriterException | IOException e){
            e.printStackTrace();
            return 202;
        }

    }

    public Object[] getResponseContent(){
        Object[] object = responseContent;
        responseContent = null;

        return object;
    }

    public void forceDisconnect(){
        if (socket != null && !socket.isConnected() && socket.isClosed())
            disconnect();
    }

    public Tokens getToken(){ return userToken; }
    public String getResponseMessage(int code){ return serverMessages.get(code); }

    public Users createUser(String email, String password){
        Users user = new Users();
        user.setEmailU(email);
        user.setPassU(password);

        return user;
    }

    private Devices createDevice() {
        try{
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
            byte[] hardwareAddress = ni.getHardwareAddress();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < hardwareAddress.length; i++) {
                sb.append(String.format(
                        "%02X%s", hardwareAddress[i],
                        (i < hardwareAddress.length - 1) ? "-" : ""));
            }

            Devices device = new Devices();
            device.setMac(sb.toString());
            device.setAgent(System.getProperty("os.name"));

            return device;
        }catch (SocketException | UnknownHostException e){
            return null;
        }
    }

    public Items createNote(String itemName, String itemObservation, String content){
        Items item = createItem(itemName, itemObservation);
        Notes note = new Notes();
        note.setContent(content);

        item.setNotesByIdI(note);
        note.setItemsByIdIn(item);

        return item;
    }

    public Items createPassword(String itemName, String itemObservation, String url, String email, String password){
        Items item = createItem(itemName, itemObservation);
        Passwords pass = new Passwords();
        pass.setUrl(url);
        pass.setEmailP(email);
        pass.setPassP(password);

        item.setPasswordsByIdI(pass);
        pass.setItemsByIdIp(item);

        return item;
    }

    private Items createItem(String itemName, String itemObservation){
        Items item = new Items();
        item.setName(itemName);
        item.setObservations(itemObservation);
        item.setModification(new Timestamp(System.currentTimeMillis()));
        item.setFav((byte) 0);
        item.setUsersByIdUi(userToken.getUsersByIdTu());
        item.setIdUi(userToken.getUsersByIdTu().getIdU());

        return item;
    }
}
