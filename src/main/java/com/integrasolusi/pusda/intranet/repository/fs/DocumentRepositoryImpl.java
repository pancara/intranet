package com.integrasolusi.pusda.intranet.repository.fs;

import com.integrasolusi.pusda.intranet.utils.StreamHelper;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Programmer   : pancara
 * Date         : 6/22/11
 * Time         : 9:51 AM
 */
public class DocumentRepositoryImpl implements DocumentRepository {
    private String dataDir;
    private String bufferDir;
    private Long bufferIndex = 1L;
    private String extension = "dat";
    private File root;
    private Logger logger = Logger.getLogger(DocumentRepositoryImpl.class);
    private StreamHelper streamHelper;

    public static String getPath(String folder, Long id) {
        return String.format("%s/%d", folder, id);
    }

    private boolean isDocumentDirExist(DocumentType type) {
        File file = new File(root, type.toString());
        return (file.exists() && file.isDirectory());
    }

    private void createDocumentDir(DocumentType type) {
        File file = new File(root, type.toString());
        file.mkdir();
    }

    private File getDocumentFile(DocumentType type, Long id) {
        return new File(root, String.format("%s/%d.%s", type.toString(), id, extension));
    }

    @Override
    public void storeDocument(DocumentType type, Long id, InputStream is) throws IOException {
        File bufferFile = storeToBuffer(is);

        if (!isDocumentDirExist(type))
            createDocumentDir(type);

        File file = getDocumentFile(type, id);
        if (file.exists()) {
            file.delete();
        }
        synchronized (this) {
            streamHelper.copyFile(bufferFile, file);
        }
        bufferFile.delete();
    }

    @Override
    public void readDocument(DocumentType type, Long id, OutputStream os) throws IOException {
        File file = getDocumentFile(type, id);
        File bufferFile = createBufferFile();
        synchronized (this) {
            streamHelper.copyFile(file, bufferFile);
        }

        FileInputStream is = new FileInputStream(bufferFile);
        try {
            streamHelper.copy(is, os);
        } finally {
            is.close();
            bufferFile.delete();
        }
    }

    @Override
    public void removeDocument(DocumentType type, Long id) {
        File file = getDocumentFile(type, id);
        if (file.exists()) {
            synchronized (this) {
                file.delete();
            }
        }
    }

    @Override
    public BufferedImage createImage(DocumentType type, Long id) throws IOException {
        File bufferFile = createBufferFile();
        File file = getDocumentFile(type, id);
        synchronized (this) {
            streamHelper.copyFile(file, bufferFile);
        }

        BufferedImage image = null;
        if (file.exists()) {
            InputStream is = new FileInputStream(bufferFile);
            try {
                image = ImageIO.read(is);
            } finally {
                is.close();
                bufferFile.delete();
            }
        }
        return image;
    }

    private synchronized File createBufferFile() {
        File bufferFile = null;
        do {
            bufferFile = new File(bufferDir, String.format("%d.tmp", bufferIndex));
            bufferIndex++;
        } while (bufferFile.exists());
        return bufferFile;
    }

    private File storeToBuffer(InputStream is) throws IOException {
        File bufferFile = createBufferFile();
        OutputStream os = new FileOutputStream(bufferFile);
        try {
            streamHelper.copy(is, os);
        } finally {
            os.close();
        }
        return bufferFile;
    }

    @Override
    public Boolean documentExist(DocumentType type, Long id) {
        return getDocumentFile(type, id).exists();
    }

    public void setStreamHelper(StreamHelper streamHelper) {
        this.streamHelper = streamHelper;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void init() {
        root = new File(dataDir);
    }

    public String getBufferDir() {
        return bufferDir;
    }

    public void setBufferDir(String bufferDir) {
        this.bufferDir = bufferDir;
    }

}

