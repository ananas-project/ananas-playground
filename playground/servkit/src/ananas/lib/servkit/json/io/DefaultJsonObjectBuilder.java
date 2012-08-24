package ananas.lib.servkit.json.io;

import java.util.Stack;

import ananas.lib.servkit.json.JSON;
import ananas.lib.servkit.json.JsonException;
import ananas.lib.servkit.json.object.IJsonArray;
import ananas.lib.servkit.json.object.IJsonDouble;
import ananas.lib.servkit.json.object.IJsonInteger;
import ananas.lib.servkit.json.object.IJsonLong;
import ananas.lib.servkit.json.object.IJsonObject;
import ananas.lib.servkit.json.object.IJsonString;
import ananas.lib.servkit.json.object.IJsonValue;
import ananas.lib.servkit.pool.IBasePool;

public class DefaultJsonObjectBuilder implements IJsonObjectBuilder {

	private final IBasePool mPool;
	private IJsonValue mRoot;
	private final Stack<IJsonValue> mStack;
	private IJsonLocator mLocator;
	private String mCurKey;

	public DefaultJsonObjectBuilder(IBasePool pool) {
		this.mPool = pool;
		this.mStack = new Stack<IJsonValue>();
	}

	@Override
	public void onDocumentPrepare() {
		this.mStack.clear();
		this.mRoot = null;
	}

	@Override
	public void onSetLocator(IJsonLocator locator) {
		this.mLocator = locator;
	}

	@Override
	public void onDocumentBegin() {
	}

	@Override
	public void onDocumentEnd() {
	}

	@Override
	public void onDocumentFinal() {
	}

	@Override
	public void onException(Exception exception) {
		System.err.println("exception in file, line:" + this.mLocator.getLine()
				+ " col:" + this.mLocator.getColumn());
		exception.printStackTrace();
	}

	@Override
	public void onArrayBegin() throws JsonException {
		IJsonArray array = (IJsonArray) this.mPool.alloc(JSON.class_array);
		this._push(array);
	}

	private IJsonValue _pop() {
		return this.mStack.pop();
	}

	private void _push(IJsonValue value) throws JsonException {

		IJsonValue parent = null;
		if (!this.mStack.empty()) {
			parent = this.mStack.peek();
		}

		if (parent == null) {
			this.mRoot = value;

		} else if (parent instanceof IJsonArray) {
			IJsonArray array = (IJsonArray) parent;
			array.add(value);

		} else if (parent instanceof IJsonObject) {
			IJsonObject object = (IJsonObject) parent;
			object.put(this.mCurKey, value);
			this.mCurKey = null;

		} else {
			throw MyException.defaultException;
		}

		if ((value instanceof IJsonArray) || (value instanceof IJsonObject)) {
			this.mStack.push(value);
		}

	}

	@Override
	public void onArrayEnd() throws JsonException {
		IJsonValue rlt = this._pop();
		if (!(rlt instanceof IJsonArray)) {
			throw MyException.defaultException;
		}
	}

	private static class MyException {

		public static final JsonException defaultException = new JsonException();
	}

	@Override
	public void onObjectBegin() throws JsonException {
		IJsonObject obj = (IJsonObject) this.mPool.alloc(JSON.class_object);
		this._push(obj);
	}

	@Override
	public void onObjectEnd() throws JsonException {
		IJsonValue rlt = this._pop();
		if (!(rlt instanceof IJsonObject)) {
			throw MyException.defaultException;
		}
	}

	@Override
	public void onObjectKey(String key) {
		this.mCurKey = key;
	}

	@Override
	public void onString(String s) throws JsonException {
		IJsonString str = (IJsonString) this.mPool.alloc(JSON.class_string);
		str.setData(s);
		this._push(str);
	}

	@Override
	public void onBoolean(boolean value) throws JsonException {
		IJsonValue val = value ? JSON.value_true : JSON.value_false;
		this._push(val);
	}

	@Override
	public void onInteger(int value) throws JsonException {
		IJsonInteger val = (IJsonInteger) this.mPool.alloc(JSON.class_int);
		val.setValue(value);
		this._push(val);
	}

	@Override
	public void onLong(long value) throws JsonException {
		IJsonLong val = (IJsonLong) this.mPool.alloc(JSON.class_long);
		val.setValue(value);
		this._push(val);
	}

	@Override
	public void onDouble(double value) throws JsonException {
		IJsonDouble val = (IJsonDouble) this.mPool.alloc(JSON.class_double);
		val.setValue(value);
		this._push(val);
	}

	@Override
	public void onNull() throws JsonException {
		this._push(JSON.value_null);
	}

	@Override
	public IBasePool getPool() {
		return this.mPool;
	}

	@Override
	public IJsonValue getRoot() {
		return this.mRoot;
	}
}
