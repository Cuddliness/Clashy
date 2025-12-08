package care.cuddliness.stacy.command.data.info;


public class StacyCommandInfo {

    private String name;
    private long id;

    public StacyCommandInfo(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
