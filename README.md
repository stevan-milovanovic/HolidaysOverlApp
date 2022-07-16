# HolidaysOverlApp

This is an example app which shows list of countries fetched from https://holidayapi.com API.
Countries are shown as filter chips and when two are selected, national holidays intersection is shown.
In the action bar, you can change filter type from intersection to holidays present only in the first or second country.

<div style="display: flex; justify-content: space-between; align-items: center;">
  <img src="https://github.com/stevan-milovanovic/HolidaysOverlApp/blob/master/initial.png" alt="Initial" width="200"/>
  &nbsp
  <img src="https://github.com/stevan-milovanovic/HolidaysOverlApp/blob/master/no%20internet.png" alt="No internet" width="200"/>
  &nbsp
  <img src="https://github.com/stevan-milovanovic/HolidaysOverlApp/blob/master/holidays%20intersection.png" alt="Holidays Intersection" width="200"/>
  &nbsp
  <img src="https://github.com/stevan-milovanovic/HolidaysOverlApp/blob/master/filters.png" alt="Filters" width="200"/>
</div>

### Libraries
* [AndroidX Core][androidx-core]
* [Material Design][material]
* [Android View Binding][view-binding]
* [Hilt][hilt] for dependency injection
* [Retrofit][retrofit] for REST api communication
* [Room][room] for saving data in a local database
* [Glide][glide] for image loading

[androidx-core]: https://developer.android.com/topic/libraries/support-library/index.html
[material]: https://github.com/material-components/material-components-android
[view-binding]: https://developer.android.com/topic/libraries/view-binding
[hilt]: https://developer.android.com/training/dependency-injection/hilt-android
[retrofit]: http://square.github.io/retrofit
[room]: https://developer.android.com/jetpack/androidx/releases/room
[glide]: https://github.com/bumptech/glide

License
--------

Copyright 2022 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
