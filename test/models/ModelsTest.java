package models;

import java.util.List;
import java.util.Map;

import models.*;
import org.junit.*;

import com.avaje.ebean.Ebean;

import static org.junit.Assert.*;
import play.libs.Yaml;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class ModelsTest extends WithApplication {
    
	@Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
        
        Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");

        // Insert users first
        Ebean.save(all.get("users"));

        // Insert projects
        Ebean.save(all.get("projects"));
        for(Object project: all.get("projects")) {
            // Insert the project/user relation
            Ebean.saveManyToManyAssociations(project, "members");
        }

        // Insert tasks
        Ebean.save(all.get("tasks"));
    }

    @Test
    public void createAndRetrieveUser() {
        new User("bob@gmail.com", "Bob", "secret").save();
        User bob = User.find.where().eq("email", "bob@gmail.com").findUnique();
        assertNotNull(bob);
        assertEquals("Bob", bob.name);
    }
}
