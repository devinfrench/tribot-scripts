package scripts.agility.data;

import scripts.agility.courses.Course;
import scripts.api.script.TaskScript;
import scripts.api.util.Statistics;

public class Vars {

    private static final Vars instance = new Vars();
    public TaskScript script;
    public Statistics stats;
    public Course course;

    private Vars() {

    }

    public static Vars get() {
        return instance;
    }

}
