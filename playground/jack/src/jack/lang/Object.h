/*
 * Object.h
 *
 *  Created on: 2013-9-18
 *      Author: xukun
 */

#ifndef OBJECT_H_
#define OBJECT_H_

namespace jack {

#define interface  class
#define implements public virtual
#define extends public
#define nil 0

interface IObject {
public:
	virtual ~IObject();
public:
	virtual void setAlive(int number) =0;
	virtual int getAlive() =0;
};

class CObject: implements IObject {
};

template<typename T> class Ref {
private:
	T * p;
public:
	Ref() :
			p(nil) {
	}
	Ref(T*init) :
			p(init) {
	}
	Ref(const Ref & init) :
			p(init.p) {
	}
	~ Ref() {
	}
};

} /* namespace fuyoo */
#endif /* OBJECT_H_ */
