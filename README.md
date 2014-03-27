TT Core Library
=======
[![Build Status](https://travis-ci.org/Plain-Solutions/tt-core.svg?branch=dev)](https://travis-ci.org/Plain-Solutions/tt-core)

General
=======
SSU TimeTables is a startup by Plain Solutions, group of several students, which is created to bring university schedules to your palm - your mobile devices. 

What is TT Core?
================
The structure of the whole TT project briefly can be described in this chart:
ADD CHART

We use our own core library to communicate between [our university schedule page](http://www.sgu.ru/schedule), database and [TT Platform module](https://github.com/plain-solutions/tt-platform).

In this repository you will find a library, which is written to communicate with SSU, however, due to high level of abstraction you can tune it to use in your university.

More, you can change database scheme and the database provider. We use H2.

Dependencies
============
The whole library is built with Maven, but here is the brief list of main dependencies:

* Java 7 (jdk>=1.7.0)
* jsoup (>=1.7.3)
* Your database driver. For us it is **h2**>=1.3.175

License
=======
TT Core Library is licensed under Apache License v2.

Authors
=======
Plain Solutions Dev Team, namely:
 
 * Vlad Slepukhin
 * Sevak Avetisyan
 * Nickolay Yurin 
 
Contacts
========
You can find more information (in Russian, however, we believe that all the developer information should be represeneted in Engish) in our VK community: 

[SSU TT](http://vk.com/ssutt)

