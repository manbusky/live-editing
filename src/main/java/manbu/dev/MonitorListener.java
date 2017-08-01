package manbu.dev;

import java.io.File;

/**
 * Created by Wang Jiacheng.
 * Date: 8/1/17
 * Time: 16:14
 */
public interface MonitorListener {

    /**
     * changed
     * @param file file
     * @param action action
     */
    void changed(File file, Action action);

    enum Action {
        CREATE,
        CHANGE,
        DELETE
    }

}
