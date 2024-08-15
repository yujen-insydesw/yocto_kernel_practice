# yocto_kernel_practice
A practice about how to cook a minimal linux image attached with customized applications. Here, we use Ubuntu 22.? on x86_64 machine.

## Make sure the build server meets the following requirements:
- 50 Gbytes of free disk space
- Git 1.8.3.1 or greater
- tar 1.28 or greater
- Python 3.6.0 or greater.
- gcc 7.5 or greater.
- GNU make 4.0 or greater

## Install sufficient packages or tools
```
sudo apt-get install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev pylint3 xterm python3-subunit mesa-common-dev zstd liblz4-tool
```

## Git clone poky (and remove git connection just for practice) 
### git clone
```
git clone git://git.yoctoproject.org/poky
```
### check out the kirkstone branch (Kirkstone release) and create new branch
```
cd poky
git checkout -t origin/kirkstone -b my-kirkstone
cd ..
```
### remove git (or submodule) connection
```
git rm --cached poky
git rm .gitmodules
rm -rf poky/.git
```
### upload poky (--force adds files ignored by .gitignore)
```
git add --force poky
git commit -m "..."
git push
```

## Initialize the project (each time)
```
cd poky
source oe-init-build-env

Common (bitbake) targets are:
    core-image-minimal
    core-image-full-cmdline
    core-image-sato
    core-image-weston
    meta-toolchain
    meta-ide-support
``` 

## Creating a new layer
### create a new layer named meta-*
```
bitbake-layers create-layer meta-application
```
### bitBake would create additional recipe-example, we can remove it or change its name
```
tree

meta-application/
├── conf
│   └── layer.conf
├── COPYING.MIT
├── README
└── recipes-example
    └── example
        └── example.bb
```
### add new layer (to build/conf/bblayer.conf)
```
bitbake-layers add-layer meta-application

# POKY_BBLAYERS_CONF_VERSION is increased each time build/conf/bblayers.conf
# changes incompatibly
POKY_BBLAYERS_CONF_VERSION = "2"

BBPATH = "${TOPDIR}"
BBFILES ?= ""

BBLAYERS ?= " \
  /media/disk4T/yujen/poky/meta \
  /media/disk4T/yujen/poky/meta-poky \
  /media/disk4T/yujen/poky/meta-yocto-bsp \
  /media/disk4T/yujen/poky/meta-application \
  "
```
### show layers
```
bitbake-layers show-layers

layer                 path                                      priority
==========================================================================
meta                  /media/disk4T/yujen/poky/meta             5
meta-poky             /media/disk4T/yujen/poky/meta-poky        5
meta-yocto-bsp        /media/disk4T/yujen/poky/meta-yocto-bsp   5
meta-application      /media/disk4T/yujen/poky/meta-application  6
```
### configure meta-*/conf/layer.conf file
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

# The following seem to be unnecessary
IMAGE_INSTALL += "_make _cmake "
```

## Add Recipes
### we have receipes-hello folder, which contains receipe hello, hellomake, hellocmake
```console
tree meta-application/

meta-application/
├── conf
│   └── layer.conf
├── COPYING.MIT
├── README
└── recipes-hello
    ├── files
    │   ├── CMakeLists.txt
    │   ├── files -> ../files
    │   ├── include
    │   │   └── echo.h
    │   ├── Makefile
    │   └── source
    │       ├── echo.cpp
    │       └── hello.cpp
    ├── hello
    │   ├── files -> ../files
    │   └── hello.bb
    ├── hellocmake
    │   ├── files -> ../files
    │   └── hellocmake.bb
    └── hellomake
        ├── files -> ../files
        └── hellomake.bb
```

## Bitbake
### bitbake <target>
```
bitbake core-image-minimal
```
### bitbake <receipe>
```
bitbake hello
bitbake hellomake
bitbake hellocmake

# out put to image:
{poky}/build/tmp/work/core2-64-poky-linux/hellocmake/1.0-r0/image/usr/bin/hellocmake

# where is core image:
{poky}/build/tmp/work/qemux86_64-poky-linux/core-image-minimal/1.0-r0/rootfs/bin/
{poky}/build/tmp/work/qemux86_64-poky-linux/core-image-minimal/1.0-r0/rootfs/bin/
```

## Some question unsolved
### not necessary to install to image ?
meta-application/recipes-hello/hellocmake/hellocmake.bb
IMAGE_INSTALL += " hellocmake"
### not work to install to core image
meta/recipes-core/images/core-image-minimal.bbappend
IMAGE_INSTALL:append = " hellocmake"
inherit core-image-minimal
### it work (build/conf/local.conf)
build/conf/local.conf
IMAGE_INSTALL:append = " hellocmake"

## Future question unsolved
### install qt
https://github.com/joaocfernandes/Learn-Yocto/blob/master/develop/Recipe-qt5.md
