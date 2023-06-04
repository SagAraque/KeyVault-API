package com.keyvault;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.keyvault.database.models.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
public class KeyVault {

    private SecureSocket secureSocket;
    private Object responseContent;
    private SessionToken userToken;
    private boolean localhost = false;

    public KeyVault(){

    }

    public KeyVault(boolean localhost){
        this.localhost = localhost;
    }

    private void connect() throws IOException {
        secureSocket = new SecureSocket(localhost ? "localhost" : "129.151.227.217", 5556);
    }

    private void disconnect(){
        try {
            secureSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int login(Users user)
    {
        try {
            connect();
            Response response = sendRequest(new Request(user, createDevice(), Request.LOGIN));

            if(response.getResponseCode() == 200){
                userToken = (SessionToken) response.getResponseContent();
                disconnect();
            }

            return response.getResponseCode();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 202;
        }
        finally
        {
            disconnect();
        }
    }

    public int verifyLogin(String code, boolean saveDevice, Users user){
        try
        {
            connect();

            Response response = sendRequest(new Request(user, createDevice(), Request.VERIFY));

            if(response.getResponseCode() == 102){
                secureSocket.writeUTF(code + "::" + (saveDevice ? 1 : 0));

                response = (Response) secureSocket.readObject();

                if(response != null)
                {
                    if( response.getResponseCode() == 200)
                        userToken = (SessionToken) response.getResponseContent();
                }
                else
                {
                    response = new Response(202);
                }
            }

            return response.getResponseCode();

        }
        catch (Exception e)
        {
            return 202;
        }
        finally
        {
            disconnect();
        }
    }

    public int register(Users user){
        return serverOperation(new Request(user, createDevice(), Request.REGISTER));
    }

    public int getItems(){
        return serverOperation(new Request(Request.GET, userToken));
    }
    public int getDevices() { return serverOperation(new Request(Request.GET_DEVICES, userToken)); }

    public int modItem(Items item){
        return serverOperation(new Request(item, Request.MOD, userToken));
    }

    public int insertItem(Items item){
        return serverOperation(new Request(item,Request.INSERT, userToken));
    }

    public int deleteItem(Items item){
        return serverOperation(new Request(item,Request.DELETE, userToken));
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

    public int verifyTOTP(String code){ return serverOperation(new Request(code, Request.VERIFY_TOTP, userToken));}

    private int serverOperation(Request request){
        try{
            connect();

            Response response = sendRequest(request);

            return response.getResponseCode();

        }catch (Exception e){
            e.printStackTrace();
            return 202;
        }
        finally
        {
            disconnect();
        }
    }

    public int sendImage(File image, String path, String name)
    {
        try
        {
            connect();

            BufferedImage bf = ImageIO.read(image);
            Image result = bf.getScaledInstance(60, 60, Image.SCALE_DEFAULT);
            BufferedImage resized = new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB);
            resized.getGraphics().drawImage(result, 0, 0, null);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(resized, "png", byteArrayOutputStream);

            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            Response response = sendRequest(new Request(imageBytes, Request.PROFILE_IMAGE, userToken));

            if(response.getResponseCode() == 200)
            {
                File output = new File(path + name + ".png");
                ImageIO.write(resized, "png", output);
            }

            return response.getResponseCode();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 202;
        }
        finally
        {
            disconnect();
        }
    }

    public int getImage()
    {
        try
        {
            connect();
            Response response = sendRequest(new Request(Request.GET_PROFILE_IMAGE, userToken));

            return response.getResponseCode();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 202;
        }
        finally
        {
            disconnect();
        }
    }

    private Response sendRequest(Request request) throws Exception {
        secureSocket.writeObject(request);
        Response response = (Response) secureSocket.readObject();

        if(response != null)
        {
            responseContent = response.getResponseContent();
        }
        else
        {
            response = new Response(202);
        }

        return response;
    }

    public BufferedImage generateQR(String qr){
        try{
            BitMatrix matrix = new MultiFormatWriter().encode(qr, BarcodeFormat.QR_CODE, 300, 300);

            return MatrixToImageWriter.toBufferedImage(matrix);
        }catch (WriterException e){
            e.printStackTrace();
            return null;
        }

    }

    public Object getResponseContent(){
        Object object = responseContent;
        responseContent = null;

        return object;
    }

    public SessionToken getToken(){ return userToken; }

    public Users getAuthUser()
    {
        return userToken != null ? userToken.getUser() : null;
    }

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
        Color color = Color.getHSBColor((float) (Math.random() * 360), 0.7F, 0.6F);
        item.setName(itemName);
        item.setObservations(itemObservation);
        item.setModification(new Timestamp(System.currentTimeMillis()));
        item.setFav(false);
        item.setUsersByIdUi(userToken.getUser());
        item.setIdUi(userToken.getUser().getIdU());
        item.setColor("#" + Integer.toHexString(color.getRGB()).substring(2));

        return item;
    }
}
