package plugin.gitlab.handlers;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import plugin.gitlab.api.GitLabApi;

import org.eclipse.jface.dialogs.MessageDialog;

public class GroupPickHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		try {
			JSONArray jsonMembersArray = 
					new JSONArray(GitLabApi.GitLabGetRequest("/groups/" + event.getParameter("id") + "/members"));
			StringBuffer information = new StringBuffer("Name\t\tUsername\t\tstate\n");
			for(Object member_o : jsonMembersArray)
			{
				JSONObject member = (JSONObject) member_o;
				information.append(member.get("name") + "\t\t");
				information.append(member.get("username") + "\t\t");
				information.append(member.get("state") + "\n");
			}
			MessageDialog.openInformation(
					window.getShell(),
					"Group Users",
					information.toString());
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
