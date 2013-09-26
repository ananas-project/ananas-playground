/*
 * Object.h
 *
 *  Created on: 2013-9-18
 *      Author: xukun
 */

#ifndef OBJECT_H_
#define OBJECT_H_

#include "Base.h"

namespace jack {
namespace lang {

#define interface  class
#define implements public virtual
#define extends public
#define nil 0

interface IObject {
public:
	virtual ~IObject();
public:
	virtual IObject * retain() =0;
	virtual void release() =0;
	virtual IObject * autorelease() =0;
};

class CObject: implements IObject {
};

template<typename T> class StackObject: public T {
};

template<typename T> class HeapObject: public T {

private:
	int _refcnt;

public:

	HeapObject() :
			_refcnt(0) {
	}

	template<typename TP1>
	HeapObject(TP1 p1) :
			_refcnt(0) {
	}

	template<typename TP1, typename TP2>
	HeapObject(TP1 p1, TP2 p2) :
			_refcnt(0) {
	}

public:

	IObject * retain() {

		return (this);
	}

	void release() {
	}

	IObject * autorelease() {

		return (this);
	}

};

// object reference

class RefBase {
protected:
	static void swap(void ** pp, IObject * pnew);
};

template<typename T> class Ref: public RefBase {
private:

	T * p;

	T * __swap(T* pnew) {
		// synchronized
		T * pold = p;
		p = pnew;
		return pold;
	}

public:
	Ref() :
			p(nil) {
	}
	Ref(T*init) :
			p(init) {
		if (p) {
			p->retain();
		}
	}
	Ref(const Ref & init) :
			p(init.p) {
		if (p) {
			p->retain();
		}
	}
	~ Ref() {
		if (p) {
			p->release();
		}
	}

	const Ref & operator=(const Ref & other) {
		T*pnew = other.p;
		T*pold = __swap(pnew);
		if (pnew) {
			pnew->retain();
		}
		if (pold) {
			pold->release();
		}
		return (*this);
	}
	const Ref & operator=(const T * other) {
		swap(&p, other);
		return (*this);
	}

};

} /* namespace lang */
} /* namespace jack */

#endif /* OBJECT_H_ */
