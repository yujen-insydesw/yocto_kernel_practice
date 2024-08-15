
### remove remote

https://dev.to/dance2die/push-git-cloned-repository-to-your-own-on-github-1ili#:~:text=First%2C%20remove%20the%20remote%20repository%20associated%20using%20git,you%20can%20push%2Fpublish%20it%20to%20your%20own%20repository%21



### yocto_kernel_practice




Make sure your Build Host meets the following requirements:

50 Gbytes of free disk space
Runs a supported Linux distribution (i.e. recent releases of Fedora, openSUSE, CentOS, Debian, or Ubuntu). I use Ubuntu 22.04.1 LTS
Required Software versions
Git 1.8.3.1 or greater
tar 1.28 or greater
Python 3.6.0 or greater.
gcc 7.5 or greater.
GNU make 4.0 or greater
Note: Build Host is the system used to build images in a Yocto Project Development environment.

Install Host Packages
You must install essential host packages on your build host.

sudo apt-get install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev pylint3 xterm python3-subunit mesa-common-dev zstd liblz4-tool
The preceding command will use apt-get, the Advanced Packaging Tool (APT), command-line tool. It is a frontend of the dpkg package manager that is included in the Ubuntu distribution. It will install all the required packages and their dependencies to support all the features of the Yocto project.

Install Poky
Use the following commands to clone the Poky repository.

git clone git://git.yoctoproject.org/poky
Then move to the poky directory and take a look at existing branches:

cd poky
git branch -a
check out the kirkstone branch based as we will be using the Kirkstone release:

git checkout -t origin/kirkstone -b my-kirkstone
The above Git checkout command creates a local branch named my-kirkstone. The files available to you in that branch exactly match the repository’s files in the kirkstone release branch.



qt可參考
https://github.com/joaocfernandes/Learn-Yocto/blob/master/develop/Recipe-qt5.md


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
meta-application      /media/disk4T/yujen/poky/meta-application  6
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




./tmp/sysroots-components/core2-64/hellocmake
./tmp/sysroots-components/core2-64/hellocmake/sysroot-providers/hellocmake
./tmp/work/qemux86_64-poky-linux/core-image-minimal/1.0-r0/rootfs/usr/bin/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/package/usr/src/debug/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/package/usr/bin/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/package/usr/bin/.debug/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/license-destdir/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/build/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/pkgdata/runtime/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/pkgdata/runtime-reverse/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/pkgdata/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/pkgdata-pdata-input/runtime/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/pkgdata-pdata-input/runtime-reverse/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/pkgdata-pdata-input/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/sysroot-destdir/sysroot-providers/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/packages-split/hellocmake-dbg/usr/bin/.debug/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/packages-split/hellocmake-src/usr/src/debug/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/packages-split/hellocmake
./tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/packages-split/hellocmake/usr/bin/hellocmake
./tmp/stamps/core2-64-poky-linux/hellocmake
./tmp/log/cleanlogs/hellocmake
./tmp/pkgdata/qemux86-64/runtime/hellocmake
./tmp/pkgdata/qemux86-64/runtime-reverse/hellocmake
./tmp/pkgdata/qemux86-64/hellocmake
./tmp/deploy/licenses/hellocmake



git 操作

git rm --cached submodule_path  # delete reference to submodule HEAD (no trailing slash)
git rm .gitmodules              # if you have more than one submodules,
                                # you need to edit this file instead of deleting!
rm -rf submodule_path/.git      # make sure you have backup!!
git add --force submodule_path  # will add files instead of commit reference
                                # --force adds files ignored by .gitignore
git commit -m "remove submodule"
