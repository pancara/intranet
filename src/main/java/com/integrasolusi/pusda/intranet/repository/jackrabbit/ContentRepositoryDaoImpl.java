package com.integrasolusi.pusda.intranet.repository.jackrabbit;

import com.integrasolusi.jcr.JcrCallback;
import com.integrasolusi.jcr.JcrTemplate;
import com.integrasolusi.pusda.intranet.utils.StreamHelper;
import com.integrasolusi.pusda.intranet.web.utils.StreamBufferUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.jcr.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Programmer   : pancara
 * Date         : Jan 3, 2011
 * Time         : 5:10:20 PM
 */
public class ContentRepositoryDaoImpl implements ContentRepositoryDao {
    private final static String NODE_TYPE = "nt:resource";
    private Logger logger = Logger.getLogger(ContentRepositoryDaoImpl.class);
    private JcrTemplate jcrTemplate;
    private StreamBufferUtils streamBufferUtils;


    private void createPath(Session session, String path) throws RepositoryException {
        Node root = session.getRootNode();
        String[] paths = StringUtils.split(path, "/");
        Node parent = root;
        for (String p : paths) {
            Node node;
            if (!parent.hasNode(p)) {
                node = parent.addNode(p);
            } else {
                node = parent.getNode(p);
            }
            parent = node;
        }

    }

    @Override
    public void saveBinaryContent(final String path, final InputStream is) throws IOException {
        logger.info(">>>>>>> " + path);
        jcrTemplate.execute(new JcrCallback() {
            @Override
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                boolean nodeExist = session.nodeExists(path);

                if (!nodeExist)
                    createPath(session, path);

                Node node = session.getNode(path);
//                node.setPrimaryType("nt:resource");

                Binary binary = session.getValueFactory().createBinary(is);
                node.setProperty(BINARY_CONTENT_PROPERTY, binary);

                session.save();
                binary.dispose();

                return node.getPath();
            }
        });
    }

    @Override
    public void removeBinaryContent(final String path) {
        jcrTemplate.execute(new JcrCallback() {
            @Override
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                if (session.nodeExists(path)) {
                    Node node = session.getNode(path);
                    node.remove();
                    session.save();
                }
                return null;
            }
        });

    }

    @Override
    public void copyBinaryContent(final String path, final OutputStream target) {
        jcrTemplate.execute(new JcrCallback() {
            @Override
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                if (session.nodeExists(path)) {
                    Node node = session.getNode(path);
                    Binary binary = node.getProperty(BINARY_CONTENT_PROPERTY).getBinary();
                    StreamHelper.getInstance().copy(binary.getStream(), target);
                    binary.dispose();
                }
                return null;
            }
        });
    }

    @Override
    public void copyBinaryContent(final String path, final OutputStream target, final boolean useTemporaryFile) {
        jcrTemplate.execute(new JcrCallback() {
            @Override
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                if (session.nodeExists(path)) {

                    if (useTemporaryFile) {
                        File temporaryFile = streamBufferUtils.createTemporaryFile();

                        // save repository bytes to temporary file
                        OutputStream osTemp = new FileOutputStream(temporaryFile);
                        Node node = session.getNode(path);
                        Binary binary = node.getProperty(BINARY_CONTENT_PROPERTY).getBinary();
                        StreamHelper.getInstance().copy(binary.getStream(), osTemp);
                        binary.dispose();
                        osTemp.close();

                        InputStream isTemp = new FileInputStream(temporaryFile);
                        StreamHelper.getInstance().copy(isTemp, target);
                        isTemp.close();
                    } else {
                        copyBinaryContent(path, target);
                    }
                }
                return null;
            }
        });
    }

    @Override
    public Boolean isPathExist(final String path) {
        return (Boolean) jcrTemplate.execute(new JcrCallback() {
            @Override
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                return session.nodeExists(path);
            }
        });
    }

    @Override
    public BufferedImage getBufferedImage(final String path) throws IOException {
        return (BufferedImage) jcrTemplate.execute(new JcrCallback() {
            @Override
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                if (session.nodeExists(path)) {
                    Node node = session.getNode(path);
                    Binary binary = node.getProperty(BINARY_CONTENT_PROPERTY).getBinary();
                    BufferedImage image = ImageIO.read(binary.getStream());
                    binary.dispose();
                    return image;
                }
                return null;
            }
        });

    }

    public void setJcrTemplate(JcrTemplate jcrTemplate) {
        this.jcrTemplate = jcrTemplate;
    }


    public void setStreamBufferUtils(StreamBufferUtils streamBufferUtils) {
        this.streamBufferUtils = streamBufferUtils;
    }
}
