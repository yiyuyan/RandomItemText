package cn.ksmcbrigade.rit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

@Mod("rit")
public class RandomItemText {

    public static boolean init = false;
    public static final String ENGLISH_STRINGS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()?;:',<>./\\\"[]{}|-+=";

    public static boolean always = false;
    public static int interval = 3000; //about 20 seconds
    public static boolean chinese = true;

    public RandomItemText() throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        if(!init){
            init();
        }
    }

    public static void init() throws IOException {
        File file = new File("config/rit-config.json");
        if(!file.exists()){
            JsonObject object = new JsonObject();
            object.addProperty("always",always);
            object.addProperty("interval",interval);
            object.addProperty("chinese",chinese);
            Files.writeString(file.toPath(),object.toString());
        }
        JsonObject json = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonObject();
        chinese = json.get("chinese").getAsBoolean();
        always = json.get("always").getAsBoolean();
        interval = json.get("interval").getAsInt();
        init = true;
    }

    public static String randomString(int length,boolean chinese){
        if(!chinese){
            return english(length);
        }
        else{
            return chinese(length);
        }
    }

    public static String english(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ENGLISH_STRINGS.length());
            builder.append(ENGLISH_STRINGS.charAt(index));
        }
        return builder.toString();
    }

    public static String chinese(int length){
        StringBuilder zh_cn = new StringBuilder();
        String str;
        int start = Integer.parseInt("4e00", 16);
        int end = Integer.parseInt("9fa5", 16);
        for(int ic=0;ic<length;ic++){
            int code = (new Random()).nextInt(end - start + 1) + start;
            str = new String(new char[] { (char) code });
            zh_cn.append(str);
        }
        return zh_cn.toString();
    }
}
