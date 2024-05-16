package debugging;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class DummyRobot implements Serializable {
    private String name;
    private int age;

    public DummyRobot(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public static void main(String[] args) throws IOException {
        DummyRobot dummy = new DummyRobot(4, "Alexa");

        try {
            FileWriter myFile = new FileWriter("src/main/java/debugging/output.json");
            Gson gson = new Gson();
            String json = gson.toJson(dummy);
            myFile.write(json);
            myFile.close();

            System.out.println("Serialized data is " + json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}