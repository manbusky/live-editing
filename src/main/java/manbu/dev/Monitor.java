package manbu.dev;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;

/**
 * Created by Wang Jiacheng.
 * Date: 8/1/17
 * Time: 15:55
 */
public class Monitor extends FileAlterationListenerAdaptor {

    private MonitorListener listener;

    public Monitor(MonitorListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDirectoryCreate(File directory) {
        listener.changed(directory, MonitorListener.Action.CREATE);
    }

    @Override
    public void onDirectoryChange(File directory) {
        listener.changed(directory, MonitorListener.Action.CHANGE);
    }

    @Override
    public void onDirectoryDelete(File directory) {
        listener.changed(directory, MonitorListener.Action.DELETE);
    }

    @Override
    public void onFileCreate(File file) {
        listener.changed(file, MonitorListener.Action.CREATE);
    }

    @Override
    public void onFileChange(File file) {
        listener.changed(file, MonitorListener.Action.CHANGE);
    }

    @Override
    public void onFileDelete(File file) {
        listener.changed(file, MonitorListener.Action.DELETE);
    }

}
