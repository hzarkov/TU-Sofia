package plugin.gitlab.views;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.HandlerEvent;
import org.eclipse.core.commands.IHandler2;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.ViewPart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import plugin.gitlab.api.GitLabApi;

public class Milestones extends ViewPart implements IHandler2 {
	private Table table;
	private Group createGroup;
	Text name;
	Button createButton;
	String currentProjectId;
	public Milestones()
	{
		super();
	}
	
	@Override
	public void createPartControl(Composite parent) {

		table = new Table (parent, SWT.MULTI | SWT.BORDER
		        | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		String[] strings = {"Title","state"};
		for(int i = 0; i < strings.length; i++)
		{
			TableColumn header = new TableColumn(table, SWT.CENTER,i);
			header.setText(strings[i]);
			header.setWidth(150);
		}
		
		createGroup = new Group(parent,SWT.HORIZONTAL);
		createGroup.setText("Create Milestone");
		name = new Text(createGroup,SWT.NONE);
		name.setEditable(true);
		name.setMessage("Milestone title");
		name.setTextLimit(20);
		name.setSize(200, 32);
		name.setLocation(10, 10);
		name.pack();
		createButton = new Button(createGroup,SWT.NONE);
		createButton.setLocation(100, 35);
		createButton.setText("Create");
		createButton.setEnabled(false);
		createButton.setSize(10,50);
		createButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createButton.setEnabled(false);
				
				if(name.getText().isEmpty())
				{
					MessageDialog.openInformation(
							getSite().getShell(),
							"Group Users",
							"Title cannot be empty empty!");
				}
				try {
					GitLabApi.GitLabPostRequest("/projects/" + currentProjectId  + "/milestones?title=" + name.getText());
					MessageDialog.openInformation(
							getSite().getShell(),
							"Group Users",
							"Milestone was created");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				createButton.setEnabled(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		createButton.pack();
		createGroup.pack();
		((ICommandService)getSite().getService(ICommandService.class)).getCommand("plugin.gitlab.commands.projectPickCommand").setHandler(this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	private boolean baseEnabled = true;

	@Override
	public void addHandlerListener(final IHandlerListener handlerListener) {
		addListenerObject(handlerListener);
	}

	protected void fireHandlerChanged(final HandlerEvent handlerEvent) {
		if (handlerEvent == null) {
			throw new NullPointerException();
		}

		for (Object listener : getListeners()) {
			final IHandlerListener handlerListener = (IHandlerListener) listener;
			handlerListener.handlerChanged(handlerEvent);
		}
	}

	
	@Override
	public boolean isEnabled() {
		return baseEnabled;
	}

	protected void setBaseEnabled(boolean state) {
		if (baseEnabled == state) {
			return;
		}
		baseEnabled = state;
		fireHandlerChanged(new HandlerEvent(this, true, false));
	}

	@Override
	public void setEnabled(Object evaluationContext) {
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	protected boolean hasListeners() {
		return isListenerAttached();
	}

	@Override
	public void removeHandlerListener(final IHandlerListener handlerListener) {
		removeListenerObject(handlerListener);
	}	// TODO Auto-generated method stub

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		createButton.setEnabled(false);
		name.setText("");
		currentProjectId = event.getParameter("id");
		try {
			table.removeAll();
			JSONArray jsonMembersArray = 
					new JSONArray(GitLabApi.GitLabGetRequest("/projects/" + event.getParameter("id") + "/milestones"));
			System.out.println(jsonMembersArray);
			for(Object member_o : jsonMembersArray)
			{
				JSONObject member = (JSONObject) member_o;
				TableItem item = new TableItem(table,0);
				item.setText(0,member.getString("title"));
				item.setText(1,member.getString("state"));
			}
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createButton.setEnabled(true);
		return null;
	}
}