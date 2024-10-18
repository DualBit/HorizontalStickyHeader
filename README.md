![Language](https://img.shields.io/github/languages/top/cortinico/kotlin-android-template?color=blue&logo=kotlin)
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.karumi/headerrecyclerview/badge.svg)
# Horizontal Sticky Headers in Android RecyclerView
==================

This project demonstrates how to implement Horizontal Sticky Headers in a ``RecyclerView`` using a
``HHeaderItemDecoration`` a custom ``RecyclerView.ItemDecoration`` class in Kotlin.

Screenshots
-----------

![Demo Screenshot][1]

Usage
-----

To use ``HeaderRecyclerView`` in your application follow those steps:

* 1 - Add mavenCentral() to repositories block in the gradle file.
* 2 - Add `implementation("com.github.dualbit:horizontalstickyheader:1.0.0")` to the dependencies.
* 3 - Create a normal ``RecyclerView`` and ``RecyclerView.Adapter``.
* 4 - Create a variable ``headerDecoration`` that provide the required the custom parameter ``AdapterElementType`` to configure the ``HHeaderItemDecoration``.

```kotlin

private val headerDecoration by lazy {
  HHeaderItemDecoration(viewBinding.rclMain, shouldFadeOutHeader = false) {
    viewBinding.rclMain.isSectionViewType(it, adapter)
  }
}

```

* 5 - Add ``headerDecoration`` to your ``RecyclerView``
  ``RecyclerView``:

```kotlin

viewBinding.rclMain.addItemDecoration(headerDecoration)

```

How it works
-----
The core functionality is implemented in the HHeaderItemDecoration class, which extends
``RecyclerView.ItemDecoration``. This class provides the logic to:

* Detect if an item is a header using a lambda function (isHeader).
* Draw the header over the list items using onDrawOver and manage transitions.
* Optionally fade out the header during transitions.

```kotlin

class HHeaderItemDecoration(
    parent: RecyclerView,
    private val shouldFadeOutHeader: Boolean = false,
    private val isHeader: (itemPosition: Int) -> Boolean
) : RecyclerView.ItemDecoration() {
    // Code details as provided earlier
}

```

Additional Features
-------------------
Handling Click Events on Sticky Headers: The class includes logic to intercept touch events and handle clicks on sticky headers.

```kotlin

parent.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
  override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {
    return if (motionEvent.action == MotionEvent.ACTION_DOWN) {
      motionEvent.x <= (currentHeader?.second?.itemView?.right ?: 0)
    } else false
  }
})

```

Do you want to contribute?
--------------------------

Please, do it! We'd like to improve this library with your help :)

License
-------

    Copyright 2024 Dualbit

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: ./asset/screenshot_demo_1.gif