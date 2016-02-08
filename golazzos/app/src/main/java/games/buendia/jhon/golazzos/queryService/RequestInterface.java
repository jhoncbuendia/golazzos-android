package games.buendia.jhon.golazzos.queryService;

import org.json.JSONObject;

import games.buendia.jhon.golazzos.utils.ServicesCall;

/**
 * Created by jhon on 19/01/16.
 */
public interface RequestInterface {
    public void onSuccessCallBack(JSONObject response, ServicesCall serviceCall);
    public void onErrorCallBack(JSONObject response);
}
