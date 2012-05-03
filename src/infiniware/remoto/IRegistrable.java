package infiniware.remoto;

import java.rmi.Remote;

public interface IRegistrable {

    public String getHost();

    public int getPort();

    public String getRemoteName();
}
