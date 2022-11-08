import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        try {
            String output = new Main().solution(Files.readString(Path.of("src/input.txt")));
            System.out.println(output);
            FileWriter writer = new FileWriter("src/out.txt");
            writer.write(output);
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String solution(String S) {
        HashMap<String, TreeSet<Photo>> photos = new HashMap<>();
        AtomicInteger counter = new AtomicInteger();
        S.lines().forEach(description -> {
            Photo photo = new Photo(description, counter.getAndIncrement());
            TreeSet<Photo> photoList;
            if( (photoList = photos.get(photo.getLocation())) == null){
                photoList = new TreeSet<>(Comparator.comparing(Photo::getDate));
                photos.put(photo.getLocation(), photoList);
            }
            photoList.add(photo);
        });

        StringBuilder stringBuilder = new StringBuilder();
        Stream.of(photos.values())
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Photo::getOriginalOrder))
                .forEach(photo -> {
            photo.setCollection(photos.get(photo.getLocation()));
            stringBuilder.append(photo + "\n");
        });
        return stringBuilder.toString();
    }
}

class Photo {
    private List<Photo> collection;
    private Integer originalOrder;
    private String name;
    private String extension;
    private String location;
    private LocalDateTime date;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-M-d HH:mm:ss");

    public Photo(String description, Integer originalOrder){
        String[] info = description.split(",");
        String[] fileName = info[0].split("\\.");
        name = fileName[0].strip();
        extension = fileName[1].strip();
        location = info[1].strip();
        date = LocalDateTime.parse(info[2].strip(), dateTimeFormatter);
        this.originalOrder = originalOrder;
    }

    @Override
    public String toString() {
        int zeros = collection.size() / 10 + 1;
        int index = collection.indexOf(this);
        return String.format("%s%0" + zeros + "d.%s", location, index + 1, extension);
    }

    public Integer getOriginalOrder() {
        return originalOrder;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setCollection(TreeSet<Photo> collection) {
        this.collection = List.copyOf(collection);
    }
}
