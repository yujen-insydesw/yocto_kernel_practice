UMMARY = "Simple Hello World application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

#SRC_URI = "file://../ "
SRC_URI = "file://include/echo.h \
           file://source/echo.cpp \
           file://source/hello.cpp \
          "

INCLUDE = "include"
SOURCE = "source/echo.cpp source/hello.cpp"

# ld flag can also set here TARGET_CC_ARCH += "${LDFLAGS}" 

S = "${WORKDIR}"

do_compile() {
  ${CXX} ${SOURCE} -I ${INCLUDE} ${LDFLAGS} -o hello
}

do_install() {
  install -d ${D}${bindir}
  install -m 0755 hello ${D}${bindir}
}
