package com.integrasolusi.demo;

import org.apache.jackrabbit.core.TransientRepository;

import javax.jcr.*;
import javax.jcr.version.Version;
import javax.jcr.version.VersionManager;

/**
 * Created by IntelliJ IDEA.
 * Account: koko
 * Date: Jan 28, 2011
 * Time: 8:17:06 PM
 */
public class TestRestoreDeleted {
    public static void main(String... args) throws Exception {
        TransientRepository rep = new TransientRepository();
        Session s = rep.login(new SimpleCredentials("", new char[0]));
        try {
            // clear the repository first
            if (s.getRootNode().hasNode("test")) {
                s.getRootNode().getNode("test").remove();
                s.save();
            }
            // add test/t1 and check in the change
            Node test = s.getRootNode().addNode("test");
            Node t1 = test.addNode("t1");
            t1.addMixin("mix:versionable");
            s.save();
            VersionManager vm = s.getWorkspace().
                    getVersionManager();
            for (int i = 0; i < 3; i++) {
                vm.checkout("/test/t1");
                t1.setProperty("data", "Hello" + i);
                s.save();
                vm.checkin("/test/t1");
            }
            // remove the node
            t1.remove();
            s.save();
            // list all versions of all nodes in the repository
            Node vs = s.getRootNode().
                    getNode("jcr:system").
                    getNode("jcr:versionStorage");
            Version v = traverseVersionStorage(vs, 0);
            // restore a version
            vm.restore("/test/t1", v, false);
            // get the node and print the data
            t1 = s.getRootNode().
                    getNode("test").getNode("t1");
            System.out.println("Restored: " +
                    t1.getProperty("data").getString());
        } finally {
            s.logout();
        }
    }

    private static Version traverseVersionStorage(
            Node n, int level) throws Exception {
        Version v = null;
        for (NodeIterator it = n.getNodes(); it.hasNext();) {
            Node n2 = it.nextNode();
            if (n2 instanceof Version
                    && !n2.getName().startsWith("jcr:")) {
                v = (Version) n2;
                System.out.println("version " + n2.getName() +
                        " of node " + n2.getParent().getName() + ":");
                Node n3 = n2.getNode("jcr:frozenNode");
                for (PropertyIterator pt =
                        n3.getProperties(); pt.hasNext();) {
                    Property p = pt.nextProperty();
                    if (!p.getName().startsWith("jcr:")) {
                        System.out.println("  " + p.getName() + "="
                                + (p.isMultiple() ? p.getValues().toString()
                                : p.getValue().getString()));
                    }
                }
                System.out.println();
            }
            Version v2 = traverseVersionStorage(n2, level + 1);
            v = v == null ? v2 : v;
        }
        return v;
    }

}
