package app.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TCFile extends File {

    private final SimpleStringProperty filename;
    private final SimpleStringProperty size;
    private final SimpleStringProperty date;
    private final SimpleStringProperty type;
    private static final String imageType = "./img/1187.png";
    private static final String textType = "./img/993.png";
    private static final String docType = "./img/895.png";
    private static final String diskType = "./img/1116.png";
    private static final String archiType = "./img/1531.png";
    private static final String musicType = "./img/1179.png";
    private static final String videoType = "./img/118.png";
    private static final String exeType = "./img/63.png";
    private static final String batType = "./img/93.png";
    private static final String cmdType = "./img/602.png";
    private static final String iniType = "./img/595.png";
    private static final String unknownType = "./img/77.png";
    private static final String parentType = "./img/1376.png";
    private static final String folderType =  "./img/101.png";
    private static final String picType =  "./img/628.png";
    private static final String folder = "<Папка>";
    private static final String parentValue = "parent";
    private static final String folderValue = "folder";
    private static final String unknownValue = "unknown";
    private ImageView image;
    private boolean isParent = false;

    private static final String dateFormat_dd_MM_yyyy_HH_mm = "dd.MM.yyyy HH:mm";
    private static final HashMap<String, String> typeImages = new HashMap<>();

    static {
        typeImages.put("folder", folderType);
        typeImages.put("txt", textType);
        typeImages.put("pdf", textType);
        typeImages.put("html", textType);
        typeImages.put("htm", textType);
        typeImages.put("mht", textType);
        typeImages.put("fb2", textType);
        typeImages.put("epub", textType);
        typeImages.put("mobi", textType);
        typeImages.put("doc", docType);
        typeImages.put("docx", docType);
        typeImages.put("xls", docType);
        typeImages.put("xlsx", docType);
        typeImages.put("ppt", docType);
        typeImages.put("pptx", docType);
        typeImages.put("djvu", unknownType);
        typeImages.put("iso", diskType);
        typeImages.put("zip", archiType);
        typeImages.put("rar", archiType);
        typeImages.put("7z", archiType);
        typeImages.put("gzip", archiType);
        typeImages.put("jpg", imageType);
        typeImages.put("bmp", imageType);
        typeImages.put("jpeg", imageType);
        typeImages.put("png", imageType);
        typeImages.put("gif", imageType);
        typeImages.put("tif", picType);
        typeImages.put("mp3", musicType);
        typeImages.put("wav", musicType);
        typeImages.put("midi", musicType);
        typeImages.put("aac", musicType);
        typeImages.put("mp4", videoType);
        typeImages.put("avi", videoType);
        typeImages.put("mkv", videoType);
        typeImages.put("wmv", videoType);
        typeImages.put("flv", videoType);
        typeImages.put("mpeg", videoType);
        typeImages.put("exe", exeType);
        typeImages.put("bat", batType);
        typeImages.put("cmd", cmdType);
        typeImages.put("ini", iniType);
        typeImages.put("unknown", unknownType);
        typeImages.put("parent", parentType);
    }

    private String getCreationTime(String path) throws IOException {
        Path file = Paths.get(path);
        BasicFileAttributes attr =
                Files.readAttributes(file, BasicFileAttributes.class);
        return formatDateTime(attr.creationTime());
    }

    public static String formatDateTime(FileTime fileTime) {
        LocalDateTime localDateTime = fileTime
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return localDateTime.format(
                DateTimeFormatter.ofPattern(dateFormat_dd_MM_yyyy_HH_mm));
    }

    public void setParentImage() {
        image.setImage(new Image(typeImages.get(parentValue)));
    }

    public TCFile(String pathname) throws IOException {
        this(pathname, false);
    }

    public TCFile(String parent, String child) throws IOException {
        this(parent, child, false);
    }

    public TCFile(String pathname, boolean isNew) throws IOException {
        super(pathname);
        this.filename = new SimpleStringProperty("[" + this.getName() + "]");
        this.size = new SimpleStringProperty(folder);
        this.date = isNew?new SimpleStringProperty(new SimpleDateFormat(dateFormat_dd_MM_yyyy_HH_mm).format(new Date()))
                :new SimpleStringProperty(getCreationTime(pathname));
        this.type = new SimpleStringProperty("");
        this.image = new ImageView(new Image(typeImages.get(folderValue)));
    }

    public TCFile(String parent, String child, boolean isNew) throws IOException {
        super(parent, child);
        this.size = new SimpleStringProperty(String.valueOf(this.length()));
        this.date = isNew?new SimpleStringProperty(new SimpleDateFormat(dateFormat_dd_MM_yyyy_HH_mm).format(new Date()))
                :new SimpleStringProperty(getCreationTime(parent + "\\" + child));
        this.type = new SimpleStringProperty(getFileExtension());
        this.filename = new SimpleStringProperty(this.getName().replace("." + getType(), ""));
        try {
            this.image = new ImageView(new Image(typeImages.get(getType())));
        } catch (NullPointerException exception) {
            this.image = new ImageView(new Image(typeImages.get(unknownValue)));
        }
    }

    public String getFilename() {
        return filename.get();
    }

    public void setFilename(String name) {
        this.filename.set(name);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getSize() {
        return size.get();
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public Long getDirLength() {
        File[] files = this.listFiles();
        Long size = 0L;
        if (files != null) {
            for (File file : files) {
                size += file.length();
            }
        }
        return size;
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty nameProperty() {
        return filename;
    }

    public SimpleStringProperty sizeProperty() {
        return size;
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public List<TCFile> getFileList(File[] sourceFiles) throws IOException {
        List<TCFile> files = new ArrayList<>();
        if (sourceFiles != null) {
            for (File sourceFile : sourceFiles) {
                Path path = Paths.get(sourceFile.getAbsolutePath());
                if (Files.isDirectory(path)) {
                    files.add(new TCFile(sourceFile.getAbsolutePath()));
                } else {
                    files.add(new TCFile(sourceFile.getParent(), sourceFile.getName()));
                }
            }
            return files;
        }
        return null;
    }

    public File[] getDirectories() {
        return this.listFiles(File::isDirectory);
    }

    public File[] getFiles() {
        return this.listFiles(File::isFile);
    }

    public static AtomicLong getDirSize(TCFile file) {
        Path path = Paths.get(file.getPath());
        AtomicLong size = new AtomicLong(0);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    size.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException e) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ignored) {
        }
        return size;
    }

    public static AtomicInteger getDirCount(TCFile file) {
        Path path = Paths.get(file.getPath());
        AtomicInteger count = new AtomicInteger(0);
        try {
            Files.walkFileTree(path, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    count.addAndGet(1);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException e) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ignored) {
        }
        count.getAndDecrement();
        return count;
    }

    public static AtomicInteger getFileCount(TCFile file) {
        Path path = Paths.get(file.getPath());
        AtomicInteger count = new AtomicInteger(0);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    count.addAndGet(1);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ignored) {
        }
        return count;
    }

    public boolean isImage() {
        switch(type.get()){
            case "jpeg":
            case "jpg":
            case "bmp":
            case "png":
            case "gif":
            case "tif":
                return true;
            default:
                return false;
        }
    }

    public String getFileExtension() {
        String fileName = this.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

}
