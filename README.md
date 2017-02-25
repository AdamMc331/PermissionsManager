PermissionsManager Library
=============

This library contains a base Fragment and Activity class that allow for easier handling of Runtime Permission requests and their various outcomes (granted, denied, blocked).

Usage
-----

To have access to the library, add the dependency to your build.gradle:

```java
	compile 'com.adammcneilly:permissionsmanager:1.0.0'
```

At the time of publication, the library has not yet been linked to JCenter, so you will also have to add the link to my Maven repository as well:

```java
	repositories {
    	maven {
        	url  "http://dl.bintray.com/adammc331/maven"
    	}
	}
```

Implementation
--------------

The basic steps to implement this library are as follows:

1. Extend PermissionsActivity or PermissionsFragment
2. Implement (or create a class that implements) [PermissionsManager](lib/src/main/java/com/adammcneilly/permissionsmanager/PermissionsManager.kt)
3. Add the implementation of your permissions calls! This may very depending on what you want to do with the results. For an example, look at the `permissionsCheck()` method in the `MainActivity.java` of the sample.

Credits & Contact
-----------------

This library was created by [Adam McNeilly](http://adammcneilly.com).

Version History
---------------

####1.0.0
 - Initial release.

License
-------

The PermissionsManager library is available under the [MIT License](https://opensource.org/licenses/MIT). You are free to modify and enhance it in any way. If you submit a pull request, please add your name into the credits section!

