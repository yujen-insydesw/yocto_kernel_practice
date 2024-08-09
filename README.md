### yocto_kernel_practice


### Creating a new layer for the calculator application project

Create a new layer named meta-*

```
$ bitbake-layers create-layer meta-application
```

BitBake should've created the following tree (with additional recipe-example):

```
meta-application/
├── conf
│   └── layer.conf
├── COPYING.MIT
├── README
└── recipes-example
    └── example
        └── example.bb
```

Configure layer.conf file:

```
# We have a conf and classes directory, add to BBPATH
BBPATH .= ":${LAYERDIR}"
LAYERSERIES_COMPAT_meta-application = "sumo"

# We have recipes-* directories, add to BBFILES
BBFILES += "${LAYERDIR}/recipes-*/*/*.bb \
            ${LAYERDIR}/recipes-*/*/*.bbappend"

BBFILE_COLLECTIONS += "meta-application"
BBFILE_PATTERN_application = "^${LAYERDIR}/"
BBFILE_PRIORITY_application = "6"

IMAGE_INSTALL += "_make _cmake "
```

# Adding Recipe

Folder structure should look like:

```console
joao@joao-ThinkPad-T470p:~/yocto/poky$ tree meta-application/
        ├── files/
        │   ├── CMakeLists.txt
        │   ├── include/
        │   │   └── echo.h
        │   └── source/
        │       ├── echo.cpp
        │       └── hello.cpp
    └── hellocmake/
        └── hellocmake.bb
```

The following display how to simply build with yocto.

hello.bb
```
SUMMARY = "Simple Hello World application"

SRC_URI = "file://../ "
#SRC_URI = "file://../include/ \
#           file://../source/ \
#          "

INCLUDE = "./../include/echo.h"
SOURCE = "./../source/echo.c ./../source/hello.c"

# ld flag can also set here TARGET_CC_ARCH += "${LDFLAGS}" 

S = "${WORKDIR}"

do_compile() {
  ${CC} ${SOURCE} -I ${INCLUDE} ${LDFLAGS} -o hello
}

do_install() {
  install -d ${D}${bindir}
  install -m 0755 hello ${D}${bindir}
}
```

The following display how to makefile build with yocto.

hellomake.bb
```
SUMMARY = "Simple Hello World application"

SRC_URI = "file://../ "
#SRC_URI = "file://../Makefile \
#           file://../include/ \
#           file://../source/ \
#          "

S = "${WORKDIR}"

do_compile() {
    make
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 hellomake ${D}${bindir}
}

# Clean the build
do_clean() {
    make clean
}
```

Makefile
```
# Compiler
CXX = g++
CXXFLAGS = -Iinclude -std=c++11

# Source files
SOURCE = source/echo.cpp source/hello.cpp

# Output executable
TARGET = hellomake

# Build rule
all: $(TARGET)

$(TARGET): $(SOURCE)
	$(CXX) $(CXXFLAGS) -o $@ $^

# Clean rule
clean:
	rm -f $(TARGET)
```

The Yocto build system contains classes to support building CMake packages. To use CMake in a recipe you need to inherit the CMake class.
Generally the CMake build system knows how to install the software so a overwrite over do_install is not necessary.

hellocmake.bb
```
SUMMARY = "Simple Hello World application"

SRC_URI = "file://../ "
#SRC_URI = "file://../CMakeLists.txt \
#           file://../include/ \
#           file://../source/ \
#          "

S = "${WORKDIR}"

inherit cmake

EXTRA_OECMAKE = ""
```

CMakeLists.txt
```
cmake_minimum_required(VERSION 3.10)
project(HelloCmake)

# Specify the C++ standard
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Specify path
set(INCLUDE include)
set(SOURCE
    source/echo.cpp
    source/hello.cpp
)

# Include directories
include_directories(${INCLUDE})

# Add the executable
add_executable(HelloCmake ${SOURCE})
```

### Building the system image

First make sure bblayers.conf include sufficient meta-* layers. Build your system image:

```
$ bitbake -k core-image-base
```

Should have executable under ```/.../```

When you boot your image, under ```/.../``` should have executable.








```

#ifndef MY_HEADER_H
#define MY_HEADER_H

void myFunction();

#endif // MY_HEADER_H



#include "my_header.h"
#include <iostream>

void myFunction() {
    std::cout << "Hello from myFunction!" << std::endl;
}





#include "my_header.h"

int main() {
    myFunction();
    return 0;
}

```
