import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main
{
   public static void main(String[] args)
   {
      String characterOne = args[0];
      String characterTwo = args[1];
      try
      {
         String characterString = FileUtils.readFileToString(new File("KharacterMoveFrameData.json"),
               StandardCharsets.UTF_8);
         JSONObject characters = new JSONObject(characterString);
         JSONObject firstCharacter = characters.getJSONObject(characterOne);
         JSONObject secondCharacter = characters.getJSONObject(characterTwo);
         JSONObject output = new JSONObject();
         JSONObject characterOnePunishes = new JSONObject();
         JSONObject characterTwoPunishes = new JSONObject();
         output.put(characterOne, characterOnePunishes);
         output.put(characterTwo, characterTwoPunishes);

         // generate second character's punishes on first character's unsafe moves
         for (String firstCharMove : firstCharacter.keySet())
         {
            JSONArray punishes = new JSONArray();
            for (String secondCharMove : secondCharacter.keySet())
            {
               int firstMoveBlock = firstCharacter.getJSONObject(firstCharMove).getInt("Block");
               int secondMoveStartup = secondCharacter.getJSONObject(secondCharMove).getInt("Startup");
               if (firstMoveBlock + secondMoveStartup < 0)
               {
                  punishes.put(secondCharMove);
               }
            }
            if (!punishes.isEmpty())
            {
               characterOnePunishes.put(firstCharMove, punishes);
            }
         }

         // generate first character's punishes on second character's unsafe moves
         for (String secondCharMove : secondCharacter.keySet())
         {
            JSONArray punishes = new JSONArray();
            for (String firstCharMove : firstCharacter.keySet())
            {
               int secondMoveBlock = secondCharacter.getJSONObject(secondCharMove).getInt("Block");
               int firstMoveStartup = firstCharacter.getJSONObject(firstCharMove).getInt("Startup");
               if (secondMoveBlock + firstMoveStartup < 0)
               {
                  punishes.put(firstCharMove);
               }
            }
            if (!punishes.isEmpty())
            {
               characterTwoPunishes.put(secondCharMove, punishes);
            }
         }

         PrintWriter outputFile = new PrintWriter("./punishes.json", "UTF-8");
         outputFile.println(output);
         outputFile.flush();
         outputFile.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
