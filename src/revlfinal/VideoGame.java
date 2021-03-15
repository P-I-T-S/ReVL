package revlfinal;

/**
 * Created by Coleten McGuire on 5/14/2016.
 */
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

//video game entity
public class VideoGame extends GenericJson {
    @Key("_id")
    private String id;
    @Key
    private String Name;
    @Key
    private String System;
    @Key
    private String PreviousAmazon;
    @Key
    private String Amazon;
    @Key
    private String PreviousGamestop;
    @Key
    private String Gamestop;
    @Key
    private String Searching;
    @Key
    private String PreviousAmazonDate;
    @Key
    private String PreviousGamestopDate;

    //GenericJson classes must have a public empty constructor
    public VideoGame() {
    }

    public VideoGame(String id, String Name, String System, String PreviousAmazon, String Amazon, String PreviousGamestop, String Gamestop, String Searching, String PreviousAmazonDate, String PreviousGamestopDate) {
        this.id = id;
        this.Name = Name;
        this.System = System;
        this.PreviousAmazon = PreviousAmazon;
        this.Amazon = Amazon;
        this.PreviousGamestop = PreviousGamestop;
        this.Gamestop = Gamestop;
        this.Searching = Searching;
        this.PreviousAmazonDate = PreviousAmazonDate;
        this.PreviousGamestopDate = PreviousGamestopDate;
    }

    //getter and setter methods
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getSystem() {
        return System;
    }
    public void setSystem(String System) {
        this.System = System;
    }
    public String getPreviousAmazon() { return PreviousAmazon; }
    public void setPreviousAmazon(String PreviousAmazon) {this.PreviousAmazon = PreviousAmazon; }
    public String getAmazon() {
        return Amazon;
    }
    public void setAmazon(String Amazon) {
        this.Amazon = Amazon;
    }
    public String getPreviousGamestop() { return PreviousGamestop; }
    public void setPreviousGamestop(String PreviousGamestop) {this.PreviousGamestop = PreviousGamestop; }
    public String getGamestop() {
        return Gamestop;
    }
    public void setGamestop(String Gamestop) {
        this.Gamestop = Gamestop;
    }
    public String getSearching() {
        return Searching;
    }
    public void setSearching(String Searching) {
        this.Searching = Searching;
    }
    public String getPreviousAmazonDate() { return PreviousAmazonDate; }
    public void setPreviousAmazonDate(String PreviousAmazonDate) { this.PreviousAmazonDate = PreviousAmazonDate; }
    public String getPreviousGamestopDate() { return PreviousGamestopDate; }
    public void setPreviousGamestopDate(String PreviousGamestopDate) { this.PreviousGamestopDate = PreviousGamestopDate; }
}