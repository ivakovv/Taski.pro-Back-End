package com.project.taskipro.service.git;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Getter
@Slf4j
public class TempDirectory implements AutoCloseable {

    private final Path path;
    private static final int DELETION_DELAY_SECONDS = 2;

    public TempDirectory(String prefix) throws IOException {
        this.path = Files.createTempDirectory(prefix);
        log.debug("Создана временная директория: {}", path);
    }

    public File toFile() {
        return path.toFile();
    }

    @Override
    public void close() {
        try {
            TimeUnit.SECONDS.sleep(DELETION_DELAY_SECONDS);
            System.gc();
            FileUtils.deleteDirectory(path.toFile());
            log.debug("Временная директория успешно удалена: {}", path);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Прервано ожидание перед удалением директории", e);
        } catch (IOException e) {
            log.warn("Не удалось удалить временную директорию: {}", path, e);
        }
    }

}
