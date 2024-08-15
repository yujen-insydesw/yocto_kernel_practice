SUMMARY = "Simple Hello World application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

#SRC_URI = "file://../ "
SRC_URI = "file://Makefile \
           file://include/echo.h \
           file://source/echo.cpp \
           file://source/hello.cpp \
          "

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
