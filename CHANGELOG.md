Changelog
=========
###TT Core 1.2.0


* Added `org.ssutt.dm.json` (now is `org.ssutt.dm.convert.json`) - transfered from TT Platform - package to transform data from TTDataManager into JSON Strings.
* `JSONConverter` now is an instance of `AbstractDataConverter` - interface to provide various output to user-agent in TT Platform. With this abstraction we can get not only JSON-formatted output, but anything, that can be converted to `String` 	
* Introduced `TTData` - an unified class to deliver data from DataManager to user-agent. It has only two fields  - `httpCode` and `message`, where we can store all the needed information about operation. Thus, we don't need to analyze the contents of message to get this or that error code in TT Platform servlets.
* As new platform-wide classes like `TTData` were introduced, all the interfaces are now named with **Abstract**. In a breif way:
	+ All the classes that don't have to be overloaded are now start with **TT**.
	+ All the interfaces, which are overloaded for SSU now start with **Abstract**.
* `org.ssutt.core.fetch.entities` renamed to `html` - as we now have `org.ssutt.dm.convert.json.entites` we don't want to mess up.
* Made all `get` methods return `TTData` instance. 
* Encapsulated all the throws in `TTDataManager`
* Created `TTModule` enum to keep labels of errors and modules. 


###TT Core 1.1.0 (squashed into 1.2.0)

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
	+ `getDepartments` now returns `Map<String, Map<String, String>` to provide extensions to existing information about departments and make it more configurabl and simplier to convert to JSON.
* `TTSQLManager`:
    + Added `setQueries` to provide `Queries` instance with their definition.
    + `getDepartments` return now `Map<String, Map<String, String>>` instead of `Map<String, String>` to provide extensions to data about departments. key is tag of department, value is all the data for this or that department (phones, for instance). Entry tagged by `name` is displayable name in Russian. The list is sorted by **names**.
* `org.ssutt.core.dm.entities` has `TableEntity` and this factory <b>no more</b> due to updates in `TTDataManager.getTT`
* `org.ssutt.core.dm.entities` and `org.ssutt.core.dm.TableParser` moved to `org.ssutt.core.fetch.entities` package as they handle only HTML data manipulations.


###TT Core 1.0.35

* First release
* Please refer [our release page](https://github.com/Plain-Solutions/tt-core/releases/tag/1.0.35)