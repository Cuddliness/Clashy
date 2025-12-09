package care.cuddliness.clashy.command.data.info;


public class ClashyCommandInfo {

    private String name;
    private long id;

    public ClashyCommandInfo(String name, long id) {
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
