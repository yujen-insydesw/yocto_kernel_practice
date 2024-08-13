### yocto_kernel_practice


### Creating a new layer for the calculator application project

Create a new layer named meta-*

```
$ bitbake-layers create-layer meta-application

$ bitbake-layers add-layer meta-application

ntc2@insydesc2:/media/disk4T/yujen/poky/build$ bitbake-layers show-layers
/usr/lib/python3/dist-packages/html5lib/_trie/_base.py:3: DeprecationWarning: Using or importing the ABCs from 'collections' instead of from 'collections.abc' is deprecated since Python 3.3, and in 3.9 it will stop working
  from collections import Mapping
NOTE: Starting bitbake server...
layer                 path                                      priority
==========================================================================
meta                  /media/disk4T/yujen/poky/meta             5
meta-poky             /media/disk4T/yujen/poky/meta-poky        5
meta-yocto-bsp        /media/disk4T/yujen/poky/meta-yocto-bsp   5
meta-application      /media/disk4T/yujen/poky/build/meta-application  6
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
project(hellocmake)

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
add_executable(hellocmake ${SOURCE})
```

### Building the system image

First make sure bblayers.conf include sufficient meta-* layers. Build your system image:

```
$ bitbake -k core-image-base
```

Should have executable under ```/.../```

When you boot your image, under ```/.../``` should have executable.








```

#ifndef ECHO_H
#define ECHO_H

void hello_world();

#endif // ECHO_H


echo.cpp

#include "echo.h"
#include <iostream>

void hello_world() {
    std::cout << "Hello from myFunction!" << std::endl;
}



hello.cpp

#include "echo.h"

int main() {
    hello_world();
    return 0;
}

```

### 尚未確認的地方

Common targets are:
    core-image-minimal
    core-image-full-cmdline
    core-image-sato
    core-image-weston
    meta-toolchain
    meta-ide-support




/media/disk4T/yujen/poky/build/tmp/work/qemux86_64-poky-linux/core-image-minimal/1.0-r0/rootfs/bin/






/media/disk4T/yujen/poky/build/tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/image/usr/bin/hellocmake

/media/disk4T/yujen/poky/meta-application/conf/layer.conf

/media/disk4T/yujen/poky/meta-application/recipes-hello/hellocmake/hellocmake.bb

cmake install 就夠了 其他沒作用 @@

IMAGE_INSTALL += " hellocmake"





直接加沒作用 ... 可能是沒有清cache的關係

/media/disk4T/yujen/poky/meta/recipes-core/images/core-image-minimal.bbappend

IMAGE_INSTALL:append = " hellocmake"

inherit core-image-minimal




加在build有作用

/media/disk4T/yujen/poky$ vim build/conf/local.conf

IMAGE_INSTALL:append = " hellocmake"



