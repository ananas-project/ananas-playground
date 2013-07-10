package ananas.lib.impl.axk.client.target.roster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.api.roster.IExRosterManager;
import ananas.lib.axk.api.roster.IRosterContact;
import ananas.lib.axk.api.roster.IRosterGroup;
import ananas.lib.axk.constant.XmppSubscription;

public class RosterManagerImpl implements IRosterManagerInner, IExRosterManager {

	private final IRosterManagerInnerCallback mCallback;
	private final Map<String, IRosterGroup> mGroupMap = new Hashtable<String, IRosterGroup>();
	private final Map<XmppAddress, IRosterContact> mContactMap = new Hashtable<XmppAddress, IRosterContact>();

	// private final Map<XmppAddress, IRosterContactInner> mNeedPushContactMap =
	// new Hashtable<XmppAddress, IRosterContact>();

	private boolean mIsAutoPullAB;
	private int mRevision;
	private int mRevisionHEAD;

	public RosterManagerImpl(IRosterManagerInnerCallback callback) {
		this.mCallback = callback;
	}

	@Override
	public IExRosterManager toOuter() {
		return this;
	}

	@Override
	public IRosterManagerInnerCallback getCallback() {
		return this.mCallback;
	}

	@Override
	public boolean isAutoPullAfterBinding() {
		return this.mIsAutoPullAB;
	}

	@Override
	public void setAutoPullAfterBinding(boolean value) {
		this.mIsAutoPullAB = value;
	}

	@Override
	public IRosterGroup getGroup(String group) {
		return this.getGroup(group, false);
	}

	@Override
	public IRosterContact getContact(XmppAddress addr) {
		return this.getContact(addr, false);
	}

	private XmppAddress normalAddress(XmppAddress addr) {
		return addr.toPure();
	}

	@Override
	public boolean removeContact(XmppAddress addr) {
		addr = this.normalAddress(addr);
		IRosterContact rlt = this.mContactMap.remove(addr);
		return (rlt != null);
	}

	@Override
	public void reset() {
		this.mGroupMap.clear();
		this.mContactMap.clear();
		this.clearCache();
	}

	@Override
	public IRosterGroup getGroup(String groupName, boolean create) {
		groupName = this.normalGroupName(groupName);
		Map<String, IRosterGroup> map = this.mGroupMap;
		IRosterGroup group = map.get(groupName);
		if (group == null && create) {
			group = new MyGroup(groupName);
			map.put(groupName, group);
		}
		return group;
	}

	private String normalGroupName(String groupName) {
		return groupName.trim();
	}

	@Override
	public IRosterContact getContact(XmppAddress addr, boolean create) {
		addr = this.normalAddress(addr);
		Map<XmppAddress, IRosterContact> map = this.mContactMap;
		IRosterContact cont = map.get(addr);
		if (cont == null && create) {
			cont = new MyContact(addr);
			map.put(addr, cont);
		}
		return cont;
	}

	@Override
	public boolean removeGroup(String groupName) {
		IRosterGroup rlt = this.mGroupMap.remove(groupName);
		if (rlt == null) {
			return false;
		} else {
			this.clearCache();
			return true;
		}
	}

	private void clearCache() {
	}

	@Override
	public IRosterContact addContact(XmppAddress addr, String[] ownerGroups) {
		addr = this.normalAddress(addr);
		IRosterContact contact = this.getContact(addr, true);
		for (String g : ownerGroups) {
			IRosterGroup group = this.getGroup(g, true);
			group.bind(contact);
			contact.bind(group);
		}
		return contact;
	}

	class MyGroup implements IRosterGroup {

		private final Map<XmppAddress, IRosterContact> mTable;
		private final String mGroupName;
		private int mRevision;

		public MyGroup(String groupName) {
			this.mGroupName = groupName;
			this.mTable = new Hashtable<XmppAddress, IRosterContact>();
		}

		@Override
		public IExRosterManager getRoster() {
			return RosterManagerImpl.this;
		}

		@Override
		public String getName() {
			return this.mGroupName;
		}

		@Override
		public Collection<IRosterContact> getContacts() {
			return this.mTable.values();
		}

		@Override
		public boolean bind(IRosterContact contact) {
			XmppAddress addr = contact.getAddress().toPure();
			this.mTable.put(addr, contact);
			return true;
		}

		@Override
		public boolean unbind(IRosterContact contact) {
			IRosterContact rlt = this.mTable.remove(contact.getAddress()
					.toPure());
			return (rlt != null);
		}

		@Override
		public int getRevision() {
			return this.mRevision;
		}
	}

	class MyContact implements IRosterContactInner, IRosterContact {

		private final List<IRosterGroup> mOwnerGroupList;
		private final XmppAddress mAddr;
		private String mName;
		private int mRevision;
		private XmppSubscription mSubscription;

		public MyContact(XmppAddress addr) {
			this.mAddr = addr;
			this.mOwnerGroupList = new ArrayList<IRosterGroup>(3);
		}

		@Override
		public IExRosterManager getRoster() {
			return RosterManagerImpl.this;
		}

		@Override
		public String getName() {
			return this.mName;
		}

		@Override
		public XmppAddress getAddress() {
			return this.mAddr;
		}

		@Override
		public boolean bind(IRosterGroup group) {
			if (this.mOwnerGroupList.contains(group)) {
				return true;
			}
			boolean rlt = this.mOwnerGroupList.add(group);
			return rlt;
		}

		@Override
		public boolean unbind(IRosterGroup group) {
			boolean rlt = this.mOwnerGroupList.remove(group);
			return rlt;
		}

		@Override
		public Collection<IRosterGroup> getOwnerGroups() {
			return this.mOwnerGroupList;
		}

		@Override
		public IRosterContact toOuter() {
			return this;
		}

		@Override
		public int getRevision() {
			return this.mRevision;
		}

		@Override
		public void setName(String name) {
			this.mName = name;
		}

		@Override
		public void setSubscription(XmppSubscription subscription) {
			this.mSubscription = subscription;
		}

		@Override
		public XmppSubscription getSubscription() {
			return this.mSubscription;
		}

		@Override
		public void setPrepareToPush(boolean value) {
			final int head = RosterManagerImpl.this.mRevisionHEAD;
			if (value) {
				this.mRevision = head + 1;
			} else {
				this.mRevision = head;
			}
		}

		@Override
		public boolean isPrepareToPush() {
			final int head = RosterManagerImpl.this.mRevisionHEAD;
			return (this.mRevision > head);
		}
	}

	@Override
	public Collection<IRosterContact> getContacts() {
		return this.mContactMap.values();
	}

	@Override
	public Collection<IRosterGroup> getGroups() {
		return this.mGroupMap.values();
	}

	@Override
	public boolean removeContact(IRosterContact contact) {
		IRosterContact rlt = this.mContactMap.remove(contact.getAddress()
				.toPure());
		return (rlt != null);
	}

	@Override
	public boolean removeGroup(IRosterGroup group) {
		IRosterGroup rlt = this.mGroupMap.remove(group.getName());
		return (rlt != null);
	}

	@Override
	public void push() {
		this.mCallback.doPush();
	}

	@Override
	public void pull() {
		this.mCallback.doPull();
	}

	@Override
	public int getRevision() {
		return this.mRevision;
	}

}
