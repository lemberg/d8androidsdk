### Description

[Drupal 8 iOS & Android SDKs](http://drupalsdk.com)

Lemberg Drupal SDK is a library for native **Android** applications to communicate with **Drupal** web servers.

Currently library is [using google volley] and [gson] libraries for communication with server and object serialization/deserialization.

Main purpose of this library is to make communication with Drupal 8 - based servers as easy and intuitive as possible.

1. Synchronous and asynchronous requests
2. Flexible object serialization / deserialization
3. API can calculate object differences to perform patch requests
4. Simple entities request

### Wiki

* [Home]
* [Integration]
* [User Guide]
* [Sample]
* [License]

### Integration

The lib **soon will be available** on Maven Central, you can find it with [Gradle, please].

```
dependencies {
    compile 'com.ls.drupal:library:1.0.0'
}
```

### Contributions

If you want to contribute to this library make sure you send pull request to **dev** branch.

### License

```
The MIT License (MIT)

Copyright (c) 2014 Lemberg Solutions Limited

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

[using google volley]:https://android.googlesource.com/platform/frameworks/volley
[gson]:https://code.google.com/p/google-gson/
[Home]:https://github.com/lemberg/d8androidsdk/wiki
[Integration]:https://github.com/lemberg/d8androidsdk/wiki/Integration
[User Guide]:https://github.com/lemberg/d8androidsdk/wiki/User-Guide
[Sample]:https://github.com/lemberg/d8androidsdk/wiki/Sample
[License]:https://github.com/lemberg/d8androidsdk/wiki/License
[Gradle, please]:http://gradleplease.appspot.com/
