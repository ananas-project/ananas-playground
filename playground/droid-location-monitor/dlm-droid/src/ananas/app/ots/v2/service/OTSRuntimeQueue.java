package ananas.app.ots.v2.service;

import java.util.LinkedList;
import java.util.Queue;

import ananas.app.ots.v2.pojo.OTSLocation;

public class OTSRuntimeQueue extends AbstractOTSRuntime {

	private final Queue<OTSLocation> mQueue;
	private final int mMaxQueueSize;

	public OTSRuntimeQueue(OTSServiceContext context) {
		super(context);
		this.mMaxQueueSize = context.getConfig().getLastLocationQueueSizeMax();
		this.mQueue = new LinkedList<OTSLocation>();
	}

	public void open() {
		this.mQueue.clear();
	}

	public void close() {
		this.mQueue.clear();
	}

	private static void trim(Queue<OTSLocation> queue, int lengthLimit) {
		lengthLimit = Math.max(lengthLimit, 0);
		for (;;) {
			final int len = queue.size();
			if (len > lengthLimit) {
				queue.poll();
			} else {
				break;
			}
		}
	}

	public OTSLocation[] getLastLocations(int limitCount) {
		LinkedList<OTSLocation> list = new LinkedList<OTSLocation>(this.mQueue);
		trim(list, limitCount);
		return list.toArray(new OTSLocation[list.size()]);
	}

	@Override
	public void onLocation(OTSLocation location) {
		this.mQueue.add(location);
		trim(this.mQueue, this.mMaxQueueSize);
	}

}
