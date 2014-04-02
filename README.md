TT Core Library
=======
[SSU TimeTables](http://ssutt.org) is a startup by Plain Solutions, group of several students, which is created to bring university schedules to your palm - your mobile devices.

General
================
TT Core is a kernel of the TT Project. It connects two main structures: university website with timetables, database, containing pre-formatted entries, groups and departments lists and an interface to represent data in web-friendly format and deliver data to  [TT Platform](https://github.com/plain-solutions/tt-platform) -  servlet module.

TT Core structure briefly can be described in this chart:
![ttcore](http://i.imgur.com/LPVCZTv.jpg)

As you can see, all the methods described as **Abstract**. That means that TT Project can be applied nearly to any university having a website with timetables. What are the modules that can be implemented and what do they do?

 * `AbstractSQLManager` - module that executes queries and works with storage: getting and putting department list, group list and timetables.
 * `AbstractQueries` - definition of SQL queries, executed by `AbstractSQLManager` implementation. This allows different SQL providers usage.
 *  `AbstractDataFetcher` - module that works with parsing stuff: getting groups and departments from university, HTML parsing and temporary DB-friendly representation of them.
 *  `AbstractDataConverter` - module that represents and converts data from the database to web-friendly format, like JSON or YAML.
 *  `AbstractDataManager` - module for unifying data between TT Core and TT Platform. Each method should return `TTData` - special structure-like class for data sharing. 

This repository contains build for [SSU](http://www.sgu.ru/) for **H2DB** and **JSON** output.

Dependencies
============
The whole library is built with Maven, but here is the brief list of main dependencies:

* Java 7 (jdk>=1.7.0)
* jsoup (>=1.7.3)
* Your formatting driver. For us it is gson (>=2.2.4)
* Your database driver. For us it is **h2**>=1.3.175


Status 
======
Concurrent Development Branch: [![Build Status](https://travis-ci.org/Plain-Solutions/tt-core.svg?branch=dev)](https://travis-ci.org/Plain-Solutions/tt-core)

Stable Master Branch:[![Build Status](https://travis-ci.org/Plain-Solutions/tt-core.svg?branch=master)](https://travis-ci.org/Plain-Solutions/tt-core)

Current  Release: [1.2.0](https://github.com/Plain-Solutions/tt-core/releases/tag/1.2.0)

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
