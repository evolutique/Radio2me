package radio2me.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("player")
public interface PlayerService extends RemoteService {
	public Boolean playUrl(String pUrl) throws Exception;
	
	public Boolean stop();
}
