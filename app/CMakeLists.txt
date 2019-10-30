# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
#CMakeLists.txt
cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#用来设置编译生成的本地库
add_library(
        # Sets the name of the library.
        # 设置so文件名称.
        encrypt

        # Sets the library as a shared library.
        # 表示编译生成的是动态链接库.
        SHARED

        # Provides a relative path to your source file(s).
        # 参与编译的文件的路径，这里面可以写多个文件的路径.
        src/main/jni/com_achpay_wallet_utils_NativeMethods.cpp)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.
#是用来添加一些我们在编译我们的本地库的时候需要依赖的一些库
find_library(
        # Sets the name of the path variable.
        #为依赖的库设置一个名字
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        # 添加要链接的库
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
#是为了关联我们自己的库和一些第三方库或者系统库
target_link_libraries( # Specifies the target library.
        # 制定目标库.
        encrypt

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})