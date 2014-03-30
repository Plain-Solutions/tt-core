Changelog
=========

###TT Core 1.1.0

* Switched versioning system from `X.X.BUILD` to `X.X.PATCH` as it becomes to hard to watch after Travis CI build system and our own CI/CD Server.
* Updated JavaDoc to provide more detailed and specific information.
* Created this file.
* `TTDataFetcher`:
	+ Removed accessors as encapsulated formatting URL from `TTDataManager` to `DataFetcher`. 
	+ Moved `exclusions` and `globalScheduleURL` to `SSUTTDataFetcher` as it is specification of SSU website and should not be defined outside of the DF.
* `TTDataManager`:
	+ Now `deliverDataFetcherProvider()` requiries `TTDataFetcher` implementation as a parameter. This way we can specify DF for any university outside the TT Core library and create real polymorphism. 
	+ The same with `deliverDBProvider()`. Now requires `TTSQLManager` implementation and `Queries` implmenetation. This also allows to use TT Core for not only SSU TT Platform and not only with H2DB.
	+ `getTT` now returns `List<String[]>` to provide working with raw strings data across DM fully and delegate JSON-formatting to TT Platform classes as new return format of timetables is used. See api_refernce in TT Platform repository (link later) fore more information on Public API.
	+ `getDepartments` now returns `List<String[]>` to provide extensions to existing information about departments and make it more configurable.
* `TTSQLManager`:
    + Added `setQueries` to provide `Queries` instance with their definition.
    + `getDepartments` return now `List<String[]>` instead of `Map<String, String>` to provide extensions to data about departments. First agr of `String[2]` (for now) is tag of department, second is displayable name in Russian. The list is sorted by **names**.
* `org.ssutt.core.dm.entities` has `TableEntity` and this factory <b>no more</b> due to updates in `TTDataManager.getTT`
* `org.ssutt.core.dm.entities` and `org.ssutt.core.dm.TableParser` moved to `org.ssutt.core.fetch.entities` package as they handle only HTML data manipulations.


###TT Core 1.0.35

* First release
* Please refer [our release page](https://github.com/Plain-Solutions/tt-core/releases/tag/1.0.35)