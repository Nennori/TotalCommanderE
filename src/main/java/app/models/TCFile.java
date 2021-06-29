package app.models;

import app.constants.Extension;
import app.constants.ExtensionImage;
import app.constants.FileConstant;
import app.controllers.FileController;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.log4j.Logger;

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

    private final transient SimpleStringProperty filename;
    private final transient SimpleStringProperty size;
    private final transient SimpleStringProperty date;
    private final transient SimpleStringProperty type;

    private transient ImageView image;
    private boolean isParent = false;

    private static final String DATE_FORMAT_DD_MM_YYYY_HH_MM = "dd.MM.yyyy HH:mm";
    private static final HashMap<String, String> typeImages = new HashMap<>();
    private static final Logger log = Logger.getLogger(TCFile.class);

    static {
        typeImages.put(Extension.FOLDER, ExtensionImage.FOLDER_TYPE);
        typeImages.put(Extension.TXT, ExtensionImage.TEXT_TYPE);
        typeImages.put(Extension.PDF, ExtensionImage.TEXT_TYPE);
        typeImages.put(Extension.HTML, ExtensionImage.TEXT_TYPE);
        typeImages.put(Extension.HTM, ExtensionImage.TEXT_TYPE);
        typeImages.put(Extension.FB2, ExtensionImage.TEXT_TYPE);
        typeImages.put(Extension.EPUB, ExtensionImage.TEXT_TYPE);
        typeImages.put(Extension.MOBI, ExtensionImage.TEXT_TYPE);
        typeImages.put(Extension.DOC, ExtensionImage.DOC_TYPE);
        typeImages.put(Extension.DOCX, ExtensionImage.DOC_TYPE);
        typeImages.put(Extension.XLS, ExtensionImage.DOC_TYPE);
        typeImages.put(Extension.XLSX, ExtensionImage.DOC_TYPE);
        typeImages.put(Extension.PPT, ExtensionImage.DOC_TYPE);
        typeImages.put(Extension.PPTX, ExtensionImage.DOC_TYPE);
        typeImages.put(Extension.DJVU, ExtensionImage.UNKNOWN_TYPE);
        typeImages.put(Extension.ISO, ExtensionImage.DISK_TYPE);
        typeImages.put(Extension.ZIP, ExtensionImage.ARCHIVE_TYPE);
        typeImages.put(Extension.RAR, ExtensionImage.ARCHIVE_TYPE);
        typeImages.put(Extension.SEVENZ, ExtensionImage.ARCHIVE_TYPE);
        typeImages.put(Extension.GZIP, ExtensionImage.ARCHIVE_TYPE);
        typeImages.put(Extension.JPG, ExtensionImage.IMAGE_TYPE);
        typeImages.put(Extension.BMP, ExtensionImage.IMAGE_TYPE);
        typeImages.put(Extension.JPEG, ExtensionImage.IMAGE_TYPE);
        typeImages.put(Extension.PNG, ExtensionImage.IMAGE_TYPE);
        typeImages.put(Extension.GIF, ExtensionImage.IMAGE_TYPE);
        typeImages.put(Extension.TIF, ExtensionImage.PIC_TYPE);
        typeImages.put(Extension.MP3, ExtensionImage.MUSIC_TYPE);
        typeImages.put(Extension.WAV, ExtensionImage.MUSIC_TYPE);
        typeImages.put(Extension.MIDI, ExtensionImage.MUSIC_TYPE);
        typeImages.put(Extension.AAC, ExtensionImage.MUSIC_TYPE);
        typeImages.put(Extension.MP4, ExtensionImage.VIDEO_TYPE);
        typeImages.put(Extension.AVI, ExtensionImage.VIDEO_TYPE);
        typeImages.put(Extension.MKV, ExtensionImage.VIDEO_TYPE);
        typeImages.put(Extension.WMV, ExtensionImage.VIDEO_TYPE);
        typeImages.put(Extension.FLV, ExtensionImage.VIDEO_TYPE);
        typeImages.put(Extension.MPEG, ExtensionImage.VIDEO_TYPE);
        typeImages.put(Extension.EXE, ExtensionImage.EXE_TYPE);
        typeImages.put(Extension.BAT, ExtensionImage.BAT_TYPE);
        typeImages.put(Extension.CMD, ExtensionImage.CMD_TYPE);
        typeImages.put(Extension.INI, ExtensionImage.INI_TYPE);
        typeImages.put(Extension.UNKNOWN, ExtensionImage.UNKNOWN_TYPE);
        typeImages.put(Extension.PARENT, ExtensionImage.PARENT_TYPE);
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
                DateTimeFormatter.ofPattern(DATE_FORMAT_DD_MM_YYYY_HH_MM));
    }

    public void setParentImage() {
        image.setImage(new Image(typeImages.get(ExtensionImage.PARENT_VALUE)));
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
        this.size = new SimpleStringProperty(ExtensionImage.FOLDER);
        this.date = isNew?new SimpleStringProperty(new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY_HH_MM).format(new Date()))
                :new SimpleStringProperty(getCreationTime(pathname));
        this.type = new SimpleStringProperty("");
        this.image = new ImageView(new Image(typeImages.get(Extension.FOLDER)));
    }

    public TCFile(String parent, String child, boolean isNew) throws IOException {
        super(parent, child);
        this.size = new SimpleStringProperty(String.valueOf(this.length()));
        this.date = isNew?new SimpleStringProperty(new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY_HH_MM).format(new Date()))
                :new SimpleStringProperty(getCreationTime(parent + "\\" + child));
        this.type = new SimpleStringProperty(getFileExtension());
        this.filename = new SimpleStringProperty(this.getName().replace("." + getType(), ""));
        try {
            this.image = new ImageView(new Image(typeImages.get(getType())));
        } catch (NullPointerException exception) {
            this.image = new ImageView(new Image(typeImages.get(ExtensionImage.UNKNOWN_VALUE)));
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

    public boolean isNotParent() {
        return !isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public Long getDirLength() {
        File[] files = this.listFiles();
        long dirSize = 0L;
        if (files != null) {
            for (File file : files) {
                dirSize += file.length();
            }
        }
        return dirSize;
    }

    public String getDate() {
        return date.get();
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
        }
        return files;
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
            log.error(FileConstant.ERR_DIR_SIZE);
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
            log.error(FileConstant.ERR_DIR);
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
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    count.addAndGet(1);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException e) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ignored) {
            log.error(FileConstant.ERR_FILE_COUNT);
        }
        return count;
    }

    public boolean isImage() {
        switch(type.get()){
            case Extension.JPEG:
            case Extension.JPG:
            case Extension.BMP:
            case Extension.PNG:
            case Extension.GIF:
            case Extension.TIF:
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
