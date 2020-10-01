# WP-Droid [WIP]

A project to convert your wordpress website into an android app. This app uses the [WP API](v2.wp-api.org/) to work properly.
The app is just an implementation of how WordPress API can be used to extend WordPress API and convert the blog into an app.

# Installation

* Install [WP API](http://v2.wp-api.org/) on your WordPress site.

* Clone this repository and edit [posts-api.php](https://github.com/arjit95/WP-Droid/blob/master/web/posts-api.php) and
  change the BASE_URL variable to your blog url.
  For eg: define("BASE_URL","http://www.yourblog.com");

* Upload this file to the root of your web server and check if it can be accessed by http://www.yourblog.com/posts-api.php 

* Now edit [Config.java] (https://github.com/arjit95/WP-Droid/blob/master/app/app/src/main/java/com/xedus/tutorials/Config.java)
  and change BASE_URL string to http://www.yourblog.com
  
* Now build the app it should work as expected.  

# Libraries used

* [Gson] (https://github.com/google/gson)
* [Universal Image Loader] (https://github.com/nostra13/Android-Universal-Image-Loader)
* [Nammu] (https://github.com/tajchert/Nammu)
* [okhttp] (https://github.com/square/okhttp)


#License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
