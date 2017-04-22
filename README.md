# ObjectCache
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![Build Status](https://travis-ci.org/myntra/ObjectCache.svg?branch=master)](https://travis-ci.org/myntra/ObjectCache)
[![Jitpack](https://jitpack.io/v/myntra/ObjectCache.svg)](https://jitpack.io/#myntra/ObjectCache)

A simple means of storing `Objects` in Cache.

It uses gson internally to serialize and de-serialize `Objects`. Its built on top of the awesome [DiskLruCache](https://github.com/JakeWharton/DiskLruCache).
DiskLruCache has been extended to support explicit eviction of entries based on an expiry.

*Note: This implementation specifically targets Android compatibility.*

### Install

Add jitpack to your root `build.gradle`
```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
	
Add the dependency
```
	dependencies {
	        compile 'com.github.myntra:ObjectCache:1.0.0'
	}
```

License
-------

    Copyright 2017 Myntra

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.