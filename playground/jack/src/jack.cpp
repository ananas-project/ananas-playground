//============================================================================
// Name        : jack.cpp
// Author      : xukun
// Version     :
// Copyright   : (C)2013 xukun
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include <stdio.h>

#include "jack/lang/Object.h"

using namespace std;
using namespace jack::lang;

int main() {

	//cout << "!!!Hello World!!!" << endl; // prints !!!Hello World!!!

	Ref<IObject> obj(new HeapObject<CObject>());

	return 0;
}
